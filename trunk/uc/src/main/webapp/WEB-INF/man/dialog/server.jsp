<%@page contentType="text/html" pageEncoding="UTF-8"%>    
<!--服务器对话框-->
<div id="serverDlg" title="添加服务器" modal="true" class="easyui-dialog" style="width: 300px; height: 380px; padding: 5px"
     closed="true" buttons="#serverButtons"> 
  <form id="serverForm" class="easyui-form" method="post" action="editServer.man" data-options="novalidate:false">
    <table cellpadding="5">
      <tr>
        <td>id</td>
        <td><input id="serverId" name="serverId" class="easyui-numberbox"  data-options="required:true"/></td>
      </tr>
      <tr>
        <td>名字:</td>
        <td><input  name="serverName" class="easyui-textbox" type="text" data-options="required:true"/></td>
      </tr>
      <tr>
        <td>地址:</td>
        <td><input name="serverAddr" class="easyui-textbox" type="text" data-options="required:true"></td>
      </tr>
      <tr>
        <td>river端口:</td>
        <td><input name="riverPort" class="easyui-textbox" type="number" data-options="required:true"></td>
      </tr>
      <tr>
        <td>状态:</td>
        <td>
          <select class="easyui-combobox" name="serverStatus" data-options="required:true">
            <option value="-1">维护</option>
            <option value="1">畅通</option>
            <option value="2">火爆</option>
            <option value="5">新服</option>
          </select>
        </td>
      </tr>
      <tr>
        <td>渠道:</td>
        <td>
          <input id="tags" name="tags" class="easyui-combobox" data-options="valueField:'tag',textField:'tag',multiple:'true'"/>
        </td>
      </tr>
      <tr>
        <td>描述:</td>
        <td><input name="serverDesc"  class="easyui-textbox"  data-options="multiline:true" style="height: 50px"></td>
      </tr>
    </table>
    <input name="id" type="hidden"/><!--server的id-->
  </form>
</div>
<div id="serverButtons"> 
  <a href="javascript:void(0)" class="easyui-linkbutton" onclick="saveserver()" iconcls="icon-save">保存</a> 
  <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#serverDlg').dialog('close')" iconcls="icon-cancel">取消</a> 
</div>
<!--更新对话框-->
<div id="serverUpdateDlg" title="更新服务器" modal="true" class="easyui-dialog" style="width: 300px; height: 260px; padding: 5px"
     closed="true" buttons="#serverUpdateButtons"> 
  <form id="serverUpdateForm" class="easyui-form" method="post" action="updateServer.man" data-options="novalidate:false">
    <table cellpadding="5">
      <tr>
        <td>id</td>
        <td><input name="serverId" class="easyui-numberbox" readonly="true" data-options="required:true"/></td>
      </tr>
      <tr>
        <td>名字:</td>
        <td><input name="serverName" class="easyui-textbox" readonly="true" type="text" data-options="required:true"/></td>
      </tr>
      <tr>
        <td>最近提交:</td>
        <td><input name="lastRelease" class="easyui-textbox" readonly="true" type="text" data-options="required:false"/></td>
      </tr>
      <tr>
        <td>版本:</td>
        <td><input name="lastVersion" class="easyui-textbox"  type="text" data-options="required:true"/></td>
      </tr>
    </table>
    <input name="id" type="hidden"/><!--server的id-->
  </form>
</div>
<div id="serverUpdateButtons"> 
  <a href="javascript:void(0)" class="easyui-linkbutton" onclick="updateserver()" iconcls="icon-edit">更新</a> 
  <a href="javascript:void(0)" class="easyui-linkbutton" onclick="startserver()" iconcls="icon-reload">启动</a> 
  <a href="javascript:void(0)" class="easyui-linkbutton" onclick="stopserver()" iconcls="icon-cancel">停服</a> 
  <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#serverUpdateDlg').dialog('close')" iconcls="icon-cancel">取消</a> 
</div>
<!--配置对话框-->
<div id="serverConfigDlg" title="配置服务器" modal="true" class="easyui-dialog" style="width: 300px; height: 500px; padding: 5px"
     closed="true" buttons="#serverConfigButtons"> 
  <form id="serverConfigForm" class="easyui-form" method="post" action="saveConfig.man" data-options="novalidate:false">
    <table cellpadding="5">
      <tr>
        <td>id</td>
        <td><input name="serverId" class="easyui-numberbox" readonly="true" data-options="required:true"/></td>
      </tr>
      <tr>
        <td>名字:</td>
        <td><input name="name" class="easyui-textbox" readonly="true" type="text" data-options="required:true"/></td>
      </tr>
      <tr>
        <td>内网地址:</td>
        <td><input name="innerIp" class="easyui-textbox"  type="text" data-options="required:true"/></td>
      </tr>
      <tr>
        <td>内网端口:</td>
        <td><input name="innerPort" class="easyui-numberbox"  data-options="required:true"/></td>
      </tr>
      <tr>
        <td>主机用户:</td>
        <td><input name="muser" class="easyui-textbox"  type="text" data-options="required:true"/></td>
      </tr>
      <tr>
        <td>主机密码:</td>
        <td><input name="mpwd" class="easyui-textbox"  type="text" data-options="required:true"/></td>
      </tr>
      <tr>
        <td>redis地址:</td>
        <td><input name="redisIp" class="easyui-textbox" type="text" data-options="required:true"/></td>
      </tr>
      <tr>
        <td>redis端口:</td>
        <td><input name="redisPort" class="easyui-textbox"  type="text" data-options="required:true"/></td>
      </tr>
      <tr>
        <td>redis密码:</td>
        <td><input name="redisPwd" class="easyui-textbox"  type="text" data-options="required:true"/></td>
      </tr>
      <tr>
        <td>redis db:</td>
        <td><input name="redisDb" class="easyui-numberbox" type="text" data-options="required:true"/></td>
      </tr>
      <tr>
        <td>日志地址:</td>
        <td><input name="logUrl" class="easyui-textbox" type="text" data-options="required:true"/></td>
      </tr>
      <tr>
        <td>日志用户名:</td>
        <td><input name="logUser" class="easyui-textbox"  type="text" data-options="required:true"/></td>
      </tr>
      <tr>
        <td>日志密码:</td>
        <td><input name="logPwd" class="easyui-textbox"  type="text" data-options="required:true"/></td>
      </tr>
    </table>
    <input name="id" type="hidden"/><!--server的id-->
  </form>
</div>
<div id="serverConfigButtons"> 
  <a href="javascript:void(0)" class="easyui-linkbutton" onclick="saveConfig()" iconcls="icon-save">保存</a>
  <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#serverConfigDlg').dialog('close')" iconcls="icon-cancel">取消</a> 
</div>