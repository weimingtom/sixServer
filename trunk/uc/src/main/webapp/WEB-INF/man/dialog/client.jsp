<%@page contentType="text/html" pageEncoding="UTF-8"%>    
<!--客户端对话框-->
<div id="clientDlg" title="添加客户端" modal="true" class="easyui-dialog" style="width: 260px; height: 300px; padding: 5px"
     closed="true" buttons="#clientButtons"> 
 <form id="clientForm" class="easyui-form" method="post" action="editClient.man" data-options="novalidate:false">
  <table cellpadding="5">
   <tr>
    <td>版本:</td>
    <td><input name="version" class="easyui-textbox" type="text" data-options="required:true"/></td>
   </tr>
   <tr>
    <td>更新时间:</td>
    <td><input name="updateTime" class="easyui-datetimebox" type="text" data-options="required:true"></td>
   </tr>
   <tr>
    <td>地址:</td>
    <td><input name="address" class="easyui-textbox" type="text" data-options="required:true"></td>
   </tr>
   <tr>
    <td>强制更新:</td>
    <td>
     <select class="easyui-combobox" name="forceUpdate" data-options="required:true">
      <option value="true">true</option>
      <option value="false">false</option>
     </select>
    </td>
   </tr>
   <tr>
    <td>更新开启:</td>
    <td>
     <select class="easyui-combobox" name="open" data-options="required:true">
      <option value="true">true</option>
      <option value="false">false</option>
     </select>
    </td>
   </tr>
   <tr>
    <td>渠道:</td>
    <td>
     <input id="clientChannelBox"  name="clientChannelBox" class="easyui-combobox" data-options="valueField:'id',textField:'name',multiple:'true'"/>
    </td>
   </tr>
  </table>
  <input name="id" type="hidden"/><!--客户端的id-->
 </form>
</div>
<div id="clientButtons"> 
 <a href="javascript:void(0)" class="easyui-linkbutton" onclick="saveClient()" iconcls="icon-save">保存</a> 
 <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#clientDlg').dialog('close')" iconcls="icon-cancel">取消</a> 
</div>