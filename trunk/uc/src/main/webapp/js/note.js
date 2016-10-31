function editnote()//编辑
{
  edit = true;
  var row = $("#noteTable").datagrid("getSelected");
  if (row)
  {
    $('#noteForm').form('clear');
    $("#noteForm").form("load", row);
    var u = "noteTagsForCombobox.man?nid=" + row["id"];//拼接好url
    $('#noteTags').combobox('reload', u);
    $("#noteDlg").dialog("open").dialog('setTitle', '编辑');
  }
}
function addnote()//添加公告
{
  edit = false;
  var u = "noteTagsForCombobox.man?nid=-1";//拼接好url
  $('#noteTags').combobox('reload', u);
  $("#noteDlg").dialog("open").dialog('setTitle', '添加公告');
  $('#noteForm').form('clear');
}
function savenote()//保存修改或者新增公告
{
  var u = edit ? "editNote.man" : "addNote.man";
  $("#noteForm").form("submit", {
    url: u,
    onsubmit: function () //验证表单
    {
      return $(this).form("validate");
    },
    success: function (result)
    {
      $('#noteForm').form('clear');
      $("#noteDlg").dialog("close");
      if (result === "1") //返回一个1表示成功，三个等号，威武我大js
      {
        $('#noteTable').datagrid('reload');
        $.messager.alert("提示信息", "操作成功");
      } else
      {
        $.messager.alert("错误", "保存公告失败");
      }
    }
  });
}
function noteList()
{
  addTab('公告管理', 'noteList.man');
}
function deletnote()//删除
{
  var row = $("#noteTable").datagrid("getSelected");
  if (row)
  {
    var nid = row["id"];//待删除的公告id
    $.messager.confirm('确认', '真心要删除此公告？', function (r) {
      if (r) {
        $.post('deleteNote.man', {id: nid}, function (result)
        {
          if (result === "1") {
            $('#noteTable').datagrid('reload');
          } else {
            $.messager.show({
              title: '错误',
              msg: "无法删除此公告"
            });
          }
        }, 'text');
      }
    });
  }
}


