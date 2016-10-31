<%@page contentType="text/html" pageEncoding="UTF-8"%>    

<!--网站信息对话框-->
<div id="accountDlg" title="添加资料" modal="true" class="easyui-dialog" style="width: 260px; height: 350px; padding: 5px"
     closed="true" buttons="#siteInfoButtons"> 
  <form id="siteInfoForm" class="easyui-form" method="post" action="editSiteInfo.man" data-options="novalidate:false">
    <table cellpadding="5">
      <tr>
        <td>id:</td>
        <td><input name="id" class="easyui-textbox"  readonly="true" type="text" data-options="required:true"/></td>
      </tr>
      <tr>
        <td>标题:</td>
        <td><input name="title" class="easyui-textbox" type="text" data-options="required:true"></td>
      </tr>
      <tr>
        <td>类型:</td>
        <td>
          <select class="easyui-combobox" name="type" data-options="required:true">
            <option value="1">新闻</option>
            <option value="2">攻略</option>
            <option value="3">资源</option>
          </select>
        </td>
      </tr>
      <tr>
        <td>等级:</td>
        <td>
          <select class="easyui-combobox" name="level" data-options="required:true">
            <option value="1">一般</option>
            <option value="2">重要</option>
          </select>
        </td>
      </tr>
      <tr>
        <td>简介:</td>
        <td><input name="note" class="easyui-textbox" type="text" data-options="required:true"></td>
      </tr>
      <tr>
        <td>地址:</td>
        <td><input name="addr" class="easyui-textbox" type="text" data-options="required:true"></td>
      </tr>
      <tr>
        <td>时间:</td>
        <td><input name="time"  class="easyui-datetimebox" data-options="required:false"></td>
      </tr>
      <tr>
        <td>内容:</td>
        <td><input name="content" class="easyui-textbox" type="text" data-options="required:true"></td>
      </tr>
    </table>
    <input name="id" type="hidden"/><!--账号的id-->
  </form>
</div>
<div id="accountButtons"> 
  <a href="javascript:void(0)" class="easyui-linkbutton" onclick="saveAccount()" iconcls="icon-save">保存</a> 
  <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#accountDlg').dialog('close')" iconcls="icon-cancel">取消</a> 
</div>