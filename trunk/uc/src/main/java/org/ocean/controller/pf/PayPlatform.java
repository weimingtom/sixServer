/**
 * 支付回调
 */
package org.ocean.controller.pf;

import com.alibaba.fastjson.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.UrlBase64;
import org.ocean.domain.Game;
import org.ocean.service.GameService;
import org.ocean.service.OrderService;
import org.ocean.service.PayService;
import org.ocean.util.DESMD5;
import org.ocean.util.HttpUtil;
import org.ocean.util.RequestUtil;
import org.ocean.util.any.PayNotify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author beykery
 */
@Controller
@RequestMapping(value = "/")
public class PayPlatform
{

  private static final Logger LOG = Logger.getLogger(PayPlatform.class);
  @Autowired
  private PayService payService;
  @Autowired
  private GameService gameService;
  @Autowired
  private OrderService orderService;

  @RequestMapping(value = "anypay.pf")
  public String anypay(WebRequest request, HttpServletResponse response) throws UnsupportedEncodingException
  {
    Map<String, String[]> ps = request.getParameterMap();
    Map<String, String> params = new HashMap<>();
    for (String key : ps.keySet())
    {
      params.put(key, ps.get(key)[0]);
    }
    LOG.error("收到any充值到账：" + params);
    String privateData = params.get("private_data");//privatedata里面保存的是游戏id（新版本有别的字段）
    //String product = params.get("product_id");//product_id字段
    int chargeType;//商品id，用来表达月卡和普通充值等
    int gameId;//游戏id
    JSONObject json = JSONObject.parseObject(privateData);
    gameId = json.getIntValue("gameId");//游戏
    chargeType = json.getIntValue("chargeType");//订单类型
    String orderId = json.getString("orderId");//自定义的订单号
    int server_id = Integer.parseInt(params.get("server_id"));//服务器id
    String anyOrder = params.get("order_id");//order
    String amount = params.get("amount");//数额
    int pay_status = Integer.parseInt(params.get("pay_status"));//状态，1为成功
    String channel_number = params.get("channel_number");//渠道编号
    String loginid = params.get("game_user_id");//loginid
    String originSign = params.get("sign");//签名串
    String paramValues = getValues(params);//所有值的连接
    Game g = gameService.getGame(gameId);//game
    String privateKey = g.getAnyPrivateKey();//私有key，用来签验
    PayNotify paynotify = new PayNotify();
    boolean r = paynotify.checkSign(paramValues, originSign, privateKey);
    LOG.error("any充值验证结果: " + r);
    HttpUtil.response(response, "ok".getBytes());
    //充值
    if (r && pay_status == 1)//充值成功
    {
      long ucenterId = 0;
      try
      {
        String temp = new String(DESMD5.decrypt(UrlBase64.decode(loginid.getBytes("UTF-8")), g.getDesKey()));
        JSONObject loginJson = JSONObject.parseObject(temp);
        ucenterId = loginJson.getLong("id");
      } catch (Exception e)
      {
      }
      payService.pay(ucenterId, toDouble(amount), gameId, server_id, chargeType, anyOrder, orderId, channel_number);
    }
    return null;
  }

  /**
   * 取值
   *
   * @param m
   * @return
   */
  public static String getValues(Map<String, String> m)
  {
    Set<String> requestParams = m.keySet();//获得所有的参数名
    List<String> params = new ArrayList<>();
    for (String key : requestParams)
    {
      params.add(key);
    }
    sortParamNames(params);// 将参数名从小到大排序，结果如：adfd,bcdr,bff,zx
    StringBuilder sb = new StringBuilder();
    for (String param : params)
    {//拼接参数值
      if (param.equals("sign"))
      {
        continue;
      }
      String paramValue = m.get(param);
      if (paramValue != null)
      {
        sb.append(paramValue);
      }
    }
    return sb.toString();
  }

  /**
   * 排序所有key
   *
   * @param paramNames
   */
  private static void sortParamNames(List<String> paramNames)
  {
    Collections.sort(paramNames, new Comparator<String>()
    {
      @Override
      public int compare(String str1, String str2)
      {
        return str1.compareTo(str2);
      }
    });
  }

  /**
   * int
   *
   * @param amount
   * @return
   */
  private double toDouble(String amount)
  {
    try
    {
      double v = Double.parseDouble(amount);
      return v;
    } catch (Exception e)
    {
      return -1;
    }
  }

  /**
   * 苹果的充值到账验证
   *
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "ipay.pf")
  public String ipay(WebRequest request, HttpServletResponse response)
  {
    JSONObject d = new JSONObject();
    d.put("status", -1);
    Map<String, String[]> ps = request.getParameterMap();
    Map<String, String> params = new HashMap<>();
    for (String key : ps.keySet())
    {
      params.put(key, ps.get(key)[0]);
    }
    String orderId = params.get("oid");
    String verifyReceipt = params.get("receipt");
    String channelId = RequestUtil.getStringParam(ps,"channelId", null);
    String jsonp = params.get("callback");
    if (orderId != null && verifyReceipt != null && !verifyReceipt.isEmpty() && payService.ipayValide(orderId, verifyReceipt,channelId))
    {
      if (orderService.overOrder(orderId))
      {
        d.put("status", 0);
      }
    }
    LOG.error("充值结果返回给客户端：" + d);
    if (jsonp != null)
    {
      HttpUtil.response(response, jsonp + "(" + d.toString() + ");");
    } else
    {
      HttpUtil.response(response, d.toString());
    }
    return null;
  }

}
