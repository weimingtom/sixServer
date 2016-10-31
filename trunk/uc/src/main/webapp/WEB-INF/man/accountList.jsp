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
    <!--账号列表-->
    <table id="accountTable" title="账号" class="easyui-datagrid" style="width: 100%; height: auto"
           url="accountJsonList.man" toolbar="#accountTool" pagination="true" striped="true"
           data-options="onDblClickRow:editAccount" loadMsg="玩命加载中" pageSize="20" pageList="[10,15,20,25,30,35,40,45,50]"
           fitcolumns="true" singleselect="true">
     <thead>
      <tr>
       <th field="email" width="50">
        邮箱
       </th>
       <th field="pwd" width="50">
        密码
       </th>
       <th field="roleName" width="50">
        权限
       </th>
      </tr>
     </thead>
    </table>
    <!--工具条-->
    <div id="accountTool">
     <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-add" onclick="addAccount()" plain="true">添加</a> 
     <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-edit" onclick="editAccount()" plain="true">修改</a> 
     <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-remove" onclick="deleteAccount()" plain="true">删除</a>
    </div>
  </c:otherwise>  
</c:choose>