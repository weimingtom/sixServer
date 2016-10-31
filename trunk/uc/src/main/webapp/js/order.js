function overOrder()//完成订单
{
  var row = $("#orderTable").datagrid("getSelected");
  if (row)
  {
    $("#orderDlg").dialog("open").dialog('setTitle', '完成');
    $("#orderForm").form("clear");
    $("#orderForm").form("load", row);
  }
}
function summitOrder()//提交订单
{
  var row = $("#orderTable").datagrid("getSelected");
  if (row)
  {
    $.post('overOrder.man', {id: row["id"]}, function (result)
    {
      if (result === "1") {
        $('#orderTable').datagrid('reload');
      } else {
        $.messager.alert('错误', '完成订单失败', 'error');
      }
    }, 'text');
  }
}
function deleteOrder()//删除
{
  var row = $("#orderTable").datagrid("getSelected");
  if (row)
  {
    var id = row["id"];
    $.messager.confirm('确认', '真心要删除此订单？', function (r) {
      if (r) {
        $.post('deleteOrder.man', {id: id}, function (result)
        {
          if (result === "1") {
            $('#orderTable').datagrid('reload');
          } else {
            $.messager.alert('错误', '删除订单失败', 'error');
          }
        }, 'text');
      }
    });
  }
}
/**
 * 搜索
 * @returns
 */
function searchOrder()
{
  $('#orderTable').datagrid('load',
          {
            serverId: $('#sid')[0].value,
            userName: $("#uName")[0].value,
            userId: $('#uId')[0].value,
            start: $('#orderStartTime').datetimebox('getValue'),
            end: $('#orderEndTime').datetimebox('getValue')
          });
}