/**
 * 游戏管理
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
import org.ocean.dao.BaseDao;
import org.ocean.domain.Game;
import org.ocean.service.GameService;
import org.ocean.util.HttpUtil;
import org.ocean.Spring;
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
public class GameController
{

  @Autowired
  private BaseDao dao;
  @Autowired
  private GameService gameSerivce;

  /**
   * 添加一个新游戏
   *
   * @param request
   * @param response
   * @param model
   * @return
   * @throws java.io.UnsupportedEncodingException
   */
  @Auth(note = "游戏新增")
  @RequestMapping(value = "addGame.man")
  public String addGame(WebRequest request, HttpServletResponse response, ModelMap model) throws UnsupportedEncodingException
  {
    Map<String, String[]> ps = request.getParameterMap();
    int gameId = Integer.parseInt(ps.get("gameId")[0]);
    String gameName = ps.get("gameName")[0];
    String gameBid = ps.get("gameBid")[0];
    int gameStatus = Integer.parseInt(ps.get("gameStatus")[0]);
    String desKey = ps.get("desKey")[0];
    String gameDec = ps.get("gameDec")[0];
    String anykey = ps.get("anyPrivateKey")[0];//anysdk的私有key
    Game g = new Game(gameName, gameId, gameBid, gameStatus, gameDec, desKey, anykey);
    g.setCreateTime(new Date());
    try
    {
      g = dao.saveOrUpdate(g);
    } catch (Exception e)
    {
      g = null;
    }
    if (g == null)//失败
    {
      model.addAttribute("error", Spring.message("game_add_error"));
    }
    HttpUtil.response(response, (g == null ? "添加游戏失败" : "1").getBytes("UTF-8"));
    return null;
  }

  /**
   * 服务器列表
   *
   * @return
   */
  @Auth(note = "游戏列表")
  @RequestMapping(value = "gameList.man")
  public String gameList()
  {
    return "gameList";
  }

  /**
   * 请求json格式的数据
   *
   * @param request
   * @param response
   * @param page
   * @param rows
   * @return
   * @throws java.io.UnsupportedEncodingException
   */
  @RequestMapping(value = "gameJsonList.man")
  public String gameJsonList(WebRequest request, HttpServletResponse response,
          @RequestParam(value = "page") int page,
          @RequestParam(value = "rows") int rows) throws UnsupportedEncodingException
  {
    List<Game> gs = gameSerivce.getGames(page, rows);
    JSONObject json = new JSONObject();
    JSONArray arr = new JSONArray();
    json.put("rows", arr);
    json.put("total", gameSerivce.totalGames());
    for (Game g : gs)
    {
      JSONObject item = new JSONObject();
      item.put("id", g.getId());
      item.put("gameId", g.getGameId());
      item.put("gameName", g.getName());
      item.put("gameBid", g.getBid());
      item.put("gameStatus", g.getStatus());
      item.put("gameStatusString", g.getStatusString());
      item.put("gameDec", g.getNote());
      item.put("desKey", g.getDesKey());
      item.put("anyPrivateKey", g.getAnyPrivateKey());
      arr.add(item);
    }
    HttpUtil.response(response, json.toString().getBytes("UTF-8"));
    return null;
  }

  /**
   * 为了combox准备
   *
   * @param request
   * @param response
   * @return
   * @throws UnsupportedEncodingException
   */
  @RequestMapping(value = "gameJsonBox.man")
  public String gameJsonBox(WebRequest request, HttpServletResponse response) throws UnsupportedEncodingException
  {
    List<Game> gs = gameSerivce.getGames();
    JSONArray arr = new JSONArray();
    for (Game g : gs)
    {
      JSONObject item = new JSONObject();
      item.put("gameId", g.getGameId());
      item.put("gameName", g.getName());
      item.put("gameBid", g.getBid());
      item.put("gameStatus", g.getStatus());
      item.put("gameDec", g.getNote());
      item.put("id", g.getId());
      arr.add(item);
    }
    HttpUtil.response(response, arr.toString().getBytes("UTF-8"));
    return null;
  }

  /**
   * 修改游戏
   *
   * @param request
   * @param response
   * @return
   */
  @Auth(note = "游戏修改")
  @RequestMapping(value = "editGame.man")
  public String editGame(WebRequest request, HttpServletResponse response)
  {
    Map<String, String[]> ps = request.getParameterMap();
    int gameId = Integer.parseInt(ps.get("gameId")[0]);
    String gameName = ps.get("gameName")[0];
    String gameBid = ps.get("gameBid")[0];
    int gameStatus = Integer.parseInt(ps.get("gameStatus")[0]);
    String desKey = ps.get("desKey")[0];
    String gameDec = ps.get("gameDec")[0];
    String anykey = ps.get("anyPrivateKey")[0];
    long id = Long.parseLong(ps.get("id")[0]);
    boolean r = false;
    try
    {
      r = gameSerivce.updateGame(id, gameId, gameName, gameBid, gameStatus, gameDec, desKey, anykey);
    } catch (Exception e)
    {
    }
    HttpUtil.response(response, (r ? "1" : "0").getBytes());
    return null;
  }

  @Auth(note = "游戏删除")
  @RequestMapping(value = "deleteGame.man")
  public String deleteGame(@RequestParam(value = "id") long id, HttpServletResponse response)
  {
    boolean r = false;
    try
    {
      r = gameSerivce.deleteGame(id);
    } catch (Exception e)
    {
    }
    HttpUtil.response(response, (r ? "1" : "0").getBytes());
    return null;
  }

  /**
   * 设置当前所选游戏
   *
   * @param gameId
   * @param session
   * @param response
   * @return
   */
  @RequestMapping(value = "setCurGame.man")
  public String setCurGame(@RequestParam(value = "gameId") int gameId, HttpSession session, HttpServletResponse response)
  {
    Game g = gameSerivce.getGame(gameId);
    if (g != null)
    {
      session.setAttribute("curGame", g.getId());
    }
    HttpUtil.response(response, g == null ? "0".getBytes() : "1".getBytes());
    return null;
  }
}
