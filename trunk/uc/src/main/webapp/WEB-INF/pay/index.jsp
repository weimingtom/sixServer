<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="renderer" content="webkit">
    <title>充值中心::宅乐科技::Nookjoy::北京宅乐科技有限公司官网::</title>
    <meta name="keywords" content="宅乐科技,nookjoy,宅乐科技充值中心,充值中心" >
    <meta name="description" content="北京宅乐科技有限公司(nookjoy)是一家专注于手机游戏研发的创新型企业，致力于为广大手机用户提供最佳游戏体验。公司拥有独立游戏的制作团队和轻松向上的工作氛围，得到创新工场和美国中经合集团的早期投资。" >
    <link href="favicon.ico?ver=0.123" type="image/x-icon" rel="shortcut icon bookmark">
    <link rel="shortcut icon" href="pay/favicon_pc.ico" type="image/x-icon" >
    <link rel="stylesheet" type="text/css" href="pay/CSS/Index.css">
    <!--[if gte IE 9]><!-->
    <meta name="application-name" content="宅乐科技">
    <meta name="msapplication-tooltip" content="宅乐科技">
    <meta name="msapplication-window" content="width=1200;height=900">
    <meta name="msapplication-starturl" content="/">
    <!--<![endif]-->
    <script type="text/javascript" src="pay/JS/Jquery.js"></script>
    <link rel="stylesheet" type="text/css" href="pay/CSS/ChooseGame.css">
  </head>

  <body>
    <div id="container">
      <div id="header" class="relative">
        <div class="header-nav ">
          <div class="layout clearfix">
            <a href="http://www.nookjoy.com/" class="float-left header-logo-xd"></a>

            <div class="nav-list float-left">
              <a href="javascript:" class="size-big btn btn-link  " style="width: 110px;">充值</a>
              <span class="pipe">|</span>
              <a href="http://www.nookjoy.com/" class="size-big btn btn-link  " style="width: 50px;">首页</a>
            </div>
          </div>
        </div>
      </div>
      <div id="main" class="">
        <div class="layout clearfix">
          <div class="main-content float-left">
            <div class="main-content-header clearfix">
              <ul class="clearfix order-step float-left">
                <li><a class="btn-block first  chosen" href="javascript:">1、选择游戏</a></li>
                <li><span class="btn-block text-low">2、确认信息</span></li>
                <li><span class="btn-block text-low">3、充值完成</span></li>
              </ul>
            </div>
            <div class="main-content-body clearfix">
              <div class="block">
                <ul class="clearfix order-app">
                  <!--
                  <li><a id="hyzgOrder" href="selectGame.pay?game=3" class="btn-block"><span>海妖之歌</span></a></li>
                  -->
                  <li><a id="glgsOrder" href="http://uc.nookjoy.com/usercenter/payindex.nj?gameid=2" class="btn-block"><span>大猩猩灌篮</span></a></li>
                  <li><a id="ttmxOrder" href="http://uc.nookjoy.com/usercenter/payindex.nj?gameid=1" class="btn-block"><span>天天冒险</span></a></li>
                </ul>
              </div>
            </div>
            <!-- END OF create form -->
          </div>
        </div>
        <div class="push"><!-- not put anything here --></div>
      </div>
    </div>

    <div id="footer" class="full">

      <div class="footer-info size-small">
        <p class="text-center">
          </br>Copyright ©2015 Nookjoy. All Rights Reserved</br>
          北京宅乐科技有限公司 版权所有</br>
          备案号 京ICP备15001937号</br>
        </p>
      </div>
    </div>
  </body>
</html>