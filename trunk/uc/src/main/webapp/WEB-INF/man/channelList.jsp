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
    <!--渠道列表-->
    <table id="channelTable" title="渠道" class="easyui-datagrid" style="width: 100%; height: auto"
           url="channelJsonList.man" toolbar="#channelToolbar" pagination="true" striped="true"
           data-options="onDblClickRow:editchannel" loadMsg="玩命加载中" pageSize="20" pageList="[10,15,20,25,30,35,40,45,50]"
           fitcolumns="true" singleselect="true">
      <thead>
        <tr>
          <th field="cid" width="50">
            id
          </th>
          <th field="name" width="50">
            名字
          </th>
          <th field="tag" width="50">
            分类
          </th>  
          <th field="bid" width="50">
            bid
          </th>
          <th field="note" width="50">
            描述
          </th>
        </tr>
      </thead>
    </table>
    <!--工具条-->
    <div id="channelToolbar">
      <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-add" onclick="addchannel()" plain="true">添加</a> 
      <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-edit" onclick="editchannel()" plain="true">修改</a> 
      <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-remove" onclick="deletechannel()" plain="true">删除</a>
    </div>
  </c:otherwise>  
</c:choose>