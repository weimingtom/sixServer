/**
 * Created by Administrator on 2015/1/20.
 */

var EVENT_CHANGE_PAYMENT = "EVENT_CHANGE_PAYMENT";
var order = {gameid: -1, ordertype: 0, serverid: "", usercode: "", paymethod: "", defaultbank: "CCB", amount: 6};
var paymentList = {
  alibank: {paymethod: "bankPay", "defaultbank": "CCB"},
  alipay: {paymethod: ""}
};
var jsTriggerRadio = false;
var weixinPay = true;//是否选择微信支付
var codeUrl;
$(document).ready(function () {
  var characterItem = $("#characterName");
  var usernameArea = $("#usernameArea");
  var characterArea = $("#characterArea");
  var usernameItem = usernameArea.find("input");
  var serverItem = $("#server").find("select.selectpicker");
  var orderTypeItem = $("#orderTypes").find("select.selectpicker");
  var serverInfo = $("#server").find(".help-block");
  var pServerid = $("#pServerid");
  var pUserCode = $("#pUserCode");
  var pAmount = $("#pAmount");
  var pOrderType = $("#pOrderType");//普通充值是0 月卡是1 终身卡是2
  var orderId = -1;
  var isCharacterInServer = false;

  serverItem.on("change", changeServer);//绑定服务器选择事件
  orderTypeItem.on("change", changeOrderType);//绑定支付类型（月开、终身卡、普通支付）选择事件

  $("select.selectpicker").selectpicker({size: 6});//初始化选择插件

  if (parseInt(pAmount.val()) !== -1) {
    order.amount = parseInt(pAmount.val());
  }
  if (parseInt($('#gameid').val()) !== -1) {
    order.gameid = $('#gameid').val();
  }
  order.ordertype = parseInt(pOrderType.val());

  $(document).on("click", ".radio", function (e) {
    e.preventDefault();
    if (jsTriggerRadio) {
      jsTriggerRadio = false;
    } else if (order.ordertype > 0 && $(this).find("input[name=amount]").length > 0) {
      return;
    }
    $(this).trigger("check.nj.radio").find("input[type=radio]").trigger("change");
  });
  $(".payment").on("click", function (e) {
    e.preventDefault();
    var id = $(this).attr("id");
    if ($(this).hasClass("chosen")) {
      return false;
    } else {
      $(this).closest(".payment-list").find(".chosen").removeClass("chosen");
      $(".sub-payment,.other-info,#manual-recharge,#foreign-recharge,#characterArea,#serverError").hide();
      if (paymentList[id]) {
        order.paymethod = paymentList[id].paymethod || "";
        order.defaultbank = paymentList[id].defaultbank || "";
      }
      $(this).addClass("chosen").trigger(EVENT_CHANGE_PAYMENT);
    }
  });
  $("#alibank").on(EVENT_CHANGE_PAYMENT, function () {
    showBanks();
    showAmountNormal();
    showBase();
    weixinPay = false;
  });
  $("#alipay").on(EVENT_CHANGE_PAYMENT, function () {
    showAmountNormal();
    showBase();
    weixinPay = false;
  });
  $("#weixin").on(EVENT_CHANGE_PAYMENT, function () {
    showAmountNormal();
    showBase();
    weixinPay = true;
  });
  $("#manual").on(EVENT_CHANGE_PAYMENT, function () {
    showManualTable();
  });
  $("#foreign").on(EVENT_CHANGE_PAYMENT, function () {
    ShowForeignRecharge();
  });
  $(".payment:first").click();

  $("#submitOrder").on("click", function () {
    if (!checkSubmit())
      return false;
    $.fancybox({href: "#loading", modal: true, padding: 0, closeBtn: false});
    var params = {};
    for (var i in order) {
      if (order[i]) {
        params[i] = order[i];
      }
    }
    if (weixinPay)
    {
      $.getJSON("wppay.pay", params, function (data) {
        if (!data["successFlag"]) {
          $.fancybox.close();
          alert("参数错误");
        } else {
          codeUrl = data["object"];
          $("#code").empty();
          $("#code").qrcode({
            text: codeUrl,
            width: 200,
            height: 200
          });
          orderId=data["message"];
          $("#wporder").attr("href", "payordercheck.pay?orderid=" + orderId);
          $.fancybox({href: "#wppay", modal: true, padding: 0});
        }
      });
    } else
    {
      $.getJSON("alipay.pay", params, function (data) {
        if (!data["successFlag"]) {
          $.fancybox.close();
          alert("参数错误");
        } else {
          ShowConfirmWindow();
          var realForm = $("#alipaysubmit");
          realForm.empty();
          realForm.html(data["message"]);
          orderId = data["object"];
          $("#order").attr("href", "payordercheck.pay?orderid=" + orderId);
        }
      });
    }
    return false;
  });
  function ShowConfirmWindow() {
    $("#confirmCharacter").parent().show().end().text(characterItem.text());
    $("#confirmUsername").text(usernameItem.val());
    $("#confirmApp").text($("#appText").text());
    $("#confirmAppId").text(serverItem.find('> option[value="' + order.serverid + '"]').text());
    $("#confirmOrderType").text(orderTypeItem.find('> option[value="' + order.ordertype + '"]').text()).parent().show();
    $("#confirmAmount").text(order.amount + "元");
    $.fancybox({href: "#payConfirm", modal: true, padding: 0});
  }
  $("#paySuccess .btn-cancel").on("click.nj.modal", function (e) {
    $.fancybox.close();
  });
  $("#payConfirm .btn").on("click", function (e) {
    if ($(this).hasClass("btn-cancel")) {
      return false;
    }
    $(this).closest(".modal").find(".close").trigger("click");
    var realForm = $("#alipaysubmit");
    realForm[0].submit();
    $.fancybox({href: "#paySuccess", modal: true, padding: 0});
  });
  usernameItem.on("blur", function (e) {
    order.usercode = $.trim(usernameItem.val());
    if (serverItem.find("> option:checked").val() !== -1) {
      changeServer();
    }
  });
  function showBase() {
    $('.base').show();
    if (usernameItem.val() != "" || serverItem.find(">option:checked").val() != -1) {
      if (!isCharacterInServer) {
        $("#serverError").show();
        characterArea.hide();
      } else {
        characterArea.show();
        $("#serverError").hide();
      }
    } else {
      $("#serverError").hide();
      characterArea.hide();
    }
    refreshAmountByOrderType()
  }
  //更改支付类型的回调函数
  function changeOrderType() {
    var orderType = orderTypeItem.val();
    order.ordertype = orderType;
    refreshAmountByOrderType();
  }
  //根据充值类型刷新金额
  function refreshAmountByOrderType() {
    switch (order.ordertype) {
      case "0"://普通
        showAmountNormal();
        break;
      default :
        $("#amountNormal").hide();
        var matchs = orderTypeItem.find("option[value=" + order.ordertype + "]").text().match(/\d+(?=元)/);
        if (matchs != null) {
          order.amount = parseInt(matchs[0]);
        }
        /* todo 被注释的是通过代码选择金额且金额不可点的逻辑
         var checkedItem = $("#amountNormal input[type=radio][value=" + (orderType==1?6:30) + "]");
         if(checkedItem.length){
         jsTriggerRadio=true;
         checkedItem.closest(".radio").trigger("click");
         }*/
        break;
    }
  }
  //更改服务器的回调函数
  function changeServer() {
    var serverid = serverItem.val();
    if (serverid === "-1" || usernameItem.val() === "") {
      $("#serverError").hide();
      characterArea.hide();
      return;
    }
    order.serverid = serverid;
    serverInfo.text("").hide();
    if (serverid) {
      $.getJSON("paymentcheck.pay",
              {
                usercode: order.usercode,
                serverid: parseInt(order.serverid),
                gameid: order.gameid
              }, function (data) {
        isCharacterInServer = data["successFlag"];
        if (!data["successFlag"]) {
          var str = "";
          if (usernameItem.val()) {
            characterArea.hide();
            $("#serverError").show().text(data["message"] || "没有匹配到用户名，请确认输入帐号是否正确！");
          }
        } else {
          $("#serverError").hide();
          characterArea.show();
          characterItem.text(data["message"]);
        }
      });
    } else {

    }
  }
  function checkSubmit() {
    if (usernameItem.val() == "") {
      alert("请输入您的充值账号！");
      return false;
    }
    if (serverItem.find("> option:checked").length == 0 || serverItem.find("> option:checked").val() == -1) {
      alert("请选择您要充值的服务器！");
      return false;
    }
    if (!isCharacterInServer) {
      alert($("#serverError").text());
      return false;
    }
    return true;
  }

  //绑定银行选择的事件
  $("#chooseBank input[type=radio]").on("change", function (e) {
    order.defaultbank = $(this).val();
  });

  //初始化账号输入框的值
  if (pUserCode.val() != "") {
    usernameItem.val(pUserCode.val()).trigger("blur");
  }

  //初始化serverId的值
  if (pServerid.val() != -1) {
    serverItem.selectpicker("val", pServerid.val());
  }

  //初始化orderType下拉框的值
  if (pOrderType.val() >= 0) {
    orderTypeItem.selectpicker("val", pOrderType.val());
  }
});

$(document).on("check.nj.radio", ".radio", function (e) {
  e.preventDefault();
  if (!$(this).hasClass("radio-checked")) {
    $(this).addClass("radio-checked").siblings(".radio").removeClass("radio-checked");
    $(this).find("input[type=radio]").prop("checked", true);
  }
});

$(document).on("click.nj.modal", ".modal .close", function (e) {
  $.fancybox.close();
  e.preventDefault();
});

$("body").on("click", ".fancybox-overlay:visible", function (e) {
  e.target === this && $(".modal:visible .close").trigger("click.nj.modal");
});

//显示银行
function showBanks() {
  $("#chooseBank").show().find("input:checked").trigger("change");
}
//显示人工汇款
function showManualTable() {
  $('.base').hide();
  $('#submitOrder').hide();
  $("#foreign-recharge").hide();
  $("#manual-recharge").show();
}
//显示海外汇款
function ShowForeignRecharge( ) {
  $('.base').hide();
  $('#submitOrder').hide();
  $("#manual-recharge").hide();
  $("#foreign-recharge").show();
}
//显示金额区域
function showAmountNormal() {
  $("#amountNormal").show();
  var checkedItem = $("#amountNormal input[type=radio][value=" + order.amount + "]");
  if (checkedItem.length) {
    jsTriggerRadio = true;
    checkedItem.closest(".radio").trigger("click");
  } else {
    $("#amountNormal input[type=radio]:eq(0)").closest(".radio").trigger("click");
  }
}
//绑定金额复选框的更改事件
$(document).on("change", ".radio input[name=amount]", function (e) {
  order.amount = $(this).val() || 100;
});




