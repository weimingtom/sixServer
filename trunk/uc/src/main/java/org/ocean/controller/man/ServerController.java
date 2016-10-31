/**
 * 服务器管理
 */
package org.ocean.controller.man;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.ocean.annotation.Auth;
import org.ocean.domain.GameServer;
import org.ocean.service.ServerService;
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
public class ServerController
{

  @Autowired
  private ServerService serverService;

  @Auth(note = "服务器列表")
  @RequestMapping(value = "serverList.man")
  public String serverList(HttpSession session, ModelMap model)
  {
    Long gameId = (Long) session.getAttribute("curGame");
    if (gameId == null)//未选中任何游戏
    {
      model.addAttribute("error", Spring.message("game_cur_none"));
    }
    return "serverList";
  }

  /**
   * 添加一个游戏
   *
   * @param request
   * @param response
   * @param session
   * @return
   */
  @Auth(note = "服务器新增")
  @RequestMapping(value = "addServer.man", method = RequestMethod.POST)
  public String addServer(WebRequest request, HttpServletResponse response, HttpSession session)
  {
    Map<String, String[]> ps = request.getParameterMap();
    int serverId = Integer.parseInt(ps.get("serverId")[0]);
    String serverName = ps.get("serverName")[0];
    String serverAddr = ps.get("serverAddr")[0];
    int serverStatus = Integer.parseInt(ps.get("serverStatus")[0]);
    String serverDesc = ps.get("serverDesc")[0];
    int riverPort = Integer.parseInt(ps.get("riverPort")[0]);
    String[] tags = ps.get("tags");
    Long tarGame = (Long) session.getAttribute("curGame");
    boolean r = false;
    try
    {
      r = this.serverService.addServer(tarGame, serverId, serverName, serverAddr, serverStatus, tags, serverDesc, riverPort);
    } catch (Exception e)
    {
    }
    HttpUtil.response(response, r ? "1".getBytes() : "0".getBytes());
    return null;
  }

  @Auth(note = "服务器修改")
  @RequestMapping(value = "editServer.man", method = RequestMethod.POST)
  public String editServer(WebRequest request, HttpServletResponse response, HttpSession session)
  {
    Map<String, String[]> ps = request.getParameterMap();
    long id = Long.parseLong(ps.get("id")[0]);
    String serverName = ps.get("serverName")[0];
    String serverAddr = ps.get("serverAddr")[0];
    int serverStatus = Integer.parseInt(ps.get("serverStatus")[0]);
    String serverDesc = ps.get("serverDesc")[0];
    int riverPort = Integer.parseInt(ps.get("riverPort")[0]);
    String[] tags = ps.get("tags");
    boolean r = false;
    try
    {
      r = this.serverService.editServer(id, serverName, serverAddr, serverStatus, tags, serverDesc, riverPort);
    } catch (Exception e)
    {
    }
    HttpUtil.response(response, r ? "1".getBytes() : "0".getBytes());
    return null;
  }

  /**
   * 删除服务器
   *
   * @param response
   * @param id
   * @return
   */
  @Auth(note = "服务器删除")
  @RequestMapping(value = "deleteServer.man")
  public String deleteServer(HttpServletResponse response, @RequestParam(value = "id") long id)
  {
    boolean r = false;
    try
    {
      r = serverService.deleteServer(id);
    } catch (Exception e)
    {
    }
    HttpUtil.response(response, r ? "1".getBytes() : "0".getBytes());
    return null;
  }

  @RequestMapping(value = "serverJsonList.man")
  public String serverJsonList(HttpServletResponse response, HttpSession session, @RequestParam(value = "page") int page, @RequestParam(value = "rows") int rows) throws UnsupportedEncodingException
  {
    ServletContext servletContext = session.getServletContext();
    String path = servletContext.getRealPath("/");
    Long gameId = (Long) session.getAttribute("curGame");
    List<GameServer> list = serverService.getServerList(gameId, page, rows);
    JSONObject json = new JSONObject();
    json.put("total", serverService.totalServer(gameId));
    JSONArray arr = new JSONArray();
    json.put("rows", arr);
    for (GameServer gs : list)
    {
      JSONObject item = new JSONObject();
      item.put("id", gs.getId());
      item.put("serverId", gs.getServerId());
      item.put("serverName", gs.getName());
      item.put("serverAddr", gs.getAddress());
      item.put("serverStatus", gs.getStatus());
      item.put("serverStatusString", gs.getStatusString());
      item.put("serverDesc", gs.getNote());
      item.put("riverPort", gs.getRiverPort());
      item.put("ts", gs.getTags());
      item.put("innerIp", gs.getInnerIp());
      item.put("innerPort", gs.getInnerPort());
      item.put("lastRelease", gs.getLastRelease());
      item.put("lastVersion", serverService.lastVersion(path, gameId));

      arr.add(item);
    }
    HttpUtil.response(response, json.toString().getBytes("UTF-8"));
    return null;
  }

  /**
   * 所有的tags
   *
   * @param session
   * @param response
   * @param id server id
   * @return
   * @throws java.io.UnsupportedEncodingException
   */
  @RequestMapping(value = "tagsForCombobox.man")
  public String tagsForCombobox(HttpSession session, HttpServletResponse response, @RequestParam(value = "sid") long id) throws UnsupportedEncodingException
  {
    Long gameId = (Long) session.getAttribute("curGame");
    JSONArray tags = serverService.getTags(id, gameId);
    HttpUtil.response(response, tags.toString().getBytes("UTF-8"));
    return null;
  }

  /**
   * 更新服务器
   *
   * @param session
   * @param response
   * @param id
   * @param lastVersion
   * @return
   */
  @RequestMapping(value = "updateServer.man")
  public String updateServer(HttpSession session, HttpServletResponse response, @RequestParam(value = "sid") long id, @RequestParam(value = "lastVersion") String lastVersion)
  {
    String log = serverService.updateServer(id, lastVersion);
    HttpUtil.response(response, log);
    return null;
  }

  /**
   * 停止服务器
   *
   * @param session
   * @param response
   * @param id
   * @return
   */
  @RequestMapping(value = "stopServer.man")
  public String stopServer(HttpSession session, HttpServletResponse response, @RequestParam(value = "sid") long id)
  {
    String log = serverService.stopServer(id);
    HttpUtil.response(response, log);
    return null;

  }

  /**
   * 启动服务器
   *
   * @param session
   * @param response
   * @param id
   * @return
   */
  @RequestMapping(value = "startServer.man")
  public String startServer(HttpSession session, HttpServletResponse response, @RequestParam(value = "sid") long id)
  {
    String log = serverService.startServer(id);
    HttpUtil.response(response, log);
    return null;
  }

  /**
   * 保存服务器配置信息
   *
   * @param request
   * @param response
   * @param session
   * @return
   */
  @RequestMapping(value = "saveConfig.man")
  public String saveConfig(WebRequest request, HttpServletResponse response, HttpSession session)
  {
    Map<String, String[]> ps = request.getParameterMap();
    long id = Long.parseLong(ps.get("id")[0]);
    String redisIp = ps.get("redisIp")[0];
    int redisPort = Integer.parseInt(ps.get("redisPort")[0]);
    String redisPwd = ps.get("redisPwd")[0];
    int redisDb = Integer.parseInt(ps.get("redisDb")[0]);
    String logUrl = ps.get("logUrl")[0];
    String logUser = ps.get("logUser")[0];
    String logPwd = ps.get("logPwd")[0];
    String muser = ps.get("muser")[0];
    String mpwd = ps.get("mpwd")[0];
    String innerIp = ps.get("innerIp")[0];
    int innerPort = Integer.parseInt(ps.get("innerPort")[0]);
    boolean r = serverService.saveConfig(id, redisIp, redisPort, redisPwd, redisDb, logUrl, logUser, logPwd, muser, mpwd, innerIp, innerPort);
    HttpUtil.response(response, r ? "成功" : "失败");
    return null;
  }

  /**
   * 访问服务器配置信息
   *
   * @param session
   * @param response
   * @param id
   * @return
   */
  @RequestMapping(value = "serverConfig.man")
  public String serverConfig(HttpSession session, HttpServletResponse response, @RequestParam(value = "sid") long id)
  {
    GameServer gs = serverService.getServer(id);
    String json = JSON.toJSONString(gs);
    HttpUtil.response(response, json);
    return null;
  }
}
