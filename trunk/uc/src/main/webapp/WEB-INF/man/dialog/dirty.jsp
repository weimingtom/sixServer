<%@page contentType="text/html" pageEncoding="UTF-8"%>    

<!--脏词对话框-->
<div id="dirtyDlg" title="添加脏词" modal="true" class="easyui-dialog" style="width: 260px; height: 200px; padding: 5px"
     closed="true" buttons="#dirtyButtons"> 
 <form id="dirtyForm" class="easyui-form" method="post" action="editDirty.man" data-options="novalidate:false">
  <table cellpadding="5">
   <tr>
    <td>脏词:</td>
    <td><input name="word" class="easyui-textbox" type="text" data-options="required:true"/></td>
   </tr>
   <tr>
    <td>描述:</td>
    <td><input name="note" class="easyui-textbox" type="text" data-options="required:false"></td>
   </tr>
  </table>
  <input name="id" type="hidden"/><!--脏词的id-->
 </form>
</div>
<div id="dirtyButtons"> 
 <a href="javascript:void(0)" class="easyui-linkbutton" onclick="saveDirty()" iconcls="icon-save">保存</a> 
 <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#dirtyDlg').dialog('close')" iconcls="icon-cancel">取消</a> 
</div>
