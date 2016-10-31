/**
 * 添加一个本地脚本文件
 * @returns {undefined}
 */
function addLocalScriptFile()
{
  var t = $('#localScriptTree');
  var node = t.tree('getSelected');
  if (!node.file) {
    $.messager.prompt('脚本', '请输入文件名', function (r)
    {
      if (r)
      {
        $.post('addLocalScriptFile.man', {path: node.id, file: r}, function (d)
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
 * 删除本地脚本文件
 * @returns {undefined}
 */
function deleteLocalScriptFile()
{
  var t = $('#localScriptTree');
  var node = t.tree('getSelected');
  $.messager.confirm('确认', '真心删掉?', function (r) {
    if (r) {
      $.post('deleteLocalScriptFile.man', {path: node.id}, function (d)
      {
        var result = d;
        if (result === "1") {
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
 * 编辑本地脚本文件
 * @returns {undefined}
 */
function editLocalScriptFile(folded)
{
  var t = $('#localScriptTree');
  var node = t.tree('getSelected');
  if (node.file) {
    $('#localScriptPath').val(node.id);//赋值
    $.post('getLocalScriptContent.man', {path: node.id}, function (result)
    {
      $('#localScriptEditPanel').panel('setTitle', node.id);
      $('#localScriptContent').textbox('setValue', result);
      showContent('localScriptContent');
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
function renameLocalScriptFile()
{
  var t = $('#localScriptTree');
  var node = t.tree('getSelected');
  if (node) {
    $.messager.prompt('脚本', '请输入文件名', function (r)
    {
      if (r) {
        $.post('renameLocalScriptFile.man', {path: node.id, to: r}, function (result)
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
function submitLocalScriptForm()
{
  var c = editor.getValue();
  $("#localScriptContent").textbox("setValue", c);
  $("#localScriptForm").form("submit", {
    url: "editLocalScript.man",
    onsubmit: function () //验证表单
    {
      return $(this).form("validate");
    },
    success: function (result)
    {
      if (result === "1") //返回一个1表示成功，三个等号，威武我大js
      {
        $.messager.alert("提示", "操作成功");
      } else {
        $.messager.alert("错误", result);
      }
    }
  });
}
/**
 * 执行
 * @returns {undefined}
 */
function runLocalScriptFile()
{
  var t = $('#localScriptTree');
  var node = t.tree('getSelected');
  if (node)
  {
    $.messager.confirm('确认', '确定执行本地脚本?', function (r) {
      if (r) {
        $.post('runLocalScriptFile.man', {path: node.id}, function (result)
        {
          $.messager.alert("结果", result);
        }, 'text');
      }
    });
  } else
  {
    $.messager.alert('错误', '尚未选中任何脚本文件', 'error');
  }
}
/**
 * 上传脚本
 * @returns {undefined}
 */
function uploadLocalScript()
{
  var fileName = $('#localScriptFileBox').filebox('getValue');
  if (fileName)
  {
    var d1 = /\.[^\.]+$/.exec(fileName);
    if (d1[0] === ".zip")
    {
      $("#uploadLocalScriptForm").form("submit", {
        url: "uploadLocalScriptFile.man",
        onsubmit: function () //验证表单
        {
          return $(this).form("validate");
        },
        success: function (result)
        {
          if (result === "1") //返回一个1表示成功，三个等号，威武我大js
          {
            $.messager.alert("提示", "上传脚本文件成功");
          } else {
            $.messager.alert("错误", result);
          }
        }
      });
    } else {
      $.messager.alert("错误", "请选择脚本文件压缩包(*.zip)", "error");
    }
  }
}