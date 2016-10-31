/**
 * 
 * 操作game的js
 */
var curGameId;//当前选中的游戏的id
var e = false;//编辑

/**
 * 添加游戏
 * @returns {undefined}
 */
function addGame()
{
  e = false;
  $("#gameDlg").dialog("open").dialog('setTitle', '添加游戏');
  $('#gameForm').form('clear');
}
/**
 * 编辑
 * @returns {undefined}
 */
function editGame()//编辑
{
  e = true;
  var row = $("#gameTable").datagrid("getSelected");
  if (row)
  {
    $("#gameDlg").dialog("open").dialog('setTitle', '编辑');
    $("#gameForm").form("clear");
    $("#gameForm").form("load", row);
  }
}
/**
 * 保存
 * @returns {undefined}
 */
function saveGame()
{
  var url = e ? "editGame.man" : "addGame.man";
  $("#gameForm").form("submit", {
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
        $("#gameDlg").dialog("close");
        $("#gameTable").datagrid("reload");
      }
      else {
        $.messager.alert("错误", "保存游戏失败", "error");
      }
    }
  });
}
/**
 * 删除游戏
 * @returns {undefined}
 */
function deleteGame()
{
  var row = $("#gameTable").datagrid("getSelected");
  if (row)
  {
    var gameId = row["id"];//待删除的游戏id
    $.messager.confirm('确认', '真心要删除此游戏？', function (r) {
      if (r) {
        $.post('deleteGame.man', {id: gameId}, function (result)
        {
          if (result === "1") {
            $('#gameTable').datagrid('reload');
          } else {
            $.messager.alert("错误", "删除游戏失败", "error");
          }
        }, 'text');
      }
    });
  }
}
function formatGameBox(row) //格式化用于显示游戏列表的box
{
  var s = '<span style="font-weight:bold">' + row.gameName + '</span><br/>' +
          '<span style="color:#888">' + row.gameDec + '</span>';
  return s;
}
function selectGame(record)//选择一个游戏,跳转到server页面
{
  curGameId = record["gameId"];
  $.post('setCurGame.man', {gameId: curGameId}, function (result)
  {
    if (result === "1") {
      serverList();//跳转到server列表
    } else {
      $.messager.show({
        title: '错误',
        msg: "所选游戏不存在，请刷新"
      });
    }
  }, 'text');
}