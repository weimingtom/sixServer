/**
 * 客户端版本
 */
function editClient()//编辑
{
  e = true;
  var row = $("#clientTable").datagrid("getSelected");
  if (row)
  {
    $("#clientForm").form("clear");
    $("#clientForm").form("load", row);
    var u = "clientChannelBox.man?cid=" + row["id"];//拼接好url
    $('#clientChannelBox').combobox('reload', u);
    $("#clientDlg").dialog("open").dialog('setTitle', '编辑');
  }
}
function addClient()//添加
{
  e = false;//user.js中定义的
  var u = "clientChannelBox.man?cid=-1";//拼接好url
  $('#clientChannelBox').combobox('reload', u);
  $('#clientForm').form('clear');
  $("#clientDlg").dialog("open").dialog('setTitle', '添加客户端版本');
}
function saveClient()//保存修改或者新增
{
  var url = e ? "editClient.man" : "addClient.man";
  $("#clientForm").form("submit", {
    url: url,
    onsubmit: function () //验证表单
    {
      return $(this).form("validate");
    },
    success: function (result)
    {
      if (result === "1") //返回一个1表示成功，三个等号，威武我大js
      {
        $.messager.alert("提示信息", "操作成功");
        $("#clientDlg").dialog("close");
        $("#clientTable").datagrid("reload");//重新加载当前页
      }
      else {
        $.messager.alert("错误", "真心失败的操作", "error");
      }
    }
  });
}

function deleteClient()//删除
{
  var row = $("#clientTable").datagrid("getSelected");
  if (row)
  {
    var id = row["id"];
    $.messager.confirm('确认', '真心要删除此客户端？', function (r) {
      if (r) {
        $.post('deleteClient.man', {id: id}, function (result)
        {
          if (result === "1") {
            $('#clientTable').datagrid('reload');
          } else {
            $.messager.alert("错误", result, "error");
          }
        }, 'text');
      }
    });
  }
}