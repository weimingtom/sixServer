/**
 * 公告管理
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
import org.ocean.domain.Game;
import org.ocean.domain.Note;
import org.ocean.service.GameService;
import org.ocean.service.NoteService;
import org.ocean.util.HttpUtil;
import org.ocean.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author beykery
 */
@Controller
@RequestMapping(value = "/")
public class NoteController
{

  @Autowired
  private NoteService ns;
  @Autowired
  private GameService gs;

  /**
   * 公告页面
   *
   * @return
   */
  @Auth(note = "公告列表")
  @RequestMapping(value = "noteList.man")
  public String noteList()
  {
    return "noteList";
  }

  /**
   * 公告列表
   *
   * @param response
   * @param request
   * @param session
   * @return
   * @throws UnsupportedEncodingException
   */
  @Auth(note = "公告列表")
  @RequestMapping(value = "noteJsonList.man")
  public String noteJsonList(HttpServletResponse response, WebRequest request, HttpSession session) throws UnsupportedEncodingException
  {
    Long gameId = (Long) session.getAttribute("curGame");
    Game g = gs.getGameById(gameId);
    Map<String, String[]> ps = request.getParameterMap();
    int page = Integer.parseInt(ps.get("page")[0]);
    int rows = Integer.parseInt(ps.get("rows")[0]);
    JSONObject json = new JSONObject();
    List<Note> list = ns.noteList(gameId, page, rows);
    json.put("total", ns.noteTotal(gameId));
    JSONArray arr = new JSONArray();
    json.put("rows", arr);
    for (Note n : list)
    {
      JSONObject item = new JSONObject();
      item.put("id", n.getId());
      item.put("game", g.getName());
      item.put("title", n.getTitle());
      item.put("content", n.getContent());
      item.put("ctime", n.getCtime() == null ? null : TimeUtil.format(n.getCtime()));
      item.put("ftime", n.getFtime() == null ? null : TimeUtil.format(n.getFtime()));
      item.put("status", n.getStatus());
      item.put("statusString", n.getStatusString());
      item.put("tags", n.getTags());
      arr.add(item);
    }
    HttpUtil.response(response, json.toString().getBytes("UTF-8"));
    return null;
  }

  /**
   * 渠道信息
   *
   * @param session
   * @param response
   * @param nid
   * @return
   * @throws java.io.UnsupportedEncodingException
   */
  @RequestMapping(value = "noteTagsForCombobox.man")
  public String noteTagsForCombobox(HttpSession session, HttpServletResponse response, @RequestParam(value = "nid") long nid) throws UnsupportedEncodingException
  {
    Long gameId = (Long) session.getAttribute("curGame");
    JSONArray tags = ns.getTags(nid, gameId);
    HttpUtil.response(response, tags.toString().getBytes("UTF-8"));
    return null;
  }

  /**
   * 修改公告
   *
   * @param request
   * @param response
   * @param session
   * @return
   */
  @RequestMapping(value = "editNote.man")
  public String editNote(WebRequest request, HttpServletResponse response, HttpSession session)
  {
    Map<String, String[]> ps = request.getParameterMap();
    String title = ps.get("title")[0];
    String content = ps.get("content")[0];
    int status = Integer.parseInt(ps.get("status")[0]);
    long id = Long.parseLong(ps.get("id")[0]);
    String ctime = ps.get("ctime") == null ? null : ps.get("ctime")[0];
    Date cd = ctime == null || ctime.isEmpty() ? null : TimeUtil.parse(ctime);
    String ftime = ps.get("ftime") == null ? null : ps.get("ftime")[0];
    Date fd = ftime == null || ftime.isEmpty() ? null : TimeUtil.parse(ftime);
    String[] tags = ps.get("ts");
    boolean r = ns.editNote(id, title, content, status, cd, fd, tags);
    HttpUtil.response(response, r ? "1" : "error");
    return null;
  }

  /**
   * 添加公告
   *
   * @param request
   * @param response
   * @param session
   * @return
   */
  @RequestMapping(value = "addNote.man")
  public String addNote(WebRequest request, HttpServletResponse response, HttpSession session)
  {
    Long gameId = (Long) session.getAttribute("curGame");
    Map<String, String[]> ps = request.getParameterMap();
    String title = ps.get("title")[0];
    String content = ps.get("content")[0];
    int status = Integer.parseInt(ps.get("status")[0]);
    String ctime = ps.get("ctime") == null ? null : ps.get("ctime")[0];
    Date cd = ctime == null || ctime.isEmpty() ? null : TimeUtil.parse(ctime);
    String ftime = ps.get("ftime") == null ? null : ps.get("ftime")[0];
    Date fd = ftime == null || ftime.isEmpty() ? null : TimeUtil.parse(ftime);
    String[] tags = ps.get("ts");
    boolean r = ns.addNote(gameId, title, content, status, cd, fd, tags);
    HttpUtil.response(response, r ? "1" : "error");
    return null;
  }

  /**
   * 删除公告
   *
   * @param session
   * @param response
   * @param nid
   * @return
   */
  @RequestMapping(value = "deleteNote.man")
  public String deleteNote(HttpSession session, HttpServletResponse response, @RequestParam(value = "id") long nid)
  {
    boolean r = ns.deleteNote(nid);
    HttpUtil.response(response, r ? "1" : "error");
    return null;
  }
}
