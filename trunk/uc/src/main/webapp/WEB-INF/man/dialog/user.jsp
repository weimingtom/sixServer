<%@page contentType="text/html" pageEncoding="UTF-8"%>    
<!--user编辑和添加对话框-->
<div id="userDlg" title="添加玩家账号" modal="true" class="easyui-dialog" style="width: 270px; height: 300px; padding: 5px"
     closed="true" buttons="#userButtons"> 
 <form id="userForm" class="easyui-form" method="post" action="editUser.man" data-options="novalidate:false">
  <table cellpadding="5">
   <tr>
    <td>名字:</td>
    <td><input name="usercode" class="easyui-textbox" type="text" data-options="required:true"/></td>
   </tr>
   <tr>
    <td>密码:</td>
    <td><input name="password" class="easyui-textbox" type="text" data-options="required:true"></td>
   </tr>
   <tr>
    <td>渠道:</td>
    <td><input name="channelId"  class="easyui-textbox" type="number" data-options="required:true"></td>
   </tr>
   <tr>
    <td>测试:</td>
    <td>
     <select class="easyui-combobox" name="testing" data-options="required:true">
      <option value="true">true</option>
      <option value="false">false</option>
     </select>
    </td>
   </tr>
   <tr>
    <td>封禁:</td>
    <td><input name="unlockTime"  class="easyui-datetimebox" data-options="required:false"></td>
   </tr>
  </table>
  <input type="hidden" name="id"/><!--id字段需要上传上去-->
 </form>
</div>
<!--编辑(添加)对话框上的按钮-->
<div id="userButtons"> 
 <a href="javascript:void(0)" class="easyui-linkbutton" onclick="saveuser()" iconcls="icon-save">保存</a> 
 <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#userDlg').dialog('close')" iconcls="icon-cancel">取消</a> 
</div>