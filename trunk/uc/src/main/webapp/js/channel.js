var ec = false;
function editchannel()//编辑
{
  ec = true;
  var row = $("#channelTable").datagrid("getSelected");
  if (row)
  {
    $("#channelDlg").dialog("open").dialog('setTitle', '编辑');
    $("#channelForm").form("clear");
    $("#channelForm").form("load", row);
    $('#cid').textbox('readonly', true);
  }
}
function addchannel()//添加渠道
{
  ec = false;
  $("#channelDlg").dialog("open").dialog('setTitle', '添加渠道');
  $('#channelForm').form('clear');
  $('#cid').textbox('readonly', false);
}
function savechannel()//保存修改或者新增渠道
{
  var url = ec ? "editChannel.man" : "addChannel.man";
  $("#channelForm").form("submit", {
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
        $("#channelDlg").dialog("close");
        $("#channelTable").datagrid("reload");//重新加载当前页
      } else {
        $.messager.alert("错误", "真心失败的操作");
      }
    }
  });
}
function deletechannel()//删除
{
  var row = $("#channelTable").datagrid("getSelected");
  if (row)
  {
    var id = row["id"];//待删除channel id
    $.messager.confirm('确认', '真心要删除此渠道？', function (r) {
      if (r) {
        $.post('deleteChannel.man', {id: id}, function (result)
        {
          if (result === "1") {
            $('#channelTable').datagrid('reload');
          } else {
            $.messager.show({
              title: '错误',
              msg: "无法删除此渠道"
            });
          }
        }, 'text');
      }
    });
  }
}
