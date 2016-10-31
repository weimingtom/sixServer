<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${role eq null||role.ops eq null||role.ops.isEmpty()||role.ops.contains('gameList.man')}">  
  <div title="游戏管理" data-options="selected:true" style="padding: 5px;">
    <ul>
      <li>
        <p>
          <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add'" style="width:60%" onclick="addGame()">添加游戏</a>
        </p>
      </li>
      <li>
        <p>
          <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" style="width:60%" onclick="addTab('游戏管理', 'gameList.man')">查看游戏列表</a>
        </p>
      </li>
    </ul>
  </div>
</c:if>
<c:if test="${role eq null||role.ops eq null||role.ops.isEmpty()||role.ops.contains('serverList.man')}">  
  <div title="服务器管理" data-options="selected:false" style="padding: 5px;">
    <ul>
      <li>
        <p>
          <input id="curGame"  class="easyui-combobox" name="curGame" style="width: 60%" data-options="
                 url: 'gameJsonBox.man',
                 method: 'get',
                 valueField: 'gameId',
                 textField: 'gameName',
                 panelHeight: 'auto',
                 formatter: formatGameBox,
                 filter: function(q, row){
                 var opts = $(this).combobox('options');
                 return row[opts.textField].indexOf(q) == 0;
                 },
                 hasDownArrow:true,
                 editable:true,
                 onSelect:selectGame
                 "/>
          <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#curGame').combobox('reload', 'gameJsonBox.man')">刷新</a>
        </p>
      </li>
      <li>
        <p>
          <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'" style="width:60%" onclick="addserver()">添加服务器</a>
        </p>  
      </li>
      <li>
        <p>
          <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" style="width:60%" onclick="serverList()">查看服务器列表</a>
        </p>
      </li>
    </ul>
  </div>
</c:if>
<c:if test="${role eq null||role.ops eq null||role.ops.isEmpty()||role.ops.contains('userList.man')}">  
  <div title="玩家管理">
    <ul>
      <li>
        <p>
          <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" style="width:60%" onclick="addTab('玩家管理', 'userList.man')">查看玩家列表</a>
        </p>
      </li>
    </ul>
  </div>
</c:if>
<c:if test="${role eq null||role.ops eq null||role.ops.isEmpty()||role.ops.contains('orderList.man')}">  
  <div title="订单管理">
    <ul>
      <li>
        <p>
          <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" style="width:60%" onclick="addTab('订单管理', 'orderList.man')">查看订单列表</a>
        </p>
      </li>
    </ul>
  </div>
</c:if>
<c:if test="${role eq null||role.ops eq null||role.ops.isEmpty()||role.ops.contains('noteList.man')}">  
  <div title="公告管理">
    <ul>
      <li>
        <p>
          <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" style="width:60%" onclick="addTab('公告管理', 'noteList.man')">查看公告列表</a>
        </p>
      </li>
    </ul>
  </div>
</c:if>
<c:if test="${role eq null||role.ops eq null||role.ops.isEmpty()||role.ops.contains('channelList.man')}">  
  <div title="渠道管理">
    <ul>
      <li>
        <p>
          <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" style="width:60%" onclick="addTab('渠道管理', 'channelList.man')">查看渠道列表</a>
        </p>
      </li>
    </ul>
  </div>
</c:if>
<c:if test="${role eq null||role.ops eq null||role.ops.isEmpty()||role.ops.contains('scriptList.man')}">  
  <div title="脚本管理">
    <ul>
      <li>
        <p>
          <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" style="width:60%" onclick="addTab('脚本管理', 'scriptList.man')">查看脚本列表</a>
        </p>
      </li>
      <li>
        <p>
        <form id="uploadScriptForm"  method="post" enctype="multipart/form-data">  
          <input id="scriptFileBox" name="scriptFileContent" class="easyui-filebox"  style="width:60%" data-options="buttonText: '文件',prompt:'选择脚本文件压缩包...'" />
          <a href="javascript:void(0)" class="easyui-linkbutton" onclick="uploadScript()">上传</a>
        </form>
        </p>
      </li>
    </ul>
  </div>
</c:if>
<c:if test="${role eq null||role.ops eq null||role.ops.isEmpty()||role.ops.contains('clientList.man')}">  
  <div title="版本管理">
    <ul>
      <li>
        <p>
          <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" style="width:60%" onclick="addTab('版本管理', 'clientList.man')">查看版本列表</a>
        </p>
      </li>
    </ul>
  </div>
</c:if>
<c:if test="${role eq null||role.ops eq null||role.ops.isEmpty()||role.ops.contains('dirtyList.man')}">  
  <div title="脏词管理">
    <ul>
      <li>
        <p>
          <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" style="width:60%" onclick="addTab('脏词管理', 'dirtyList.man')">查看脏词列表</a>
        </p>
      </li>
    </ul>
  </div>
</c:if>
<!--官网-->
<%--<c:if test="${role eq null||role.ops eq null||role.ops.isEmpty()||role.ops.contains('siteInfoList.man')}">  
  <div title="官网管理">
    <ul>
      <li>
        <p>
          <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" style="width:60%" onclick="addTab('新闻公告', 'siteInfoList.man?type=1')">新闻公告列表</a>
        </p>
      </li>
      <li>
        <p>
          <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" style="width:60%" onclick="addTab('新手指南', 'siteInfoList.man?type=2')">新手指南列表</a>
        </p>
      </li>
      <li>
        <p>
          <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" style="width:60%" onclick="addTab('游戏资料', 'siteInfoList.man?type=3')">游戏资料列表</a>
        </p>
      </li>
    </ul>
  </div>
</c:if>--%>

<c:if test="${role eq null||role.ops eq null||role.ops.isEmpty()||role.ops.contains('authList.man')}">  
  <div title="权限管理">
    <ul>
      <li>
        <p>
          <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" style="width:60%" onclick="addTab('权限管理', 'authList.man')">查看角色列表</a>
        </p>
      </li>
    </ul>
  </div>
</c:if>
<c:if test="${role eq null||role.ops eq null||role.ops.isEmpty()||role.ops.contains('accountList.man')}">  
  <div title="账号管理">
    <ul>
      <li>
        <p>
          <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" style="width:60%" onclick="addTab('账号管理', 'accountList.man')">查看账号列表</a>
        </p>
      </li>
    </ul>
  </div>
</c:if>
<c:if test="${role eq null||role.ops eq null||role.ops.isEmpty()||role.ops.contains('localScriptList.man')}">  
  <div title="本地脚本">
    <ul>
      <li>
        <p>
          <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" style="width:60%" onclick="addTab('本地脚本', 'localScriptList.man')">查看本地脚本列表</a>
        </p>
      </li>
      <li>
        <p>
        <form id="uploadLocalScriptForm"  method="post" enctype="multipart/form-data">  
          <input id="localScriptFileBox" name="localScriptFileContent" class="easyui-filebox"  style="width:60%" data-options="buttonText: '文件',prompt:'选择脚本文件压缩包...'" />
          <a href="javascript:void(0)" class="easyui-linkbutton" onclick="uploadLocalScript()">上传</a>
        </form>
        </p>
      </li>
    </ul>
  </div>
</c:if>

