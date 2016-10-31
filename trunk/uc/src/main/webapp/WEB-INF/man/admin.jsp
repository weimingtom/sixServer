<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>ocean-管理</title>  
    <link rel="stylesheet" href="css/common.css" />
    <link rel="stylesheet" href="css/codemirror.css">
    <link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="css/themes/gray/easyui.css">
    <link rel="stylesheet" type="text/css" href="css/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="css/highlight/default.css"><!--高亮-->
    <script type="text/javascript" src="js/jquery.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="js/jquery.easyui.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="js/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="js/game.js" charset="utf-8"></script>
    <script type="text/javascript" src="js/server.js" charset="utf-8"></script>
    <script type="text/javascript" src="js/user.js" charset="utf-8"></script>
    <script type="text/javascript" src="js/channel.js" charset="utf-8"></script>
    <script type="text/javascript" src="js/script.js" charset="utf-8"></script>
    <script type="text/javascript" src="js/localScript.js" charset="utf-8"></script>
    <script type="text/javascript" src="js/auth.js" charset="utf-8"></script>
    <script type="text/javascript" src="js/account.js" charset="utf-8"></script>
    <script type="text/javascript" src="js/client.js" charset="utf-8"></script>
    <script type="text/javascript" src="js/dirty.js" charset="utf-8"></script>
    <script type="text/javascript" src="js/order.js" charset="utf-8"></script>
    <script type="text/javascript" src="js/note.js" charset="utf-8"></script>

    <script type="text/javascript" src="js/script/codemirror.js"></script>
    <script type="text/javascript" src="js/script/matchbrackets.js"></script>
    <script type="text/javascript" src="js/script/groovy.js"></script>

    <script type="text/javascript" charset="utf-8">
    
//    jQuery(function($){
//        var _ajax=$.ajax;  
//        $.ajax=function(opt){
//        	var _success =opt.success;  
//            var _opt = $.extend(opt, {
//                success:function(data, textStatus){
//                    if(data.toString().indexOf('<html lang="zh-cn">') != -1) {
//                      
//                        window.document=data;
//                        return;
//                    }  
//                    _succxess(data, textStatus);    
//                }    
//            });  
//           return _ajax(_opt);  
//        };  
//    });  
    
    
      jQuery.ajaxSetup({cache: false});//ajax不缓存
      function setmain(href) {
        $('#center').panel('refresh', href);
      }
      function addTab(title, url)//添加tab
      {

        //  setmain(url);

        if ($('#tts').tabs('exists', title))
        {
          $('#tts').tabs('select', title);
          var tab = $('#tts').tabs('getSelected');
          tab.panel('refresh');
        } else {
          $('#tts').tabs('add', {
            title: title,
            href: url,
            closable: true,
            collapsible: true,
            onBeforeRender:function(data){
            	
            }
          });
        }
      }
      var editor;
      function showContent(id)
      {
        $('.CodeMirror').remove();
        editor = CodeMirror.fromTextArea(document.getElementById(id),
                {
                  lineNumbers: true,
                  matchBrackets: true,
                  mode: "text/x-groovy"
                });
      }
    </script>
  </head>
  <body class="easyui-layout">
    <div data-options="region:'north',border:false" style="height:60px;padding:5px">
      <%@include file="header.jsp"%>
    </div>
    <div data-options="region:'west',border:false,split:false,title:'管 理'" style="width:20%;">
      <div class="easyui-accordion" data-options="multiple:true" fit="false" border="false">
        <%@include file="menu.jsp"%>
      </div>
    </div>
    <div data-options="region:'south',border:false" style="height:90px;padding:10px;">
      <%@include file="footer.jsp"%>
    </div>
    <!--中间部分，会被替换掉-->
    <div id="center" data-options="region:'center',border:true">
      <div id="tts" class="easyui-tabs" style="width: 100%">
        <div title="主页君" href="welcome.html" closable="true" collapsible="true">
        </div>
      </div>
    </div>

    <!--各种弹出层,对话框-->
    <%@include file="dialog/channel.jsp"%>
    <%@include file="dialog/game.jsp"%>
    <%@include file="dialog/script.jsp"%>
    <%@include file="dialog/localScript.jsp"%>
    <%@include file="dialog/server.jsp"%>
    <%@include file="dialog/user.jsp"%>
    <%@include file="dialog/role.jsp"%>
    <%@include file="dialog/account.jsp"%>
    <%@include file="dialog/relogin.jsp"%>
    <%@include file="dialog/client.jsp"%>
    <%@include file="dialog/dirty.jsp"%>
    <%@include file="dialog/order.jsp"%>
    <%@include file="dialog/note.jsp"%>
  </body>
</html>
