/**
 * 渠道controller
 */
package org.ocean.controller.man;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.ocean.annotation.Auth;
import org.ocean.domain.Channel;
import org.ocean.service.ChannelService;
import org.ocean.util.HttpUtil;
import org.ocean.Spring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author beykery
 */
@Controller
@RequestMapping(value = "/")
public class ChannelController
{

  @Autowired
  private ChannelService channelService;

  @Auth(note = "渠道列表")
  @RequestMapping(value = "channelList.man")
  public String channelList(HttpSession session, ModelMap model)
  {
    Long tarGame = (Long) session.getAttribute("curGame");
    if (tarGame == null)//未选中任何游戏
    {
      model.addAttribute("error", Spring.message("game_cur_none"));
    }
    return "channelList";
  }

  /**
   * 添加一个渠道
   *
   * @param request
   * @param response
   * @param session
   * @return
   */
  @Auth(note = "渠道新增")
  @RequestMapping(value = "addChannel.man", method = RequestMethod.POST)
  public String addChannel(WebRequest request, HttpServletResponse response, HttpSession session)
  {
    Map<String, String[]> ps = request.getParameterMap();
    int cid = Integer.parseInt(ps.get("cid")[0]);
    String name = ps.get("name")[0];
    String tag = ps.get("tag")[0];
    String note = ps.get("note")[0];
    String bid = ps.get("bid")[0];
    Long tarGame = (Long) session.getAttribute("curGame");
    boolean r = false;
    try
    {
      r = channelService.addChannel(tarGame, cid, name, tag, note, bid);
    } catch (Exception e)
    {
    }
    HttpUtil.response(response, r ? "1".getBytes() : "0".getBytes());
    return null;
  }

  /**
   * 修改渠道
   *
   * @param request
   * @param response
   * @return
   */
  @Auth(note = "渠道修改")
  @RequestMapping(value = "editChannel.man", method = RequestMethod.POST)
  public String editChannel(WebRequest request, HttpServletResponse response)
  {
    Map<String, String[]> ps = request.getParameterMap();
    int cid = Integer.parseInt(ps.get("cid")[0]);
    String name = ps.get("name")[0];
    String tag = ps.get("tag")[0];
    String note = ps.get("note")[0];
    String bid = ps.get("bid")[0];
    long id = Long.parseLong(ps.get("id")[0]);
    boolean r = false;
    try
    {
      r = channelService.editChannel(id, cid, name, tag, note, bid);
    } catch (Exception e)
    {
    }
    HttpUtil.response(response, r ? "1".getBytes() : "0".getBytes());
    return null;
  }

  /**
   * 删除渠道
   *
   * @param response
   * @param id
   * @return
   */
  @Auth(note = "渠道删除")
  @RequestMapping(value = "deleteChannel.man")
  public String deleteChannel(HttpServletResponse response, @RequestParam(value = "id") long id)
  {
    boolean r = false;
    try
    {
      r = channelService.deleteChannel(id);
    } catch (Exception e)
    {
    }
    HttpUtil.response(response, r ? "1".getBytes() : "0".getBytes());
    return null;
  }

  @RequestMapping(value = "channelJsonList.man")
  public String channelJsonList(HttpSession session, HttpServletResponse response, @RequestParam(value = "page") int page, @RequestParam(value = "rows") int rows) throws UnsupportedEncodingException
  {
    Long tarGame = (Long) session.getAttribute("curGame");
    List<Channel> list = channelService.channelList(tarGame, page, rows);
    JSONObject json = new JSONObject();
    json.put("total", channelService.channelTotal(tarGame));
    JSONArray arr = new JSONArray();
    json.put("rows", arr);
    for (Channel c : list)
    {
      JSONObject item = new JSONObject();
      item.put("id", c.getId());
      item.put("cid", c.getCid());
      item.put("name", c.getName());
      item.put("tag", c.getTag());
      item.put("note", c.getNote());
      item.put("bid", c.getBid());
      arr.add(item);
    }
    HttpUtil.response(response, json.toString().getBytes("UTF-8"));
    return null;
  }
}
