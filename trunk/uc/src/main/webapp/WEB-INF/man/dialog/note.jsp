<%@page contentType="text/html" pageEncoding="UTF-8"%>    
<!--公告对话框-->
<div id="noteDlg" title="添加公告" modal="true" class="easyui-dialog" style="width: 300px; height: 440px; padding: 5px"
     closed="true" buttons="#noteButtons"> 
  <form id="noteForm" class="easyui-form" method="post" action="editNote.man" data-options="novalidate:false">
    <table cellpadding="5">
      <tr>
        <td>标题:</td>
        <td><input  name="title" class="easyui-textbox" type="text" data-options="required:true"/></td>
      </tr>
      <tr>
        <td>内容:</td>
        <td><input name="content" class="easyui-textbox"  data-options="multiline:true,required:true" style="height: 160px"></td>
      </tr>
      <tr>
        <td>状态:</td>
        <td>
          <select class="easyui-combobox" name="status" data-options="required:true">
            <option value="0">开启</option>
            <option value="1">关闭</option>
          </select>
        </td>
      </tr>
      <tr>
        <td>渠道:</td>
        <td>
          <input id="noteTags" name="ts" class="easyui-combobox" data-options="valueField:'t',textField:'t',multiple:'true'"/>
        </td>
      </tr>
      <tr>
        <td>开始时间:</td>
        <td><input name="ctime"  class="easyui-datetimebox" data-options="required:true"></td>
      </tr>
      <tr>
        <td>结束时间:</td>
        <td><input name="ftime"  class="easyui-datetimebox" data-options="required:true"></td>
      </tr>
    </table>
    <input name="id" type="hidden"/><!--note的id-->
  </form>
</div>
<div id="noteButtons"> 
  <a href="javascript:void(0)" class="easyui-linkbutton" onclick="savenote()" iconcls="icon-save">保存</a> 
  <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#noteDlg').dialog('close')" iconcls="icon-cancel">取消</a> 
</div>