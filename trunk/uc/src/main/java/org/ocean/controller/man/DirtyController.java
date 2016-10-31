/**
 * 脏词
 */
package org.ocean.controller.man;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.ocean.annotation.Auth;
import org.ocean.dao.BaseDao;
import org.ocean.domain.DirtyWord;
import org.ocean.service.KeyWordsService;
import org.ocean.util.HttpUtil;
import org.ocean.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author beykery
 */
@Controller
@RequestMapping(value = "/")
public class DirtyController
{

  private static final Logger LOG = Logger.getLogger(DirtyController.class);
  @Autowired
  private KeyWordsService kws;
  @Autowired
  private BaseDao dao;

  @Auth(note = "脏词列表")
  @RequestMapping(value = "dirtyList.man")
  public String dirtyList()
  {
    return "dirtyList";
  }

  @RequestMapping(value = "addDirty.man")
  public String addDirty(@RequestParam(value = "word") String word, @RequestParam(value = "note") String note, HttpServletResponse response)
  {
    String[] ws = StringUtil.split(word, ',');
    if (ws != null && ws.length > 0)
    {
      for (String w : ws)
      {
        try
        {
          kws.addWord(w, note);
        } catch (Exception e)
        {
          e.printStackTrace();
          LOG.error("无法添加脏词：" + w);
        }
      }
    }
    HttpUtil.response(response, "1");
    return null;
  }

  @RequestMapping(value = "editDirty.man")
  public String editDirty(@RequestParam(value = "id") long id, @RequestParam(value = "word") String word, @RequestParam(value = "note") String note, HttpServletResponse response)
  {
    boolean r = false;
    DirtyWord dw = kws.getDirty(id);
    if (dw != null)
    {
      dw.setNote(note);
      dw.setWord(word);
      r = dao.saveOrUpdate(dw) != null;
    }
    HttpUtil.response(response, r ? "1" : "修改脏词失败");
    return null;
  }

  /**
   * 删除脏词
   *
   * @param id
   * @param response
   * @return
   */
  @RequestMapping(value = "deleteDirty.man")
  public String deleteDirty(@RequestParam(value = "id") long id, HttpServletResponse response)
  {
    boolean r = kws.deleteDirty(id);
    HttpUtil.response(response, r ? "1" : "修改脏词失败");
    return null;
  }

  /**
   * dirty列表
   *
   * @param response
   * @param page
   * @param rows
   * @return
   * @throws java.io.UnsupportedEncodingException
   */
  @RequestMapping(value = "dirtyJsonList.man")
  public String dirtyJsonList(HttpServletResponse response, @RequestParam(value = "page") int page, @RequestParam(value = "rows") int rows) throws UnsupportedEncodingException
  {
    List<DirtyWord> l = kws.dirtyList(page, rows);
    JSONObject json = new JSONObject();
    json.put("total", kws.dirtyTotal());
    JSONArray arr = new JSONArray();
    json.put("rows", arr);
    for (DirtyWord dw : l)
    {
      JSONObject item = new JSONObject();
      item.put("word", dw.getWord());
      item.put("note", dw.getNote());
      item.put("id", dw.getId());
      arr.add(item);
    }
    HttpUtil.response(response, json.toString().getBytes("UTF-8"));
    return null;
  }
}
