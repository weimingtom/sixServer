<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!--错误信息-->
<script>
  if (typeof $!=='undefined')
    $.messager.alert('错误', '鉴权失败', 'error');
</script>