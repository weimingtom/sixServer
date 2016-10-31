<%@page contentType="text/html" pageEncoding="UTF-8"%>    
<!--重新登录-->
<div id="reloginDlg" title="重新登录" modal="true" class="easyui-dialog" style="width: 250px; height: 160px; padding: 5px"
     closed="true" buttons="#reloginButtons"> 
 <form id="reloginForm" class="easyui-form" method="post" action="relogin.man" data-options="novalidate:false">
  <table cellpadding="5">
   <tr>
    <td>邮箱:</td>
    <td><input name="email" class="easyui-textbox" type="text" data-options="required:true"/></td>
   </tr>
   <tr>
    <td>密码:</td>
    <td><input name="pwd" class="easyui-textbox" type="password" data-options="required:true"></td>
   </tr>
  </table>
 </form>
</div>
<div id="reloginButtons"> 
 <a href="javascript:void(0)" class="easyui-linkbutton" onclick="relogin()" iconcls="icon-save">登录</a> 
</div>