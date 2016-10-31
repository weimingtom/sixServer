<%@page contentType="text/html" pageEncoding="UTF-8"%>  
<!--channel对话框-->
<div id="channelDlg" title="添加渠道" modal="true" class="easyui-dialog" style="width: 250px; height: 270px; padding: 5px"
     closed="true" buttons="#channel-buttons"> 
  <form id="channelForm" class="easyui-form" method="post" action="editChannel.man" data-options="novalidate:false">
    <table cellpadding="5">
      <tr>
        <td>id:</td>
        <td><input id="cid" name="cid" class="easyui-textbox" type="number" data-options="required:true"/></td>
      </tr>
      <tr>
        <td>名字:</td>
        <td><input name="name" class="easyui-textbox" type="text" data-options="required:true"></td>
      </tr>
      <tr>
        <td>分类:</td>
        <td><input name="tag"  class="easyui-textbox" type="text" data-options="required:true"></td>
      </tr>
      <tr>
        <td>bid:</td>
        <td><input name="bid"  class="easyui-textbox" type="text" data-options="required:true"></td>
      </tr>
      <tr>
        <td>描述:</td>
        <td><input name="note"  class="easyui-textbox" type="text" data-options="required:false"></td>
      </tr>
    </table>
    <input type="hidden" name="id"/><!--id字段需要上传上去-->
  </form>
</div>
<div id="channel-buttons"> 
  <a href="javascript:void(0)" class="easyui-linkbutton" onclick="savechannel()" iconcls="icon-save">保存</a> 
  <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#channelDlg').dialog('close')" iconcls="icon-cancel">取消</a> 
</div>