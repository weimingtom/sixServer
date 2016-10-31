<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>  
<html>  
 <head>  
  <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />  
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />  
  <title>Hello, World</title>  
  <style type="text/css">  
   html{height:100%}  
   body{height:100%;margin:0px;padding:0px}  
   #container{height:100%}  
  </style>  
  <script type="text/javascript" src="http://api.map.baidu.com/api?v=1.5&ak=ARXrqdGSOy34yEvuzIDPd2Yi">
    //v1.5版本的引用方式：src="http://api.map.baidu.com/api?v=1.5&ak=您的密钥"
    //v1.4版本及以前版本的引用方式：src="http://api.map.baidu.com/api?v=1.4&key=您的密钥&callback=initialize"
  </script>
 </head>  

 <body>  
  <div id="container"></div> 
  <script type="text/javascript">
    var map = new BMap.Map("container");          // 创建地图实例  
    map.addControl(new BMap.NavigationControl());
    map.addControl(new BMap.ScaleControl());
    map.addControl(new BMap.OverviewMapControl());
    map.addControl(new BMap.MapTypeControl());
    map.setCurrentCity("北京"); // 仅当设置城市信息时，MapTypeControl的切换功能才能可用
    var point = new BMap.Point(116.404, 39.915);  // 创建点坐标  
    map.centerAndZoom(new BMap.Point(116.404, 39.915), 14);
    var driving = new BMap.DrivingRoute(map, {
      renderOptions: {
        map: map,
        panel: "results",
        autoViewport: true
      }
    });
    driving.search("中关村", "天安门");

  </script>  
 </body>  
</html>