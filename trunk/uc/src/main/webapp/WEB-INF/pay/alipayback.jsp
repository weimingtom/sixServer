<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="renderer" content="webkit"/>
    <title>充值中心::宅乐科技::Nookjoy::北京宅乐科技有限公司官网::</title>
    <meta name="keywords" content="宅乐科技,nookjoy,宅乐科技充值中心,充值中心" />
    <meta name="description" content="北京宅乐科技有限公司(nookjoy)是一家专注于手机游戏研发的创新型企业，致力于为广大手机用户提供最佳游戏体验。公司拥有独立游戏的制作团队和轻松向上的工作氛围，得到创新工场和美国中经合集团的早期投资。" />
    <link href="favicon.ico?ver=0.123" type="image/x-icon" rel="shortcut icon bookmark"/>
    <link rel="stylesheet" type="text/css" href="pay/CSS/Index.css"/>
    <!--[if gte IE 9]><!-->
    <meta name="application-name" content="宅乐科技"/>
    <meta name="msapplication-tooltip" content="宅乐科技"/>
    <meta name="msapplication-window" content="width=1200;height=900"/>
    <meta name="msapplication-starturl" content="/"/>
    <!--<![endif]-->
    <script type="text/javascript" src="pay/JS/Jquery.js"></script>
  </head>
  <body>
    <div id="container">
      <div id="header" class="relative">
        <div class="header-nav ">
          <div class="layout clearfix">
            <a href="http://www.nookjoy.com/" class="float-left header-logo-xd"></a>

            <div class="nav-list float-left">
              <a href="index.pay" class="size-big btn btn-link  "
                 style="width: 110px;">充值</a> <span class="pipe">|</span> <a
                 href="http://www.nookjoy.com/" class="size-big btn btn-link  "
                 style="width: 50px;">首页</a>
            </div>
          </div>
        </div>
      </div>
      <div id="main" class="">
        <div class="layout clearfix">
          <div class="main-content float-left">
            <div class="main-content-header clearfix">
              <ul class="clearfix order-step float-left">
                <li><a class="btn-block first text-low" href="index.pay">1、选择游戏</a></li>
                <li><span class="btn-block text-low">2、确认信息</span></li>
                <li><span class="btn-block chosen">3、充值完成</span></li>
              </ul>
            </div>
            <div class="main-content-body clearfix">
              <div class="block text-center">
                <div class="well well-danger well-inline">
                  <p class="text-danger text-left">
                    <c:choose>
                      <c:when test="${status eq 1 }">
                        恭喜您，充值成功！<a href="index.pay?gameid=<c:out value="${gameid}"/>" class="btn btn-danger btn-block-narrow">继续充值</a>
                      </c:when>
                      <c:otherwise>
                        您的充值尚未成功，请及时付款！<a href="index.pay?gameid=<c:out value="${gameid}"/>"  class="btn btn-danger btn-block-narrow">重新充值</a>
                      </c:otherwise>
                    </c:choose>
                  </p>
                </div>
              </div>
              <div class="block-sub">
                <p>1、网上银行已经扣款，但是游戏余额没有增加。是因为银行数据正在传输，请您耐心等待。</p>
                <p>2、如果您的浏览器打不开网上银行，建议您使用IE浏览器再次尝试。</p>
                <p>3、出现安全提示不能充值，建议你点击"系统面板-Internet选项-安全"，将各项设置恢复到默认。</p>
              </div>
              <div class="block-sub clearfix">
              </div>
            </div>
            <!-- END OF create form -->
          </div>
        </div>
        <div class="push">
          <!-- not put anything here -->
        </div>
      </div>
    </div>
    <div id="footer" class="full">
      <div class="footer-info size-small">
        <p class="text-center">
          </br>Copyright ©2015 Nookjoy. All Rights Reserved</br> 北京宅乐科技有限公司 版权所有</br> 备案号
          京ICP备15001937号</br>
        </p>
      </div>
    </div>
  </body>
</html>