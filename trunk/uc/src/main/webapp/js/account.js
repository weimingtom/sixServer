/**
 * 账号
 */
function editAccount()//编辑角色
{
  e = true;
  var row = $("#accountTable").datagrid("getSelected");
  if (row)
  {
    $('#accountForm').form('clear');
    $("#accountForm").form("load", row);
    var u = "rolesBox.man?aid=" + row["id"];//拼接好url
    $('#roles').combobox('reload', u);
    $("#accountDlg").dialog("open").dialog('setTitle', '编辑');
  }
}
function addAccount()//添加账号
{
  e = false;//user.js中定义的
  var u = "rolesBox.man?aid=-1";//拼接好url
  $('#roles').combobox('reload', u);
  $('#accountForm').form('clear');
  $("#accountDlg").dialog("open").dialog('setTitle', '添加账号');
}
function saveAccount()//保存修改或者新增账号
{
  var url = e ? "editAccount.man" : "addAccount.man";
  $("#accountForm").form("submit", {
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
        $("#accountDlg").dialog("close");
        $("#accountTable").datagrid("reload");//重新加载当前页
      }
      else {
        $.messager.alert("错误", "真心失败的操作");
      }
    }
  });
}

function deleteAccount()//删除账号
{
  var row = $("#accountTable").datagrid("getSelected");
  if (row)
  {
    var id = row["id"];//待删除的account id
    $.messager.confirm('确认', '真心要删除此账号？', function (r) {
      if (r) {
        $.post('deleteAccount.man', {id: id}, function (result)
        {
          if (result === "1") {
            $('#accountTable').datagrid('reload');
          } else {
            $.messager.show({
              title: '错误',
              msg: "无法删除此账号"
            });
          }
        }, 'text');
      }
    });
  }
}
/**
 * 重新登录
 * @returns {undefined}
 */
function relogin()
{
  $("#reloginForm").form("submit", {
    onsubmit: function () //验证表单
    {
      return $(this).form("validate");
    },
    success: function (result)
    {
      if (result === "1") //返回一个1表示成功，三个等号，威武我大js
      {
        $("#reloginDlg").dialog("close");
        self.location="admin.man";//刷新一次
      }
      else {
        $.messager.alert("错误", result);
      }
    }
  });
}
/**
 * 退出
 * @returns {undefined}
 */
function logout()
{
  self.location="logout.man";
}