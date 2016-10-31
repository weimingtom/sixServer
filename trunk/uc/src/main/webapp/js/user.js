function edituser()//编辑
{
  e = true;
  var row = $("#userTable").datagrid("getSelected");
  if (row)
  {
    $("#userDlg").dialog("open").dialog('setTitle', '编辑');
    $("#userForm").form("clear");
    $("#userForm").form("load", row);
  }
}
function adduser()//添加玩家账号
{
  e = false;
  $("#userDlg").dialog("open").dialog('setTitle', '添加玩家账号');
  $('#userForm').form('clear');
}
function saveuser()//保存修改或者新增玩家账号
{
  var url = e ? "editUser.man" : "addUser.man";
  $("#userForm").form("submit", {
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
        $("#userDlg").dialog("close");
        $("#userTable").datagrid("reload");//重新加载当前页
      } else {
        $.messager.alert("错误", result, "error");
      }
    }
  });
}
function userList()//设置center
{
  addTab('玩家管理', 'userList.man');//
}
function deleteuser()//删除
{
  var row = $("#userTable").datagrid("getSelected");
  if (row)
  {
    var id = row["id"];//待删除的user id
    $.messager.confirm('确认', '真心要删除此用户？', function (r) {
      if (r) {
        $.post('deleteUser.man', {id: id}, function (result)
        {
          if (result === "1") {
            $('#userTable').datagrid('reload');
          } else {
            $.messager.show({
              title: '错误',
              msg: "无法删除此用户"
            });
          }
        }, 'text');
      }
    });
  }
}
/**
 * 搜索用户
 * @returns {undefined}
 */
function searchUser()
{
  $('#userTable').datagrid('load',
          {
            userName: $("#userName")[0].value,
            userId: $('#userId')[0].value
          });
}