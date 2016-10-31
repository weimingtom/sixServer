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
    <!--服务器列表-->
    <table id="serverTable" title="服务器" class="easyui-datagrid" style="width: 100%; height: auto"
           url="serverJsonList.man" toolbar="#serverTool" pagination="true" striped="true"
           data-options="onDblClickRow:editserver" loadMsg="玩命加载中" pageSize="20" pageList="[10,15,20,25,30,35,40,45,50]"
           fitcolumns="true" singleselect="true">
      <thead>
        <tr>
          <th field="serverId" width="50">
            id
          </th>
          <th field="serverName" width="50">
            服务器名
          </th>
          <th field="serverAddr" width="50">
            地址
          </th>
          <th field="riverPort" width="50">
            river端口
          </th>
          <th field="ts" width="50">
            渠道
          </th>
          <th field="serverStatusString" width="50">
            状态
          </th>
          <!--
          <th field="innerIp" width="50">
            内网地址
          </th>
          <th field="innerPort" width="50">
            内网端口
          </th>
          -->
          <th field="serverDesc" width="50">
            描述
          </th> 
          <th field="lastRelease" width="50">
            最近提交
          </th>
          <th field="lastVersion" width="50">
            最新版本
          </th>
        </tr>
      </thead>
    </table>
    <!--工具条-->
    <div id="serverTool">
      <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-add" onclick="addserver()" plain="true">添加</a> 
      <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-edit" onclick="editserver()" plain="true">修改</a> 
      <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-remove" onclick="deleteserver()" plain="true">删除</a>
      <c:if test="${role eq null||role.ops eq null||role.ops.isEmpty()}">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-reload" onclick="configserverDlg()" plain="true">配置</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-reload" onclick="updateserverDlg()" plain="true">更新</a>
      </c:if>
    </div>
  </c:otherwise>  
</c:choose>