<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div style="background-color: #DDDDDD">
 <table cellpadding="0" cellspacing="0" style="width: 100%;">
  <tr>
   <td>
<h3 style="color: blue;">用户中心</h3>
   </td>
   <td style="padding-right: 5px; text-align: right; vertical-align: central;">
    <div>
     <span style="color:blue;">欢 迎 ${sessionScope.role.name}</span>
     <a href="#" class="easyui-menubutton" data-options="menu:'#mm1'" style="color:red;">${sessionScope.acc.email}</a>  
     <div id="mm1">
      <div onclick="$('#reloginDlg').dialog('open')">重新登录</div>
      <div class="menu-sep"></div>
      <div onclick="logout()">退出系统</div>
     </div>
    </div>
   </td>
  </tr>
 </table>
</div>