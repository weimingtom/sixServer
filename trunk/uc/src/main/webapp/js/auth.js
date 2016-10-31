/**
 * 权限
 */
function editRole()//编辑角色
{
  e = true;
  var row = $("#roleTable").datagrid("getSelected");
  if (row)
  {
    $('#roleForm').form('clear');
    $("#roleForm").form("load", row);
    var u = "opsForAuthBox.man?rid=" + row["id"];//拼接好url
    $('#ops').combobox('reload', u);
    $("#roleDlg").dialog("open").dialog('setTitle', '编辑');
  }
}
function addRole()//添加角色
{
  e = false;//user.js中定义的
  var u = "opsForAuthBox.man?rid=-1";//拼接好url
  $('#ops').combobox('reload', u);
  $('#roleForm').form('clear');
  $("#roleDlg").dialog("open").dialog('setTitle', '添加角色');
}
function saveRole()//保存修改或者新增角色
{
  var url = e ? "editRole.man" : "addRole.man";
  $("#roleForm").form("submit", {
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
        $("#roleDlg").dialog("close");
        $("#roleTable").datagrid("reload");//重新加载当前页
      } else {
        $.messager.alert("错误", "真心失败的操作");
      }
    }
  });
}

function deleteRole()//删除角色
{
  var row = $("#roleTable").datagrid("getSelected");
  if (row)
  {
    var id = row["id"];//待删除的user id
    $.messager.confirm('确认', '真心要删除此角色？', function (r) {
      if (r) {
        $.post('deleteRole.man', {id: id}, function (result)
        {
          if (result === "1") {
            $('#roleTable').datagrid('reload');
          } else {
            $.messager.show({
              title: '错误',
              msg: "无法删除此角色"
            });
          }
        }, 'text');
      }
    });
  }
}