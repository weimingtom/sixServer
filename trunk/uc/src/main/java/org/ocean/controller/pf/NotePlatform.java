/**
 * 公告
 */
package org.ocean.controller.pf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.ocean.domain.Channel;
import org.ocean.domain.Game;
import org.ocean.domain.Note;
import org.ocean.service.ChannelService;
import org.ocean.service.GameService;
import org.ocean.service.NoteService;
import org.ocean.util.HttpUtil;
import org.ocean.util.RequestUtil;
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
public class NotePlatform
{

  @Autowired
  private ChannelService cs;
  @Autowired
  private NoteService ns;
  @Autowired
  private GameService gs;

  /**
   * 查看公告
   *
   * @param response
   * @param cid
   * @return
   */
  @RequestMapping(value = "note.pf")
  public String note(HttpServletResponse response, @RequestParam(value = "channelid") int cid)
  {
    JSONObject json = new JSONObject();
    Channel channel = cs.getChannelByCid(cid);
    String t = channel.getTag();
    Note note = ns.getNote(channel.getGid(), t);
    if (note != null)
    {
      json.put("title", note.getTitle());
      json.put("content", note.getContent());
    }
    json.put("successFlag", true);
    HttpUtil.response(response, json.toString());
    return null;
  }

  /**
   * 灌篮公告
   *
   * @param response
   * @param request
   * @return
   */
  @RequestMapping(value = "sdNoteList.pf")
  public String sdNoteList(HttpServletResponse response, WebRequest request)
  {
    Map<String, String[]> m = request.getParameterMap();
    String channel = RequestUtil.getStringParam(m, "channelId", null);
    JSONArray arr = new JSONArray();
    JSONObject json = new JSONObject();
    arr.add(json);
    JSONObject notice = new JSONObject();
    json.put("notice", notice);
    Game game = gs.getGame(2);
    Note note = ns.getNoteByGame(game, channel);
    if (note != null)
    {
      notice.put("title", note.getTitle());
      notice.put("content", note.getContent());
      notice.put("flag", true);
    } else
    {
      json.put("flag", false);
    }
    HttpUtil.response(response, arr.toString());
    return null;
  }
}
