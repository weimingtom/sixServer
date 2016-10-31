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
    <!--脏词列表-->
    <table id="dirtyTable" title="脏词" class="easyui-datagrid" style="width: 100%; height: auto"
           url="dirtyJsonList.man" toolbar="#dirtyTool" pagination="true" striped="true"
           data-options="onDblClickRow:editDirty" loadMsg="玩命加载中" pageSize="20" pageList="[10,15,20,25,30,35,40,45,50]"
           fitcolumns="true" singleselect="true">
     <thead>
      <tr>
       <th field="id" width="50">
        id
       </th>
       <th field="word" width="50">
        脏词
       </th>
       <th field="note" width="50">
        描述
       </th>
      </tr>
     </thead>
    </table>
    <!--工具条-->
    <div id="dirtyTool">
     <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-add" onclick="addDirty()" plain="true">添加</a> 
     <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-edit" onclick="editDirty()" plain="true">修改</a> 
     <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-remove" onclick="deleteDirty()" plain="true">删除</a>
    </div>
  </c:otherwise>  
</c:choose>