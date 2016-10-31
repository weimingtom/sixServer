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
    <!--公告列表-->
    <table id="noteTable" title="公告" class="easyui-datagrid" style="width: 100%; height: auto"
           url="noteJsonList.man" toolbar="#noteTool" pagination="true" striped="true"
           data-options="onDblClickRow:editnote" loadMsg="玩命加载中" pageSize="20" pageList="[10,15,20,25,30,35,40,45,50]"
           fitcolumns="true" singleselect="true">
      <thead>
        <tr>
          <th field="game" width="50">
            游戏
          </th>
          <th field="id" width="50">
            id
          </th>
          <th field="title" width="50">
            标题
          </th>
          <th field="content" width="50">
            内容
          </th>
          <th field="ctime" width="50">
            开始时间
          </th>
          <th field="ftime" width="50">
            结束时间
          </th>
          <th field="statusString" width="50">
            状态
          </th>
          <th field="tags" width="50">
            渠道
          </th>
        </tr>
      </thead>
    </table>
    <!--工具条-->
    <div id="noteTool">
      <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-add" onclick="addnote()" plain="true">添加</a> 
      <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-edit" onclick="editnote()" plain="true">修改</a> 
      <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-remove" onclick="deletenote()" plain="true">删除</a>
    </div>
  </c:otherwise>  
</c:choose>