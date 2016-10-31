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
    <!--版本列表-->
    <table id="clientTable" title="版本列表" class="easyui-datagrid" style="width: 100%; height: auto"
           url="clientJsonList.man" toolbar="#clientTool" pagination="true" striped="true"
           data-options="onDblClickRow:editClient" loadMsg="玩命加载中" pageSize="20" pageList="[10,15,20,25,30,35,40,45,50]"
           fitcolumns="true" singleselect="true">
     <thead>
      <tr>
       <th field="game" width="50">
        游戏
       </th>
       <th field="version" width="50">
        版本
       </th>
       <th field="updateTime" width="50">
        更新时间
       </th>
       <th field="address" width="50">
        更新地址
       </th>
       <th field="forceUpdate" width="50">
        强制更新
       </th>
       <th field="open" width="50">
        开启
       </th>
       <th field="channels" width="50">
        渠道
       </th>
      </tr>
     </thead>
    </table>
    <!--工具条-->
    <div id="clientTool">
     <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-add" onclick="addClient()" plain="true">添加</a> 
     <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-edit" onclick="editClient()" plain="true">修改</a> 
     <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-remove" onclick="deleteClient()" plain="true">删除</a>
    </div>
  </c:otherwise>  
</c:choose>