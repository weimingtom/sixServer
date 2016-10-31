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
    <div class="easyui-layout" fit='false' style="width: 100%;height: 600px">
      <div data-options="region:'west',split:true,collapsible:true,border:false,title:'本 地 脚 本'" style="width:30%;">
        <ul id="localScriptTree" class="easyui-tree" data-options="
            url: 'localScriptTree.man',
            lines: true,
            method: 'post',
            animate: true,
            onDblClick:function(){editLocalScriptFile(true)},
            onContextMenu: function(e,node){
            e.preventDefault();
            $(this).tree('select',node.target);
            $('#localTreeMenu').menu('show',{
            left: e.pageX,
            top: e.pageY
            });
            },
            formatter:function(node)
            {
            var s = node.text;
            if (!node.file)
            {
            s += ' <span style=\'color:blue\'>(' + node.children.length + ')</span>';
            }
            return s;
            }
            ">
        </ul>
      </div>
      <div data-options="region:'center',border:false">
        <!--内容展示-->
        <div id="localScriptEditPanel" class="easyui-panel" title="编辑" style="width: 100%">
          <form id="localScriptForm" class="easyui-form" 
                method="post" action="editLocalScript.man" 
                data-options="novalidate:true">
            <input class="easyui-textbox" 
                   id="localScriptContent"
                   name="localScriptContent"
                   type="hidden"
                   />
            <input id="localScriptPath" name="localScriptPath" type="hidden"/><!--脚本文件路径-->
          </form>
          <div style="text-align:center;padding:5px">
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitLocalScriptForm()">保存</a>
          </div>
        </div>
      </div>
    </div>
  </c:otherwise>  
</c:choose>