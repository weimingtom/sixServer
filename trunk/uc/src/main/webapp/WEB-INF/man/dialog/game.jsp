<%@page contentType="text/html" pageEncoding="UTF-8"%>  
<!--弹出游戏对话框-->
<div id="gameDlg" modal="true" class="easyui-dialog" style="width: 320px; height: 370px; padding: 10px"
     closed="true" buttons="#gameButtons"> 
 <form id="gameForm" class="easyui-form" method="post" action="editGame.man" data-options="novalidate:false">
  <table cellpadding="5">
   <tr>
    <td>id</td>
    <td><input name="gameId" class="easyui-textbox" type="number"  data-options="required:true"/></td>
   </tr>
   <tr>
    <td>名字:</td>
    <td><input name="gameName" class="easyui-textbox" type="text" data-options="required:true"/></td>
   </tr>
   <tr>
    <td>bid:</td>
    <td><input name="gameBid" class="easyui-textbox" type="text" data-options="required:true"></td>
   </tr>
   <tr>
    <td>状态:</td>
    <td>
     <select class="easyui-combobox" name="gameStatus">
      <option value="-1">开发</option>
      <option value="0">维护</option>
      <option value="1">测试</option>
      <option value="2">运营</option>
     </select>
    </td>
   </tr>
   <tr>
    <td>秘钥:</td>
    <td><input name="desKey" class="easyui-textbox" type="text" data-options="required:true"></td>
   </tr>
   <tr>
    <td>any私有key:</td>
    <td><input name="anyPrivateKey" class="easyui-textbox" type="text" data-options="required:true"></td>
   </tr>
   <tr>
    <td>描述:</td>
    <td><input name="gameDec" class="easyui-textbox"  data-options="multiline:true"  style="height:50px"></td>
   </tr>
  </table>
  <input name="id" type="hidden"/><!--game的id-->
 </form>
</div>
<div id="gameButtons"> 
 <a href="javascript:void(0)" class="easyui-linkbutton" onclick="saveGame()" iconcls="icon-save">保存</a> 
 <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#gameDlg').dialog('close')" iconcls="icon-cancel">取消</a> 
</div>
