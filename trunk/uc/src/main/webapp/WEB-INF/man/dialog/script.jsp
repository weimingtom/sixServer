<%@page contentType="text/html" pageEncoding="UTF-8"%>    
<!--脚本推送对话框-->
<div id="pushDlg" buttons="#sbs" modal="true" data-options="closed:'true'" class="easyui-dialog" title="推送" style="width:270px;height:300px;padding:10px">
 <form id="pushForm" class="easyui-form" method="post" action="pushScript.man" data-options="novalidate:false">
  <table cellpadding="5">
   <tr>
    <td>脚本:</td>
    <td><input id="path" class="easyui-textbox" type="text" name="path" data-options="required:true"/></td>
   </tr>
   <tr>
    <td>服务器:</td>
    <td><input class="easyui-textbox" type="text" name="sid" data-options="required:true"></td>
   </tr>
  </table>
 </form>
</div>
<div id="sbs"> 
 <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitPush()" iconcls="icon-save">推送</a>
 <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitExe()" iconcls="icon-save">执行</a>
 <a href="javascript:void(0)" class="easyui-linkbutton" onclick="cancelPush()" iconcls="icon-cancel">取消</a> 
</div>
<!--tree menu-->
<div id="treeMenu" class="easyui-menu" style="width:140px;">
 <div onclick="addScriptFile()" data-options="iconCls:'icon-add'">添加</div>
 <div onclick="deleteScriptFile(false)" data-options="iconCls:'icon-remove'">删除</div>
 <div onclick="renameScriptFile()" data-options="iconCls:'icon-edit'">重命名</div>
 <div class="menu-sep"></div>
 <div onclick="editScriptFile(false)" data-options="iconCls:'icon-edit'">编辑</div>
 <div onclick="pushScriptFile()" data-options="iconCls:'icon-reload'">推送</div>
 <div class="menu-sep"></div>
 <div onclick="deleteScriptFile(true)" data-options="iconCls:'icon-remove'">远程删除</div>
</div>