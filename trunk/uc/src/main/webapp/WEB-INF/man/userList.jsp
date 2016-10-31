<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>  
  <c:when test="${not empty error}">
    <!--错误信息-->
    <script>
      $.messager.alert('错误', '${error}', 'error');
    </script>
  </c:when>  
  <c:otherwise>
    <!--玩家列表-->
    <table id="userTable" title="玩家账号" class="easyui-datagrid" style="width: 100%; height: auto"
           url="userJsonList.man" toolbar="#userToolbar" pagination="true" striped="true"
           data-options="onDblClickRow:edituser" idField="id" loadMsg="玩命加载中" pageSize="20" pageList="[10,15,20,25,30,35,40,45,50]"
           fitcolumns="true" singleselect="true">
     <thead>
      <tr>
       <th field="id" width="50">
        id
       </th>
       <th field="usercode" width="50">
        名字
       </th>
       <th field="password" width="50">
        密码
       </th>
       <th field="channelId" width="50">
        渠道
       </th>
       <th field="testing" width="50">
        测试
       </th>
       <th field="anyChannel" width="50">
        any渠道
       </th>
       <th field="anyUid" width="50">
        any账号
       </th>
       <th field="unlockTime" width="50">
        封禁
       </th>
       <th field="regTime" width="50">
        注册时间
       </th>
      </tr>
     </thead>
    </table>
    <!--工具条-->
    <div id="userToolbar">
     <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-add" onclick="adduser()" plain="true">添加</a> 
     <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-edit" onclick="edituser()" plain="true">修改</a> 
     <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-remove" onclick="deleteuser()" plain="true">删除</a>
     <div>
      <span>用户名:</span>
      <input id="userName" class="easyui-textbox" type="text" data-options="required:false"/>
      <span>用户id:</span>
      <input id="userId" class="easyui-textbox" type="number" data-options="required:false"/>
      <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-search" plain="true" onclick="searchUser()">搜索</a>
     </div>
    </div>
  </c:otherwise>  
</c:choose>