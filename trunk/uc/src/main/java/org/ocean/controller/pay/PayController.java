/**
 * 支付
 */
package org.ocean.controller.pay;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.ocean.controller.pay.ali.AlipayConfig;
import org.ocean.controller.pay.ali.AlipayCore;
import org.ocean.controller.pay.ali.AlipayNotify;
import org.ocean.controller.pay.ali.MessageCode;
import org.ocean.domain.Game;
import org.ocean.domain.Orderform;
import org.ocean.domain.User;
import org.ocean.service.GameService;
import org.ocean.service.OrderService;
import org.ocean.service.ServerService;
import org.ocean.service.UserService;
import org.ocean.util.HttpUtil;
import org.ocean.Spring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import java.util.*;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import org.ocean.controller.pay.weixin.bean.Order;
import org.ocean.controller.pay.weixin.bean.OrderMessage;
import org.ocean.controller.pay.weixin.bean.OrderReturn;
import org.ocean.controller.pay.weixin.utils.Config;
import org.ocean.controller.pay.weixin.utils.HttpRequest;
import org.ocean.controller.pay.weixin.utils.Sign;
import org.ocean.util.StringUtil;

/**
 *
 * @author Admin
 */
@Controller
@RequestMapping(value = "/")
public class PayController
{

  private static final Logger LOG = Logger.getLogger(PayController.class);
  @Autowired
  private GameService gs;
  @Autowired
  private ServerService ss;
  @Autowired
  private UserService us;
  @Autowired
  private OrderService os;
  private final String alipaychoose = "alipaychoose";
  private final String index = "index";

  @RequestMapping(value = "index.pay")
  public String index()
  {
    return "index";
  }

  @RequestMapping(value = "selectGame.pay")
  public String selectGame(@RequestParam(name = "game") int gid, ModelMap model)
  {
    Game g = gs.getGame(gid);
    if (g != null)
    {
      model.addAttribute("game", g);
      model.addAttribute("serverList", ss.getServerList(g.getId(), 0, 1000000));
      return alipaychoose;
    }
    return index;
  }

  /**
   * ali支付
   *
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "alipay.pay")
  public String alipay(WebRequest request, HttpServletResponse response, ModelMap model)
  {
    Map<String, String[]> map = request.getParameterMap();
    int gameId = Integer.parseInt(get(map, "gameid"));
    int serverId = Integer.parseInt(get(map, "serverid"));
    int money = Integer.parseInt(get(map, "amount"));
    String userCode = get(map, "usercode");
    String paymethod = get(map, "paymethod");
    int orderType = Integer.parseInt(get(map, "ordertype"));
    String defaultbank = get(map, "defaultbank");
    User user = us.getUser(userCode);
    if (gameId > 0 && serverId > 0 && money > 0)
    {
      // 必填 //商户订单号
      Orderform of = os.createOrder(userCode, money, gameId, serverId, orderType, "alipay");// 商户网站订单系统中唯一订单号，必填
      if (of != null)
      {
        Game game = gs.getGame(gameId);
        // 订单名称
        String subject = game != null ? "游戏《" + game.getName() + "》充值" : "游戏充值";
        // 必填 
        String body = "游戏内虚拟币兑换";
        // 把请求参数打包成数组
        Map<String, String> sParaTemp = new HashMap<>();
        sParaTemp.put("out_trade_no", of.getOid());
        sParaTemp.put("subject", subject);
        sParaTemp.put("total_fee", user != null && user.isTesting() ? "0.01" : String.valueOf(money));
        sParaTemp.put("body", body);
        if (paymethod != null && paymethod.trim().length() > 0)
        {
          sParaTemp.put("paymethod", paymethod);
          if (defaultbank != null && defaultbank.trim().length() > 0)
          {
            sParaTemp.put("defaultbank", defaultbank);
          } else
          {
            request.setAttribute("error", "请选择支付银行！", RequestAttributes.SCOPE_REQUEST);
            return "redirect:/ index";
          }
        }
        sParaTemp.put("service", AlipayConfig.alipay_service);
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        sParaTemp.put("payment_type", AlipayConfig.payment_type);
        sParaTemp.put("notify_url", AlipayConfig.notifyUrl);
        sParaTemp.put("return_url", AlipayConfig.return_url);
        sParaTemp.put("show_url", AlipayConfig.show_url);
        sParaTemp.put("seller_email", AlipayConfig.seller_email);

        String html = AlipayCore.buildRequest(sParaTemp, "get", "pay");
        model.addAttribute("orderid", of.getOid());
        model.addAttribute("postdata", html);
        MessageCode messageCode = new MessageCode(true, html, of.getOid());
        HttpUtil.response(response, JSON.toJSONString(messageCode));
      } else
      {
        MessageCode messageCode = new MessageCode(false, "参数错误！000", null);
        HttpUtil.response(response, JSON.toJSONString(messageCode));
      }
    } else
    {
      MessageCode messageCode = new MessageCode(false, "参数错误！000", null);
      HttpUtil.response(response, JSON.toJSONString(messageCode));
    }
    return null;
  }

  /**
   * 微信支付
   *
   * @param request
   * @param hsr
   * @param response
   * @param model
   * @return
   * @throws java.io.UnsupportedEncodingException
   */
  @RequestMapping(value = "wppay.pay")
  public String wppay(WebRequest request, HttpServletRequest hsr, HttpServletResponse response, ModelMap model) throws UnsupportedEncodingException
  {
    Map<String, String[]> map = request.getParameterMap();
    int gameId = Integer.parseInt(get(map, "gameid"));
    int serverId = Integer.parseInt(get(map, "serverid"));
    int money = Integer.parseInt(get(map, "amount"));
    String userCode = get(map, "usercode");
    String paymethod = get(map, "paymethod");
    int orderType = Integer.parseInt(get(map, "ordertype"));
    String defaultbank = get(map, "defaultbank");
    User user = us.getUser(userCode);
    if (gameId > 0 && serverId > 0 && money > 0)
    {
      // 必填 //商户订单号
      Orderform of = os.createOrder(userCode, money, gameId, serverId, orderType, "wppay");// 商户网站订单系统中唯一订单号，必填
      if (of != null)
      {
        Game game = gs.getGame(gameId);
        String u = hsr.getRemoteAddr();
        LOG.error("客户端ip地址: " + u);
        String[] sp = StringUtil.split(u, '.');
        if (sp[0].equals("0") || sp.length < 4)
        {
          u = "8.8.8.8";
        } else
        {
          u = sp[0] + "." + sp[1] + "." + sp[2] + "." + sp[3];
        }
        double price = user != null && user.isTesting() ? 0.01 : of.getPrice();
        String codeurl = getWpCodeurl(String.valueOf(price), game.getName() + " 充值", of.getOid(), u);
        MessageCode messageCode = new MessageCode(true, of.getOid(), codeurl == null ? "http://nookjoy.com" : codeurl);
        HttpUtil.response(response, JSON.toJSONString(messageCode));
      } else
      {
        MessageCode messageCode = new MessageCode(false, "参数错误！000", null);
        HttpUtil.response(response, JSON.toJSONString(messageCode));
      }
    } else
    {
      MessageCode messageCode = new MessageCode(false, "参数错误！000", null);
      HttpUtil.response(response, JSON.toJSONString(messageCode));
    }
    return null;
  }

  /**
   * 微信支付到账通知
   *
   * @param request
   * @param response
   * @return
   * @throws java.lang.Exception
   */
  @RequestMapping(value = "wpaynotify.pay")
  public String wpaynotify(HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    // 支付成功后，接受微信反馈的参数
    XStream xstream = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_")));
    ServletInputStream s = request.getInputStream();
    xstream.alias("xml", OrderMessage.class);
    OrderMessage om = (OrderMessage) xstream.fromXML(s);
    LOG.error("微信支付通知: " + om);
    if (om.sign(Config.key).equals(om.getSign()))
    {
      os.overOrder(om.getOut_trade_no());//更新订单状态
    } else
    {
      LOG.error("签验失败 " + om);
    }
    String rs = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    try (PrintWriter pw = response.getWriter())
    {
      pw.print(rs);
      pw.flush();
    }
    return null;
  }

  @RequestMapping(value = "payordercheck.pay")
  public String payordercheck(@RequestParam(value = "orderid") String order, ModelMap model)
  {
    String orderId = order;
    Orderform orderForm = os.getOrderByOid(orderId);
    if (orderForm != null)
    {
      model.put("status", orderForm.getStatus());
      model.put("gameid", orderForm.getGameId());
      model.put("serverid", orderForm.getServerId());
    } else
    {
      model.put("status", -1);
    }
    return "alipayback";
  }

  @RequestMapping(value = "paymentcheck.pay")
  public String paymentcheck(@RequestParam(value = "usercode") String usercode,
          @RequestParam(value = "serverid") int serverId,
          @RequestParam(value = "gameid") int gameId, HttpServletResponse response)
  {
    MessageCode messageCode;
    Player p = us.getPlayer(gameId, serverId, usercode);
    messageCode = new MessageCode(true, p == null ? Spring.message("user_nickName_notfound") : p.getNickyName(), null);
    HttpUtil.response(response, JSON.toJSONString(messageCode));
    return null;
  }

  @RequestMapping(value = "alipayNotify.pay")
  public String alipayNotify(HttpServletResponse response, WebRequest request) throws IOException
  {
    Map<String, String[]> paras = request.getParameterMap();
    Map<String, String> params = new HashMap<>();
    for (Map.Entry<String, String[]> en : paras.entrySet())
    {
      params.put(en.getKey(), en.getValue()[0]);
    }
    Map<String, String> sParaNew = AlipayCore.paraFilter(params);
    //获取待签名字符串
    LOG.error("alipayNotify:" + params);
    String preSignStr = AlipayCore.createLinkString(sParaNew);
    String out_trade_no = params.get("out_trade_no");
    String trade_status = params.get("trade_status");
    //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
    if (AlipayNotify.verify(params))
    {
      //验证成功
      //////////////////////////////////////////////////////////////////////////////////////////
      //请在这里加上商户的业务逻辑程序代码
      LOG.error("验证成功");
      //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
      if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS"))
      {
        //判断该笔订单是否在商户网站中已经做过处理
        //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
        //如果有做过处理，不执行商户的业务程序
        os.overOrder(out_trade_no);
        //注意：
        //该种交易状态只在两种情况下出现
        //1、开通了普通即时到账，买家付款成功后。
        //2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。
      }
//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
      try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(response.getOutputStream())))
      {
        bw.write("success");
      }
    } else
    {//验证失败
      LOG.error("验证失败");
      try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(response.getOutputStream())))
      {
        bw.write("fail");
      }
    }
    return null;
  }

  /**
   * 值
   *
   * @param map
   * @param key
   * @return
   */
  public String get(Map<String, String[]> map, String key)
  {
    String[] v = map.get(key);
    return v == null ? null : (v.length <= 0 ? null : v[0]);
  }

  /**
   * 微信支付模式二的扫描url
   *
   * @return
   */
  private String getWpCodeurl(String price, String name, String oid, String ip) throws UnsupportedEncodingException
  {
    Order o = new Order();
    String appid = Config.appid;
    String mch_id = Config.mch_id;
    String nonce_str = UUID.randomUUID().toString().trim().replaceAll("-", "");
    String body = name;
    String out_trade_no = oid;
    int total_fee = (int) (Double.parseDouble(price) * 100);//单位为分
    String spbill_create_ip = ip;
    String notify_url = Config.notify_url;
    String trade_type = Config.trade_type;
    String key = Config.key;
    o.setAppid(appid);
    o.setBody(body);
    o.setMch_id(mch_id);
    o.setNotify_url(notify_url);
    o.setOut_trade_no(out_trade_no);
    o.setTotal_fee(total_fee);
    o.setNonce_str(nonce_str);
    o.setTrade_type(trade_type);
    o.setSpbill_create_ip(spbill_create_ip);
    SortedMap<String, String> p = new TreeMap<>();
    p.put("appid", appid);
    p.put("mch_id", mch_id);
    p.put("body", body);
    p.put("nonce_str", nonce_str);
    p.put("out_trade_no", out_trade_no);
    p.put("total_fee", String.valueOf(total_fee));
    p.put("spbill_create_ip", spbill_create_ip);
    p.put("notify_url", notify_url);
    p.put("trade_type", trade_type);
    // 得到签名
    String sign = Sign.createSign("utf-8", p, key);
    o.setSign(sign);
    // 转换为XML
    XStream xstream = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_")));
    xstream.alias("xml", Order.class);
    String xml = xstream.toXML(o);
    System.out.println(xml);
    // 统一下单
    String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    String returnXml = HttpRequest.sendPost(url, xml);
    LOG.error("下单返回: " + returnXml);
    XStream xstream2 = new XStream(new DomDriver());
    xstream2.alias("xml", OrderReturn.class);
    OrderReturn or = (OrderReturn) xstream2.fromXML(returnXml);
    return or.getCode_url();
  }
}
