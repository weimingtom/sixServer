/**
 * 添加一个脚本文件
 * @returns {undefined}
 */
function addScriptFile()
{
  var t = $('#scriptTree');
  var node = t.tree('getSelected');
  if (!node.file) {
    $.messager.prompt('脚本', '请输入文件名', function (r)
    {
      if (r)
      {
        $.post('addScriptFile.man', {path: node.id, file: r}, function (d)
        {
          var obj = JSON.parse(d);
          var result = obj["result"];
          if (result === 1) {
            t.tree('reload');
          } else {
            $.messager.alert('错误', '创建文件失败', 'error');
          }
        }, 'text');
      }
    });
  } else {
    $.messager.alert('错误', '真心不能，臣妾做不到啊', 'error');
  }
}
/**
 * 删除脚本文件
 * @returns {undefined}
 */
function deleteScriptFile(remote)
{
  var t = $('#scriptTree');
  var node = t.tree('getSelected');
  $.messager.confirm('确认', '真心删掉?', function (r) {
    if (r) {
      $.post('deleteScriptFile.man', {path: node.id, river: remote}, function (d)
      {
        var result = d;
        if (result === "1") {
          if (!remote)
            t.tree('remove', node.target);
          $.messager.alert('消息', '删除成功', 'info');
        } else {
          $.messager.alert('错误', '操作失败', 'error');
        }
      }, 'text');
    }
  });
}
/**
 * 编辑脚本文件
 * @returns {undefined}
 */
function editScriptFile(folded)
{
  var t = $('#scriptTree');
  var node = t.tree('getSelected');
  if (node.file) {
    $('#scriptPath').val(node.id);//赋值
    $.post('getScriptContent.man', {path: node.id}, function (result)
    {
      $('#scriptEditPanel').panel('setTitle', node.id);
      $('#scriptContent').textbox('setValue', result);
      showContent('scriptContent');
      //$('#scriptContent').val(result);
    }, 'text');
  } else if (folded) {
    if (node.state === 'closed') {
      t.tree('expand', node.target);
    } else {
      t.tree('collapse', node.target);
    }
  } else {
    $.messager.alert('错误', '真心不能，臣妾做不到啊', 'error');
  }
}
/**
 * 重命名脚本文件
 * @returns {undefined}
 */
function renameScriptFile()
{
  var t = $('#scriptTree');
  var node = t.tree('getSelected');
  if (node) {
    $.messager.prompt('脚本', '请输入文件名', function (r)
    {
      if (r) {
        $.post('renameScriptFile.man', {path: node.id, to: r}, function (result)
        {
          if (result === "1")
            t.tree('reload');
          else
            $.messager.alert('错误', result, 'error');
        }, 'text');
      }
    });
  } else {
    $.messager.alert('错误', '尚未选中任何脚本文件', 'error');
  }
}
/**
 * 提交修改过的脚本
 * @returns {undefined}
 */
function submitScriptForm()
{
  var c = editor.getValue();
  $("#scriptContent").textbox("setValue", c);
  $("#scriptForm").form("submit", {
    url: "editScript.man",
    onsubmit: function () //验证表单
    {
      return $(this).form("validate");
    },
    success: function (result)
    {
      if (result === "1") //返回一个1表示成功，三个等号，威武我大js
      {

        $.messager.alert("提示", "操作成功");
      }
      else {
        $.messager.alert("错误", result);
      }
    }
  });
}
/**
 * 推送脚本（弹出推送窗口）
 * @returns {undefined}
 */
function pushScriptFile()
{
  var t = $('#scriptTree');
  var node = t.tree('getSelected');
  $("#pushDlg").dialog("open").dialog('setTitle', '推送');
  $("#path").textbox('setValue', node.id);
}
/**
 * 推送
 * @returns {undefined}
 */
function submitPush()
{
  $("#pushForm").form("submit", {
    url: "pushScript.man",
    onsubmit: function () //验证表单
    {
      return $(this).form("validate");
    },
    success: function (result)
    {
      $("#pushDlg").dialog("close");
      $.messager.alert("结果", result);
    }
  });
}
/**
 * 推送并执行
 * @returns {undefined}
 */
function submitExe()
{
  $("#pushForm").form("submit", {
    url: "pushAndExeScript.man",
    onsubmit: function () //验证表单
    {
      return $(this).form("validate");
    },
    success: function (result)
    {
      $("#pushDlg").dialog("close");
      $.messager.alert("结果", result);
    }
  });
}
/**
 * 取消推送
 * @returns {undefined}
 */
function cancelPush()
{
  $("#pushDlg").dialog("close");
}
/**
 * 上传脚本
 * @returns {undefined}
 */
function uploadScript()
{
  var fileName = $('#scriptFileBox').filebox('getValue');
  if (fileName)
  {
    var d1 = /\.[^\.]+$/.exec(fileName);
    if (d1[0] === ".zip")
    {
      $("#uploadScriptForm").form("submit", {
        url: "uploadScriptFile.man",
        onsubmit: function () //验证表单
        {
          return $(this).form("validate");
        },
        success: function (result)
        {
          if (result === "1") //返回一个1表示成功，三个等号，威武我大js
          {
            $.messager.alert("提示", "上传脚本文件成功");
          }
          else {
            $.messager.alert("错误", result);
          }
        }
      });
    } else {
      $.messager.alert("错误", "请选择脚本文件压缩包(*.zip)", "error");
    }
  }

}