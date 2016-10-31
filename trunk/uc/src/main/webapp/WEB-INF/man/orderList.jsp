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
    <!--订单列表-->
    <table id="orderTable" title="订单列表" class="easyui-datagrid" style="width: 100%; height: auto"
           url="orderJsonList.man" toolbar="#orderToolbar" pagination="true" striped="true"
           data-options="onDblClickRow:overOrder" idField="id" loadMsg="玩命加载中" pageSize="20" pageList="[10,15,20,25,30,35,40,45,50]"
           fitcolumns="true" singleselect="true" showFooter="true">
     <thead>
      <tr>
       <th field="id" width="50" sortable="true">
        id
       </th>
       <th field="oid" width="50">
        订单号
       </th>
       <th field="anyOid" width="50">
        any订单号
       </th>
       <th field="serverName" width="50">
        服务器
       </th>
       <th field="userName" width="50">
        用户
       </th>
       <th field="price" width="50" sortable="true">
        价格
       </th>
       <th field="statusString" width="50" sortable="true">
        状态
       </th>
       <th field="chargeType" width="50" sortable="true">
        订单类型
       </th>
       <th field="anyChannel" width="50" sortable="true">
        渠道
       </th>
       <th field="ctime" width="50" sortable="true">
        创建时间
       </th>
       <th field="ftime" width="50" sortable="true">
        到账时间
       </th>
       <th field="manual" width="50" sortable="true">
        手动完成
       </th>
      </tr>
     </thead>
    </table>
    <!--工具条-->
    <div id="orderToolbar"> 
     <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-ok" onclick="overOrder()" plain="true">完成</a>
     <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-remove" onclick="deleteOrder()" plain="true">删除</a>
     <div>
      <span>服务器:</span>
      <input id="sid" class="easyui-textbox" type="number" data-options="required:false"/>
      <span>用户名:</span>
      <input id="uName" class="easyui-textbox" type="text" data-options="required:false"/>
      <span>用户id:</span>
      <input id="uId" class="easyui-textbox" type="number" data-options="required:false"/>
      <span>开始时间:</span>
      <input id="orderStartTime" class="easyui-datetimebox"  data-options="required:false"/>
      <span>结束时间:</span>
      <input id="orderEndTime" class="easyui-datetimebox"  data-options="required:false"/>

      <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-search" plain="true" onclick="searchOrder()">搜索</a>
     </div>
    </div>
  </c:otherwise>  
</c:choose>