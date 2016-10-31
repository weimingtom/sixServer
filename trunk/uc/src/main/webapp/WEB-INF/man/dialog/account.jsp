<%@page contentType="text/html" pageEncoding="UTF-8"%>    

<!--账号对话框-->
<div id="accountDlg" title="添加账号" modal="true" class="easyui-dialog" style="width: 260px; height: 350px; padding: 5px"
     closed="true" buttons="#accountButtons"> 
 <form id="accountForm" class="easyui-form" method="post" action="editAccount.man" data-options="novalidate:false">
  <table cellpadding="5">
   <tr>
    <td>邮箱:</td>
    <td><input name="email" class="easyui-textbox" type="text" data-options="required:true"/></td>
   </tr>
   <tr>
    <td>密码:</td>
    <td><input name="pwd" class="easyui-textbox" type="text" data-options="required:true"></td>
   </tr>
   <tr>
    <td>权限:</td>
    <td>
     <input id="roles" name="role" class="easyui-combobox" data-options="valueField:'rid',textField:'role',multiple:'false'"/>
    </td>
   </tr>
  </table>
  <input name="id" type="hidden"/><!--账号的id-->
 </form>
</div>
<div id="accountButtons"> 
 <a href="javascript:void(0)" class="easyui-linkbutton" onclick="saveAccount()" iconcls="icon-save">保存</a> 
 <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#accountDlg').dialog('close')" iconcls="icon-cancel">取消</a> 
</div>