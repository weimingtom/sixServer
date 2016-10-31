/* global curGameId */

var edit = false;//是否为编辑模式

function edit()//是否为edit模式
{
  return edit;
}

function editserver()//编辑
{
  edit = true;
  var row = $("#serverTable").datagrid("getSelected");
  if (row)
  {
    $('#serverForm').form('clear');
    $("#serverForm").form("load", row);
    var u = "tagsForCombobox.man?sid=" + row["id"];//拼接好url
    $('#tags').combobox('reload', u);
    $("#serverDlg").dialog("open").dialog('setTitle', '编辑');
    $('#serverId').textbox('readonly', true);
  }
}
function addserver()//添加服务器
{
  edit = false;
  var u = "tagsForCombobox.man?sid=-1";//拼接好url
  $('#tags').combobox('reload', u);
  $("#serverDlg").dialog("open").dialog('setTitle', '添加服务器');
  $('#serverForm').form('clear');
  $('#serverId').textbox('readonly', false);
}
function saveserver()//保存修改或者新增服务器
{
  var u = edit ? "editServer.man" : "addServer.man";
  $("#serverForm").form("submit", {
    url: u,
    onsubmit: function () //验证表单
    {
      return $(this).form("validate");
    },
    success: function (result)
    {
      $('#serverForm').form('clear');
      $("#serverDlg").dialog("close");
      if (result === "1") //返回一个1表示成功，三个等号，威武我大js
      {
        $('#serverTable').datagrid('reload');
        $.messager.alert("提示信息", "操作成功");
      } else
      {
        $.messager.alert("错误", "保存游戏失败");
      }
    }
  });
}
function serverList()//设置center维服务器列表
{
  addTab('服务器管理', 'serverList.man');
}
function deleteserver()//删除
{
  var row = $("#serverTable").datagrid("getSelected");
  if (row)
  {
    var serverId = row["id"];//待删除的服务器id
    $.messager.confirm('确认', '真心要删除此服务器？', function (r) {
      if (r) {
        $.post('deleteServer.man', {id: serverId}, function (result)
        {
          if (result === "1") {
            $('#serverTable').datagrid('reload');
          } else {
            $.messager.show({
              title: '错误',
              msg: "无法删除此服务器"
            });
          }
        }, 'text');
      }
    });
  }
}
/**
 * 更新
 * @returns {undefined}
 */
function updateserverDlg()
{
  var row = $("#serverTable").datagrid("getSelected");
  if (row)
  {
    $("#serverUpdateForm").form("load", row);
    $("#serverUpdateDlg").dialog("open").dialog('setTitle', '更新 | 重启');
  }
}

/**
 * 更新服务器
 * @returns {undefined}
 */
function updateserver()
{
  var row = $("#serverTable").datagrid("getSelected");
  if (row)
  {
    var u = "updateServer.man?sid=" + row["id"];
    $("#serverUpdateForm").form("submit", {
      url: u,
      onsubmit: function () //验证表单
      {
        return $(this).form("validate");
      },
      success: function (result)
      {
        $('#serverUpdateForm').form('clear');
        $("#serverUpdateDlg").dialog("close");
        $('#serverTable').datagrid('reload');
        $.messager.alert("更新结果", result);
      }
    });
  }
}
/**
 * 停服
 * @returns {undefined}
 */
function stopserver()
{
  var row = $("#serverTable").datagrid("getSelected");
  if (row)
  {
    var serverId = row["id"];
    $.messager.confirm('确认', '真心要停止此服务器？', function (r) {
      if (r) {
        $.post('stopServer.man', {sid: serverId}, function (result)
        {
          $.messager.alert("停止服务器", result);
        }, 'text');
      }
    });
  }
}
/**
 * 启动服务器
 * @returns {undefined}
 */
function startserver()
{
  var row = $("#serverTable").datagrid("getSelected");
  if (row)
  {
    var serverId = row["id"];
    $.messager.confirm('确认', '真心要启动此服务器？', function (r) {
      if (r) {
        $.post('startServer.man', {sid: serverId}, function (result)
        {
          $.messager.alert("启动服务器", result);
        }, 'text');
      }
    });
  }
}
/**
 * 配置
 * @returns {undefined}
 */
function configserverDlg()
{
  var row = $("#serverTable").datagrid("getSelected");
  if (row)
  {
    $.post("serverConfig.man", {sid: row["id"]}, function (result)
    {
      var j = JSON.parse(result);
      $("#serverConfigForm").form("clear");
      $("#serverConfigForm").form("load", j);
      $("#serverConfigDlg").dialog("open").dialog('setTitle', '配置');
    }, 'text');
  }
}
/**
 * 保存服务器配置
 * @returns {undefined}
 */
function saveConfig()
{
  var u = "saveConfig.man";
  $("#serverConfigForm").form("submit", {
    url: u,
    onsubmit: function () //验证表单
    {
      return $(this).form("validate");
    },
    success: function (result)
    {
      $('#serverConfigForm').form('clear');
      $("#serverConfigDlg").dialog("close");
      $('#serverTable').datagrid('reload');
      $.messager.alert("服务器配置", result);
    }
  });
}
