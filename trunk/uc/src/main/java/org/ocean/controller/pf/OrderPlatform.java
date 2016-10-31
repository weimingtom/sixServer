/**
 * 创建订单的接口
 */
package org.ocean.controller.pf;

import com.alibaba.fastjson.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.ocean.domain.Orderform;
import org.ocean.service.OrderService;
import org.ocean.util.HttpUtil;
import org.ocean.util.RequestUtil;
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
public class OrderPlatform
{

  private static final Logger LOG = Logger.getLogger(OrderPlatform.class);
  @Autowired
  private OrderService orderService;

  @RequestMapping(value = "createOrder.pf")
  public String createOrder(WebRequest request, HttpServletResponse response) throws UnsupportedEncodingException
  {
    Map<String, String[]> map = request.getParameterMap();
    int price = RequestUtil.getIntParam(map, "price", -1);
    int gid = RequestUtil.getIntParam(map, "gid", -1);
    int sid = RequestUtil.getIntParam(map, "sid", -1);
    int ot = RequestUtil.getIntParam(map, "type", -1);
    String usercode = RequestUtil.getStringParam(map, "usercode", null);
    String jsonp = RequestUtil.getStringParam(map, "callback", null);
    LOG.error("createOrder: " + price + ":" + gid + ":" + sid + ":" + ot + ":" + usercode + ":" + jsonp);
    if (price < 0 || gid < 0 || sid < 0 || ot < 0 || usercode == null)
    {
      return null;
    }
    Orderform orderform = orderService.createOrder(usercode, price, gid, sid, ot, null);
    JSONObject json = new JSONObject();
    json.put("oid", orderform.getOid());
    if (jsonp != null)
    {
      HttpUtil.response(response, jsonp + "(" + json.toString() + ");");
    } else
    {
      HttpUtil.response(response, json.toString());
    }
    return null;
  }
}
