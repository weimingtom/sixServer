/**
 * 订单
 */
package org.ocean.controller.man;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.ocean.annotation.Auth;
import org.ocean.domain.Orderform;
import org.ocean.service.OrderService;
import org.ocean.util.HttpUtil;
import org.ocean.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author beykery
 */
@Controller
@RequestMapping(value = "/")
public class OrderController
{

  @Autowired
  private OrderService orderService;//订单服务

  @Auth(note = "订单列表")
  @RequestMapping(value = "orderList.man")
  public String orderList(HttpSession session, ModelMap model)
  {
    Long tarGame = (Long) session.getAttribute("curGame");
    if (tarGame == null)
    {
      model.addAttribute("error", "尚未选中任何游戏");
    }
    return "orderList";
  }

  @Auth(note = "完成订单")
  @RequestMapping(value = "overOrder.man")
  public String overOrder(HttpServletResponse response, @RequestParam(value = "id") long id)
  {
    boolean r = orderService.overOrder(id, true);
    HttpUtil.response(response, r ? "1" : "无法完成订单");
    return null;
  }

  @Auth(note = "删除订单")
  @RequestMapping(value = "deleteOrder.man")
  public String deleteOrder(HttpServletResponse response, @RequestParam(value = "id") long id)
  {
    boolean r = orderService.deleteOrder(id);
    HttpUtil.response(response, r ? "1" : "无法删除订单");
    return null;
  }

  @RequestMapping(value = "orderJsonList.man")
  public String orderJsonList(HttpServletResponse response, WebRequest request, HttpSession session) throws UnsupportedEncodingException
  {
    Long tarGame = (Long) session.getAttribute("curGame");
    Map<String, String[]> ps = request.getParameterMap();
    int page = Integer.parseInt(ps.get("page")[0]);
    int rows = Integer.parseInt(ps.get("rows")[0]);
    int sid = ps.get("serverId") == null || ps.get("serverId")[0].trim().isEmpty() ? 0 : Integer.parseInt(ps.get("serverId")[0]);
    long uid = ps.get("userId") == null || ps.get("userId")[0].trim().isEmpty() ? 0 : Long.parseLong(ps.get("userId")[0]);
    String uname = ps.get("userName") == null ? null : ps.get("userName")[0];
    Date start = ps.get("start") == null ? null : TimeUtil.parse(ps.get("start")[0]);
    Date end = ps.get("end") == null ? null : TimeUtil.parse(ps.get("end")[0]);
    String sort = ps.get("sort") == null ? null : ps.get("sort")[0];
    String order = ps.get("order") == null ? null : ps.get("order")[0];
    JSONObject json = new JSONObject();
    List<Orderform> list = orderService.getOrders(tarGame, sid, uname, uid, start, end, page, rows, sort, order);
    long total = orderService.total(tarGame, sid, uname, uid, start, end);
    json.put("total", total);
    JSONArray arr = new JSONArray();
    json.put("rows", arr);
    for (Orderform of : list)
    {
      JSONObject item = new JSONObject();
      item.put("id", of.getId());
      item.put("oid", of.getOid());
      item.put("anyOid", of.getAnyOid());
      item.put("serverName", orderService.getServerName(of.getGameId(), of.getServerId()));
      item.put("userName", of.getUsercode());
      item.put("price", of.getPrice());
      item.put("status", of.getStatus());
      item.put("statusString", of.getStatusString());
      item.put("chargeType", of.getChargeType());
      item.put("anyChannel", of.getAnyChannel());
      item.put("ctime", TimeUtil.format(of.getCtime()));
      item.put("manual", of.isManual());
      if (of.getFtime() != null)
      {
        item.put("ftime", TimeUtil.format(of.getFtime()));
      }
      arr.add(item);
    }
    //footer
    JSONArray footer = new JSONArray();
    JSONObject t = new JSONObject();
    t.put("id", "合计");
    t.put("price", orderService.totalPrice(tarGame, sid, uname, uid, start, end));
    footer.add(t);
    json.put("footer", footer);
    HttpUtil.response(response, json.toString().getBytes("UTF-8"));
    return null;
  }
}
