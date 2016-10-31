/**
 * 客户端管理
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
import org.ocean.dao.BaseDao;
import org.ocean.domain.Channel;
import org.ocean.domain.Client;
import org.ocean.domain.Game;
import org.ocean.service.ClientService;
import org.ocean.service.GameService;
import org.ocean.util.HttpUtil;
import org.ocean.Spring;
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
public class ClientController
{

  @Autowired
  private ClientService cs;
  @Autowired
  private GameService gs;
  @Autowired
  private BaseDao dao;

  /**
   * 版本页面
   *
   * @param session
   * @param model
   * @return
   */
  @Auth(note = "版本列表")
  @RequestMapping(value = "clientList.man")
  public String clientList(HttpSession session, ModelMap model)
  {
    Long tarGame = (Long) session.getAttribute("curGame");
    if (tarGame == null)//未选中任何游戏
    {
      model.addAttribute("error", Spring.message("game_cur_none"));
    }
    return "clientList";
  }

  @RequestMapping(value = "clientJsonList.man")
  public String clientJsonList(HttpSession session, HttpServletResponse response, @RequestParam(value = "page") int page, @RequestParam(value = "rows") int rows) throws UnsupportedEncodingException
  {
    Long tarGame = (Long) session.getAttribute("curGame");
    Game g = gs.getGameById(tarGame);
    List< Client> l = cs.getClients(tarGame, page, rows);
    JSONObject json = new JSONObject();
    json.put("total", cs.clientTotal(tarGame));
    JSONArray arr = new JSONArray();
    json.put("rows", arr);
    for (Client c : l)
    {
      JSONObject item = new JSONObject();
      item.put("id", c.getId());
      item.put("game", g.getName());
      item.put("version", c.getVersion());
      item.put("updateTime", TimeUtil.format(c.getUpdateTime()));
      item.put("address", c.getAddress());
      item.put("forceUpdate", c.isForceUpdate());
      item.put("open", c.isOpen());
      List<Channel> channels = cs.getChannelsByClientId(c.getId());
      item.put("channels", toString(channels));
      arr.add(item);
    }
    HttpUtil.response(response, json.toString().getBytes("UTF-8"));
    return null;
  }

  /**
   * 计算绑定的channel
   *
   * @param cid client id
   * @param session
   * @param response
   * @return
   */
  @RequestMapping(value = "clientChannelBox.man")
  public String clientChannelBox(@RequestParam(value = "cid") long cid, HttpSession session, HttpServletResponse response)
  {
    JSONArray arr = new JSONArray();
    Long tarGame = (Long) session.getAttribute("curGame");
    List<Channel> all = cs.getChannelsByGameId(tarGame);
    List<Channel> l = null;
    Client c = cid > 0 ? dao.get(Client.class, cid) : null;
    if (c != null)
    {
      l = this.cs.getChannelsByClientId(c.getId());
    }
    for (Channel ch : all)
    {
      JSONObject item = new JSONObject();
      item.put("id", ch.getId());
      item.put("name", ch.getName());
      if (l != null && l.contains(ch))
      {
        item.put("selected", true);
      }
      arr.add(item);
    }
    HttpUtil.response(response, arr.toString());
    return null;
  }

  @RequestMapping(value = "deleteClient.man")
  public String deleteClient(@RequestParam(value = "id") long id, HttpServletResponse response)
  {
    boolean r = this.cs.deleteClient(id);
    HttpUtil.response(response, r ? "1" : "无法删除");
    return null;
  }

  /**
   * 编辑客户端版本
   *
   * @param response
   * @param request
   * @return
   */
  @RequestMapping(value = "editClient.man")
  public String editClient(HttpServletResponse response, WebRequest request)
  {
    Map<String, String[]> ps = request.getParameterMap();
    long id = Long.parseLong(ps.get("id")[0]);
    Client c = dao.get(Client.class, id);
    if (c != null)
    {
      c.setAddress(ps.get("address")[0]);
      c.setOpen(Boolean.parseBoolean(ps.get("open")[0]));
      c.setForceUpdate(Boolean.parseBoolean(ps.get("forceUpdate")[0]));
      c.setUpdateTime(ps.get("updateTime") == null || ps.get("updateTime")[0].isEmpty() ? null : TimeUtil.parse(ps.get("updateTime")[0]));
      c.setVersion(ps.get("version")[0]);
      c = dao.saveOrUpdate(c);
      if (c != null)
      {
        this.cs.clearBinding(c.getId());//清除所有的channel绑定，然后再重新绑定
        String[] channels = ps.get("clientChannelBox");
        for (String cid : channels)
        {
          Channel ch = dao.get(Channel.class, Long.parseLong(cid));
          if (ch != null)
          {
            ch.setClid(c.getId());
            dao.saveOrUpdate(ch);
          }
        }
        HttpUtil.response(response, "1");
      } else
      {
        HttpUtil.response(response, "更新失败");
      }
    } else
    {
      HttpUtil.response(response, "不存在指定的客户端");
    }
    return null;
  }

  @RequestMapping(value = "addClient.man")
  public String addClient(HttpServletResponse response, WebRequest request, HttpSession session)
  {
    Map<String, String[]> ps = request.getParameterMap();
    Long tarGame = (Long) session.getAttribute("curGame");
    Client c = new Client();
    c.setAddress(ps.get("address")[0]);
    c.setGid(tarGame);
    c.setOpen(Boolean.parseBoolean(ps.get("open")[0]));
    c.setForceUpdate(Boolean.parseBoolean(ps.get("forceUpdate")[0]));
    c.setUpdateTime(ps.get("updateTime") == null || ps.get("updateTime")[0].isEmpty() ? null : TimeUtil.parse(ps.get("updateTime")[0]));
    c.setVersion(ps.get("version")[0]);
    String[] channels = ps.get("clientChannelBox");
    c = dao.saveOrUpdate(c);
    if (c != null && channels != null)
    {
      for (String cid : channels)
      {
        Channel ch = dao.get(Channel.class, Long.parseLong(cid));
        if (ch != null)
        {
          ch.setClid(c.getId());
          dao.saveOrUpdate(ch);
        }
      }
    }
    HttpUtil.response(response, c == null ? "添加客户端失败" : "1");
    return null;
  }

  /**
   * 转换string
   *
   * @param channels
   * @return
   */
  private String toString(List<Channel> channels)
  {
    StringBuilder sb = new StringBuilder();
    for (Channel c : channels)
    {
      sb.append(c.getName()).append(" + ");
    }
    return sb.length() > 0 ? sb.substring(0, sb.length() - 3) : sb.toString();
  }
}
