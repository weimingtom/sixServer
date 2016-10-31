/**
 * 脏词
 */
function editDirty()//编辑脏词
{
  e = true;
  var row = $("#dirtyTable").datagrid("getSelected");
  if (row)
  {
    $('#dirtyForm').form('clear');
    $("#dirtyForm").form("load", row);
    $("#dirtyDlg").dialog("open").dialog('setTitle', '编辑');
  }
}
function addDirty()//添加脏词
{
  e = false;//user.js中定义的
  $('#dirtyForm').form('clear');
  $("#dirtyDlg").dialog("open").dialog('setTitle', '添加脏词');
}
function saveDirty()//保存修改或者新增脏词
{
  var url = e ? "editDirty.man" : "addDirty.man";
  $("#dirtyForm").form("submit", {
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
        $("#dirtyDlg").dialog("close");
        $("#dirtyTable").datagrid("reload");//重新加载当前页
      }
      else {
        $.messager.alert("错误", "真心失败的操作");
      }
    }
  });
}

function deleteDirty()//删除账号
{
  var row = $("#dirtyTable").datagrid("getSelected");
  if (row)
  {
    var id = row["id"];//待删除的dirty id
    $.messager.confirm('确认', '真心要删除此脏词？', function (r) {
      if (r) {
        $.post('deleteDirty.man', {id: id}, function (result)
        {
          if (result === "1") {
            $('#dirtyTable').datagrid('reload');
          } else {
            $.messager.alert("错误", "真心失败的操作","error");
          }
        }, 'text');
      }
    });
  }
}