<%@page contentType="text/html" pageEncoding="UTF-8"%>    
<!--order编辑和添加对话框-->
<div id="orderDlg" title="完成订单" modal="true" class="easyui-dialog" style="width: 270px; height: 200px; padding: 5px"
     closed="true" buttons="#orderButtons"> 
 <form id="orderForm" class="easyui-form" method="post" data-options="novalidate:false">
  <table cellpadding="5">
   <tr>
    <td>订单:</td>
    <td><input name="oid" class="easyui-textbox" type="text" readonly="true" data-options="required:true"/></td>
   </tr>
   <tr>
    <td>any订单:</td>
    <td><input name="anyOid" class="easyui-textbox" type="text" readonly="true" data-options="required:false"/></td>
   </tr>
   <tr>
    <td>用户:</td>
    <td><input name="userName" class="easyui-textbox" type="text" readonly="true" data-options="required:true"/></td>
   </tr>
  </table>
  <input type="hidden" name="id"/><!--id字段需要上传上去-->
 </form>
</div>
<!--编辑(添加)对话框上的按钮-->
<div id="orderButtons"> 
 <a href="javascript:void(0)" class="easyui-linkbutton" onclick="summitOrder()" iconcls="icon-save">完成</a> 
 <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#orderDlg').dialog('close')" iconcls="icon-cancel">取消</a> 
</div>