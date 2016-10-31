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
    <!--游戏列表-->
    <table id="gameTable" title="游戏" class="easyui-datagrid" style="width: 100%; height: auto"
           url="gameJsonList.man" toolbar="#gameToolbar" pagination="true" striped="true"
           data-options="onDblClickRow:editGame" loadMsg="玩命加载中" pageSize="20" pageList="[10,15,20,25,30,35,40,45,50]"
           fitcolumns="true" singleselect="true">
     <thead>
      <tr>
       <th field="gameId" width="50">
        id
       </th>
       <th field="gameName" width="50">
        游戏名
       </th>
       <th field="gameBid" width="50">
        bid
       </th>
       <th field="gameStatusString" width="50">
        状态
       </th>
       <th field="desKey" width="50">
        秘钥
       </th>
       <th field="anyPrivateKey" width="50">
        any私有key
       </th>
       <th field="gameDec" width="50">
        描述
       </th>
      </tr>
     </thead>
    </table>
    <!--工具条-->
    <div id="gameToolbar">
     <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-add" onclick="addGame()" plain="true">添加</a> 
     <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-edit" onclick="editGame()" plain="true">修改</a> 
     <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-remove" onclick="deleteGame()" plain="true">删除</a>
    </div>
  </c:otherwise>  
</c:choose>