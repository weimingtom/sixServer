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
    <!--信息列表-->
    <table id="infoTable" title="信息列表" class="easyui-datagrid" style="width: 100%; height: auto"
           url="infoJsonList.man" toolbar="#infoToolbar" pagination="true" striped="true"
           data-options="onDblClickRow:editInfo" idField="id" loadMsg="玩命加载中" pageSize="20" pageList="[10,15,20,25,30,35,40,45,50]"
           fitcolumns="true" singleselect="true" showFooter="true">
      <thead>
        <tr>
          <th field="id" width="50" sortable="true">
            id
          </th>
          <th field="title" width="50">
            标题
          </th>
          <th field="type" width="50">
            类型
          </th>
          <th field="level" width="50">
            信息等级
          </th>
          <th field="note" width="50">
            简介
          </th>
          <th field="addr" width="50">
            地址
          </th>        
          <th field="time" width="50">
            时间
          </th>
        </tr>
      </thead>
    </table>
    <!--工具条-->
    <div id="infoToolbar"> 
      <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-add" onclick="addInfo()" plain="true">添加</a>
      <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-edit" onclick="editInfo()" plain="true">修改</a>
      <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-remove" onclick="deleteInfo()" plain="true">删除</a>
    </div>
  </c:otherwise>  
</c:choose>