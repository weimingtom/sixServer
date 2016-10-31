/**
 * 游戏服务器
 */
package org.ocean.controller.pf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.UrlBase64;
import org.ocean.domain.Game;
import org.ocean.domain.GameServer;
import org.ocean.domain.User;
import org.ocean.service.GameService;
import org.ocean.service.ServerService;
import org.ocean.service.UserService;
import org.ocean.util.DESMD5;
import org.ocean.util.ExceptionUtil;
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
public class ServerPlatform
{

  private static final Logger LOG = Logger.getLogger(ServerPlatform.class);
  @Autowired
  private ServerService ss;
  @Autowired
  private GameService gs;
  @Autowired
  private UserService us;

  @RequestMapping(value = "serverlist.pf")
  public String serverList(WebRequest request, HttpServletResponse response) throws Exception
  {
    JSONObject json = new JSONObject();
    try
    {
      Map<String, String[]> m = request.getParameterMap();
      int channelId = RequestUtil.getIntParam(m, "channelid", -1);
      String loginId = RequestUtil.getStringParam(m, "loginid", null);
      Game g = gs.getGameByChannelid(channelId);
      JSONObject loginJson;
      long ucenterId = -1;
      boolean testing = false;
      String usercode;
      loginJson = JSONObject.parseObject(new String(DESMD5.decrypt(UrlBase64.decode(loginId.getBytes("UTF-8")), g.getDesKey())));
      ucenterId = loginJson.getLong("id");
      testing = loginJson.getBooleanValue("test");
      usercode = loginJson.getString("usercode");
      List<GameServer> gss = ss.getServerList(channelId);
      json.put("successFlag", true);
      json.put("open", testing);
      JSONArray arr = new JSONArray();
      json.put("servers", arr);
      for (GameServer gs : gss)
      {
        JSONObject item = new JSONObject();
        item.put("address", gs.getAddress());
        item.put("name", gs.getName());
        item.put("serverId", gs.getServerId());
        item.put("status", gs.getStatus());
        arr.add(item);
      }
      //最近登录
      arr = new JSONArray();
      json.put("recent", arr);
      List<GameServer> recents = ss.getRecentServers(ucenterId, g);
      for (GameServer s : recents)
      {
        JSONObject item = new JSONObject();
        item.put("address", s.getAddress());
        item.put("name", s.getName());
        item.put("serverId", s.getServerId());
        item.put("status", s.getStatus());
        arr.add(item);
      }
    } catch (Exception e)
    {
      json.put("successFlag", false);
    }
    HttpUtil.response(response, json.toString().getBytes("UTF-8"));
    return null;
  }

  /**
   * 灌篮服务器列表
   *
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "sdServers.pf")
  public String sdServers(WebRequest request, HttpServletResponse response) throws Exception
  {
    JSONArray arr = new JSONArray();
    try
    {
      Map<String, String[]> m = request.getParameterMap();
      String username = RequestUtil.getStringParam(m, "username", null);
      String channel = RequestUtil.getStringParam(m, "channelId", null);
      Game game = gs.getGame(2);
      User user = us.getUser(username);
      List<GameServer> recent = user == null ? Collections.EMPTY_LIST : ss.getRecentServers(user.getId(), game);
      JSONArray re = new JSONArray();
      for (GameServer g : recent)
      {
        String addr = g.getAddress();
        int index = addr.indexOf(":");
        JSONObject item = new JSONObject();
        item.put("ip", addr.subSequence(0, index));
        item.put("port", Integer.parseInt(addr.substring(index + 1)));
        item.put("wz_id", String.valueOf(g.getServerId()));
        item.put("name", g.getName());
        item.put("status", "server status" + g.getStatus());
        re.add(item);
      }
      JSONObject playservers = new JSONObject();
      playservers.put("playservers", re);
      arr.add(playservers);
      List<GameServer> gss = ss.getServerListByGameId(game.getId(), channel);
      JSONArray allserver = new JSONArray();
      for (GameServer g : gss)
      {
        String addr = g.getAddress();
        int index = addr.indexOf(":");
        JSONObject item = new JSONObject();
        item.put("ip", addr.subSequence(0, index));
        item.put("port", Integer.parseInt(addr.substring(index + 1)));
        item.put("wz_id", String.valueOf(g.getServerId()));
        item.put("name", g.getName());
        item.put("status", "server status" + g.getStatus());
        allserver.add(item);
      }
      JSONObject all = new JSONObject();
      all.put("allservers", allserver);
      arr.add(all);
      JSONObject sta = new JSONObject();
      sta.put("flag", user == null ? false : user.isTesting());
      arr.add(sta);
    } catch (Exception e)
    {
      LOG.error(ExceptionUtil.stackTrace(e));
    }
    HttpUtil.response(response, arr.toString().getBytes("UTF-8"));
    return null;
  }
}
