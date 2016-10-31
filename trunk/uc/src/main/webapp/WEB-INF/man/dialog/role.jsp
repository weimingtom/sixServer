<%@page contentType="text/html" pageEncoding="UTF-8"%>    
<!--角色对话框-->
<div id="roleDlg" title="添加角色" modal="true" class="easyui-dialog" style="width: 260px; height: 200px; padding: 5px"
     closed="true" buttons="#roleButtons"> 
 <form id="roleForm" class="easyui-form" method="post" action="editRole.man" data-options="novalidate:false">
  <table cellpadding="5">
   <tr>
    <td>名字:</td>
    <td><input  name="name" class="easyui-textbox" type="text" data-options="required:true"/></td>
   </tr>
   <tr>
    <td>描述:</td>
    <td><input name="note" class="easyui-textbox" type="text" data-options="required:true"></td>
   </tr>
   <tr>
    <td>操作:</td>
    <td>
     <input id="ops" name="ops" class="easyui-combobox" data-options="valueField:'op',textField:'note',multiple:'true'"/>
    </td>
   </tr>
  </table>
  <input name="id" type="hidden"/><!--role的id-->
 </form>
</div>
<div id="roleButtons"> 
 <a href="javascript:void(0)" class="easyui-linkbutton" onclick="saveRole()" iconcls="icon-save">保存</a> 
 <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#roleDlg').dialog('close')" iconcls="icon-cancel">取消</a> 
</div>