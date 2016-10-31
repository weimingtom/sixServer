<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/taglibs.jsp"%>
 <!DOCTYPE HTML>
<html >
<head xmlns="http://www.w3.org/1999/xhtml" dir="ltr" lang="zh-CN"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<meta name="renderer" content="webkit"/>
<title>充值中心::宅乐科技::Nookjoy::北京宅乐科技有限公司官网::</title>
<meta name="keywords" content="宅乐科技,nookjoy,宅乐科技充值中心,充值中心" />
<meta name="description"
	content="北京宅乐科技有限公司(nookjoy)是一家专注于手机游戏研发的创新型企业，致力于为广大手机用户提供最佳游戏体验。公司拥有独立游戏的制作团队和轻松向上的工作氛围，得到创新工场和美国中经合集团的早期投资。" />
<link href="favicon.ico?ver=0.123" type="image/x-icon"
	rel="shortcut icon bookmark"/>
<link rel="stylesheet" type="text/css" href="pay/CSS/Index.css"/>
<link rel="stylesheet" type="text/css" href="pay/CSS/Pay.css"/>
<link rel="stylesheet"
	href="pay/Libs/fancyBox/source/jquery.fancybox.css?v=2.1.5"
	type="text/css" media="screen" />
<!--[if gte IE 9]><!-->
<meta name="application-name" content="宅乐科技"/>
<meta name="msapplication-tooltip" content="宅乐科技"/>
<meta name="msapplication-window" content="width=1200;height=900"/>
<meta name="msapplication-starturl" content="/"/>
<!--<![endif]-->
<script type="text/javascript" src="pay/JS/Jquery.js"></script>
    <script type="text/javascript" src="pay/JS/Lib.js"></script>
    <script
    src="//netdna.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
    <script type="text/javascript"
    src="pay/Libs/fancyBox/lib/jquery.mousewheel-3.0.6.pack.js"></script>
    <script type="text/javascript"
    src="pay/Libs/fancyBox/source/jquery.fancybox.pack.js?v=2.1.5"></script>
    <script type="text/javascript" src="pay/JS/Index2.js"></script>
    <script type="text/javascript" src="pay/JS/jquery.qrcode.js"></script>
    <script type="text/javascript" src="pay/JS/qrcode.js"></script>
</head>
<c:if test="${error!=null}">
	<script>
		alert('<c:out value="${error }" />');
	</script>
</c:if>
<body>
    <div id="data" style="display:none;">
        <input type="hidden" id="pServerid" value="${serverid}" />
        <input type="hidden"  id="pUserCode" value="${usercode}" />
        <input type="hidden"  id="pAmount" value="${money}" />
        <input type="hidden"  id="pOrderType" value="${ordertype}" />
    </div>
	<div id="container">
		<div id="header" class="relative">
			<div class="header-nav ">
				<div class="layout clearfix">
					<a href="http://www.nookjoy.com/" class="float-left header-logo-xd"></a>

					<div class="nav-list float-left">
						<a href="payindex.nj" class="size-big btn btn-link  "
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
							<li><span class="btn-block chosen">2、确认信息</span></li>
							<li><span class="btn-block text-low">3、充值完成</span></li>
						</ul>
					</div>
					<div class="main-content-body clearfix need-login">
						<div class="float-left relative">
							<ul class="main-sidebar-menu payment-list">
								<li class="text-left relative">
									<div class="title">充值</div>
								</li>
                <li><a id="weixin" href="javascript:"
									class="payment btn-block">微信支付
										<div class="arrow">
											<div class="inside"></div>
										</div>
								</a></li>
								<li><a id="alipay" href="javascript:"
									class="payment btn-block">支付宝
										<div class="arrow">
											<div class="inside"></div>
										</div>
								</a></li>
								<li><a id="alibank" href="javascript:"
									class="payment btn-block">网银充值
										<div class="arrow">
											<div class="inside"></div>
										</div>
								</a></li>

								<li><a id="manual" href="javascript:"
									class="payment btn-block">人工汇款
										<div class="arrow">
											<div class="inside"></div>
										</div>
								</a></li>
    <li><a id="foreign" href="javascript:"
    class="payment btn-block">海外汇款
    <div class="arrow">
    <div class="inside"></div>
    </div>
    </a></li>
							</ul>
							<ul class="main-sidebar-menu">
								<li class="text-left">
									<div class="title">客服</div>
								</li>
								<li class="service-contact">
									<div class="size-small service-email">晴子QQ：2595739600</div>
									<div class="size-small service-email">邮箱：kefu@nookjoy.com</div>
								</li>
							</ul>
						</div>
						<div class="float-left">
							<form class="form text-center" method="POST"
								<%--action="alipay.nj" --%>
                 id="createForm">
								<input id="paymethod" name="paymethod" type="hidden"	value="bankPay"/> 
                <input id="defaultbank"name="defaultbank" type="hidden" value="CCB"/>
								<div class="input-container text-left">
    <div id="foreign-recharge" class="form-group hide">
    <div style="font-size: 14px; padding-bottom: 10px; color: #000;"> 請向官方銀行充值帳號匯款：</div>
    <table class="table_01">
    <thead>
    <tr class="title_tr">
    <th>匯款信息：</th>
    </tr>
    </thead>
    <tbody>
    <tr > <td>香港上海匯豐銀行有限公司<br>The Hongkong and Shanghai Banking Corporation Limited</td> </tr >
    <tr ><td>A/C Name：Huang He</td></tr>
    <tr><td>A/C No:657-314217-888</td></tr>
    <tr><td>Branch:Hong Kong Office HSBC Premier Centre, Level 5, 1 Queen's Road Central. Central, Hong Kong</td></tr>
    <tr><td>Swift Code:HSBCHKHHHKH</td></tr>
    <tr class="table_tip"> <td >
    服務說明：<br>
    1、<strong style="color:red;">匯款請提前聯系遊戲客服，務必正確告知您需要充值的遊戲帳號；</strong><br>
    2、核實完成後，請玩家立即進入遊戲查收遊戲蔽；<br>
    3、接收匯款的賬戶暫不接收公司賬戶的匯款；<br>
    4、請匯款或轉賬的時候保留單據和轉賬訂單號；<br>
    5、請各位玩家提高安全意識，非本頁公布的帳號信息均非官方帳號。<br>

    </td>
    </tr>
    </tbody>
    </table>
    </div>
									<div id="manual-recharge" class="form-group hide">
										<div
											style="font-size: 14px; padding-bottom: 10px; color: #000;">请向官方银行充值账号汇款或转账：</div>
										<table class="table_01">
											<thead>
												<tr class="title_tr">
													<th>银行</th>
													<th>账户</th>
													<th>开户行</th>
													<th>卡号</th>
												</tr>
											</thead>
											<tbody>
												<tr class="">
													<td>工商银行</td>
													<td>黄何</td>
													<td>北京苏州街支行</td>
													<td><strong>6212 2602 0006 2576 653</strong></td>
												</tr>
												<tr class="">
													<td>建设银行</td>
													<td>黄何</td>
													<td>北京北大南街支行</td>
													<td><strong>6217 0000 1004 2789 688</strong></td>
												</tr>
												<tr class="">
													<td>交通银行</td>
													<td>黄何</td>
													<td>北京海淀支行</td>
													<td><strong>6222 6209 1001 0833 766</strong></td>
												</tr>
												<tr class="">
													<td>农业银行</td>
													<td>黄何</td>
													<td>北京中关村SOHO支行</td>
													<td><strong>6228 4800 1838 5978 970</strong></td>
												</tr>
												<tr class="">
													<td>中国银行</td>
													<td>黄何</td>
													<td>北京海淀支行</td>
													<td><strong>6217 9001 0001 3534 477</strong></td>
												</tr>
												<tr class="">
													<td>招商银行</td>
													<td>黄何</td>
													<td>北京分行海淀支行</td>
													<td><strong>6214 8301 1291 5557</strong></td>
												</tr>
												<tr class="table_tip">
													<td colspan="4">特别说明：<br> 1、接收汇款的账户暂不接收公司账户的汇款。<br>
														2、每个帐号目前只接受单笔充值100元RMB以上，其他方式请按照官网充值中心操作。<br>
														3、请汇款或转账时将转账金额增加角和分，例如：充值100元，请转账100.01元到100.99元之间的金额，方便官方充值客服查询。<br>
														4、异地和跨行汇款或转账的手续费根据各银行规定由银行向用户收取。<br>
														5、请汇款或转账的时候保留单据和转账订单号。<br>
														6、海外汇款的玩家，建议先向当地银行咨询所需资料以后再联系游戏客服，确认资料正确后再进行汇款。<br>
														7、请尽量选择我们支持的银行进行汇款，不建议使用跨行汇款，如条件不允许的情况下使用跨行汇款，请务必保留您的汇款凭据以及联系客服时将您的汇款流水号告知客服便于我们查询。跨行汇款处理时间3—5个工作日。
													</td>
												</tr>
												<tr class="table_tip">
													<td colspan="4">服务说明：<br>
														1、<strong style="color:red;">汇款请提前联系游戏客服，务必正确告知您需要充值的游戏帐号，游戏帐号填写错误导致的损失请自行承担！</strong><br>
														2、核实完成后，请玩家立即进入游戏查收游戏币！<br>
														3、一旦充值成功，系统将不提供充值修正服务，如填写金额错误导致的损失我们不承担！<br>
														4、人工充值不需要您提供任何密码，请各位玩家提高安全意识，非本页公布的账号信息均非我平台官方账号。
													</td>
												</tr>
											</tbody>
										</table>
									</div>
									<div class="form-group clearfix base">
										<div class="float-left">
											<p>
												充值的游戏：<span id="appText" class="text-lead">《<c:out
														value="${game.name}" />》
												</span>
											</p>
											<input id="gameid" name="gameid" type="hidden"
												value="${game.gameId}">
										</div>
									</div>
									<div class="form-group base" id="usernameArea">
										<p class="form-label">充入账号：</p>
										<div class="form-control-inline">
											<input type="text" autocomplete="off"
												class="input-text form-control input-username" id="usercode"
												name="usercode">
										</div>
									</div>
									<div class="form-group form-group-block-sub base" id="server">
										<p class="form-label">充值的服务器：</p>
										<div class="form-control-inline">
											<select id="serverList" name="serverid" class="selectpicker form-control">
												<c:if test="${serverid  lt 1 }">
													<option value="-1">请选择服务器</option>
												</c:if>
												<c:forEach items="${serverList}" var="server">
													<option value="${server.serverId}"
														<c:if test="${server.serverId  eq serverid }">selected="selected"
							            	</c:if>>
														<c:out value="${server.name}" />
													</option>
												</c:forEach>
											</select>
											<div
												class="help-block text-danger hide float-left size-small"></div>
										</div>
									</div>
                  <div class="form-group form-group-block-sub base" id="orderTypes">
									       <p class="form-label">充值类型：</p>
									       <div class="form-control-inline">
										     <select id="orderList" name="ordertype" class="selectpicker form-control">
                                                <option value="0" selected="selected">普通充值</option>
                                                <option value="1">月卡</option>
                                                <option value="2">终身卡</option>
											   </select>
											   <div class="help-block text-danger hide float-left size-small"></div>
										     </div>
									</div>
									<div id="serverError"
										class="help-block text-danger hide  float-left size-small">没有匹配到用户名，请确认输入帐号是否正确</div>
									<div id="characterArea" class="form-group clearfix hide">
										<div class="float-left">
											<p>
												当前角色：<span id="characterName" class="text-lead"></span>
											</p>
										</div>
									</div>
									<div class="form-group amount-type form-group-block base"
										id="amountNormal" style="display: block;">
										<p class="form-label">需要充值的金额：</p>
										<div class="radio-list clearfix">
    <div class="radio float-left radio-checked">
    <input id="amount6" type="radio" name="amount" value="6">6元
    </div>
											<div class="radio float-left">
												<input id="amount10" type="radio" name="amount" value="10">10元
											</div>
											<div class="radio float-left">
												<input id="amount30" type="radio" name="amount" value="30">30元
											</div>
											<div class="radio float-left">
												<input id="amount50" type="radio" name="amount" value="50">50元
											</div>
											<div class="radio float-left ">
												<input id="amount100" type="radio" name="amount" value="100"
													checked="checked">100元
											</div>
											<div class="radio float-left">
												<input id="amount500" type="radio" name="amount" value="500">500元
											</div>
											<div class="radio float-left">
												<input id="amount1000" type="radio" name="amount" value="1000">1000元
											</div>
										</div>
									</div>
									<div class="form-group sub-payment form-group-block"
										id="chooseBank" style="display: block;">
										<p class="form-label">选择银行：</p>
										<div class="radio-list clearfix size-small">
											<div class="radio float-left  radio-checked" id="ccb">
												<input type="radio" name="bank" value="CCB" checked="checked">建设银行
											</div>
											<div class="radio float-left" id="icbc">
												<input type="radio" name="bank" value="ICBCB2C" >工商银行
											</div>
											<div class="radio float-left " id="cmbchina">
												<input type="radio" name="bank" value="CMB">招商银行
											</div>
											<div class="radio float-left " id="boco">
												<input type="radio" name="bank" value="COMM-DEBIT">交通银行
											</div>
											<div class="radio float-left " id="zgb">
												<input type="radio" name="bank" value="BOCB2C">中国银行
											</div>
											<div class="radio float-left " id="bccb">
												<input type="radio" name="bank" value="BJBANK">北京银行
											</div>
											<div class="radio float-left " id="abc">
												<input type="radio" name="bank" value="ABC">农业银行
											</div>
											<div class="radio float-left " id="cmbc">
												<input type="radio" name="bank" value="CMBC">民生银行
											</div>
											<div class="radio float-left " id="pab">
												<input type="radio" name="bank" value="SPABANK">平安银行
											</div>
											<div class="radio float-left " id="spdb">
												<input type="radio" name="bank" value="SPDB">浦发银行
											</div>
											<div class="radio float-left " id="cib">
												<input type="radio" name="bank" value="CIB">兴业银行
											</div>
											<div class="radio float-left " id="gfb">
												<input type="radio" name="bank" value="GDB">广发银行
											</div>
										</div>
									</div>
								</div>
								<a class="btn btn-block btn-primary btn-block-narrow base"
									id="submitOrder" href="javascript:">提交充值</a>
							</form>
							<form id="alipaysubmit" target="_blank" name="alipaysubmit" action="https://mapi.alipay.com/gateway.do?"  method="get">
							</form>
						</div>
						<div class="modal modal-dialog" id="ktpdTestNotice">
							<div class="modal-header clearfix">
								<div class="title">特别注意</div>
								<a href="javascript:" class="close">×</a>
							</div>
						</div>
						<div class="modal modal-dialog" id="paySuccess">
							<div class="modal-header clearfix">
								<div class="title">完成付款</div>
								<a href="javascript:" class="close">×</a>
							</div>
							<div class="modal-body">
								<p>请在新开页面中完成付款后提交。</p>
								<p class="hide tip-yikatong other-info" style="display: none;">充值有延迟到账的风险，如有疑问请联系宝付客服（021-68811008）</p>
							</div>
							<div class="modal-footer">
								<a href="javascript:" class="btn btn-default btn-cancel">返回修改</a> <a
									href="" id="order" class="btn btn-primary">已完成付款</a>
							</div>
						</div>
              <div class="modal modal-dialog" id="wppay" style="width: 250px">
							<div class="modal-header clearfix">
								<div class="title">微信扫一扫</div>
								<a href="javascript:" class="close">×</a>
							</div>
							<div class="modal-body text-align:center">
                <div>请打开微信,扫一扫下面的二维码支付</div>
						    <div id="code"></div>
              </div>
							<div class="modal-footer">
							 <a href="" id="wporder" class="btn btn-primary">我已完成付款</a>
							</div>
						</div>
						<div id="payConfirm" class="modal modal-dialog">
							<div class="modal-header clearfix">
								<div class="title text-lead">充值信息确认</div>
								<a href="javascript:" class="close">×</a>
							</div>
							<div class="modal-body">
								<p>
									您将要进行充值的账户：<span class="text-lead" id="confirmUsername"></span>
								</p>
								<p>
									充值的游戏：<span class="text-lead" id="confirmApp"></span>
								</p>
								<p>
									充值的服务器：<span class="text-lead" id="confirmAppId"></span>
								</p>
                 <p>
                  充值的类型：<span class="text-lead" id="confirmOrderType"></span>
                 </p>
								<p>
									充值的角色：<span class="text-lead" id="confirmCharacter"></span>
								</p>
								<p>
									充值金额：<span class="text-lead" id="confirmAmount"></span>
								</p>
								<div class="well well-warning text-warning text-center">
									<p>提示：本笔充值无法进行退款操作，</p>
									<p>请确认以上操作内容无误后提交充值</p>
								</div>
							</div>
							<div class="modal-footer">
								<a href="javascript:" class="btn btn-confirm btn-primary">确定</a>
							</div>
						</div>
    <div class="modal modal-dialog" id="loading">
    <div class="modal-header clearfix">
    <div class="title text-lead">等待提示</div>
    </div>
    <div class="modal-body">与服务器通信中，请稍后...</div>
    </div>
						<script type="text/javascript">
							var appRatio = 10, characters;
							var current_user_id = '', current_username = '';
						</script>
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