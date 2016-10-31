<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!--local tree menu-->
<div id="localTreeMenu" class="easyui-menu" style="width:140px;">
  <div onclick="addLocalScriptFile()" data-options="iconCls:'icon-add'">添加</div>
  <div onclick="deleteLocalScriptFile()" data-options="iconCls:'icon-remove'">删除</div>
  <div onclick="renameLocalScriptFile()" data-options="iconCls:'icon-edit'">重命名</div>
  <div onclick="editLocalScriptFile(false)" data-options="iconCls:'icon-edit'">编辑</div>
  <div class="menu-sep"></div>
  <div onclick="runLocalScriptFile()" data-options="iconCls:'icon-reload'">运行脚本</div>
</div>