/**
 * 权限控制
 */
package org.ocean.controller.man;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.ocean.annotation.Auth;
import org.ocean.dao.BaseDao;
import org.ocean.domain.Role;
import org.ocean.service.AuthService;
import org.ocean.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
public class AuthController
{

  private static final Logger LOG = Logger.getLogger(AuthController.class);
  @Autowired
  private AuthService authService;
  @Autowired
  private BaseDao dao;

  @Auth(note = "角色列表")
  @RequestMapping(value = "authList.man", method = RequestMethod.GET)
  public String authList()
  {
    return "authList";
  }

  /**
   *
   * @param response
   * @param page
   * @param rows
   * @return
   * @throws java.io.UnsupportedEncodingException
   */
  @RequestMapping(value = "roleJsonList.man")
  public String roleJsonList(HttpServletResponse response, @RequestParam(value = "page") int page, @RequestParam(value = "rows") int rows) throws UnsupportedEncodingException
  {
    List<Role> list = authService.roleList(page, rows);
    JSONObject json = new JSONObject();
    json.put("total", authService.roleTotal());
    JSONArray arr = new JSONArray();
    json.put("rows", arr);
    for (Role u : list)
    {
      JSONObject item = new JSONObject();
      item.put("name", u.getName());
      item.put("note", u.getNote());
      item.put("id", u.getId());
      arr.add(item);
    }
    HttpUtil.response(response, json.toString().getBytes("UTF-8"));
    return null;
  }

  /**
   * 删除角色
   *
   * @param response
   * @param id
   * @return
   * @throws java.io.UnsupportedEncodingException
   */
  @Auth(note = "角色删除")
  @RequestMapping(value = "deleteRole.man")
  public String deleteRole(HttpServletResponse response, @RequestParam(value = "id") long id) throws UnsupportedEncodingException
  {
    boolean r = false;
    try
    {
      r = authService.deleteRole(id);
    } catch (Exception e)
    {
      LOG.error("无法删除role：" + e.getMessage());
    }
    HttpUtil.response(response, r ? "1".getBytes() : "无法删除角色".getBytes("UTF-8"));
    return null;
  }

  /**
   * 添加role
   *
   * @param request
   * @param response
   * @param session
   * @return
   */
  @Auth(note = "角色添加")
  @RequestMapping(value = "addRole.man")
  public String addRole(WebRequest request, HttpServletResponse response, HttpSession session)
  {
    Map<String, String[]> ps = request.getParameterMap();
    String name = ps.get("name")[0];
    String note = ps.get("note")[0];
    String[] ops = ps.get("ops");
    boolean r = false;
    try
    {
      r = authService.addRole(name, note, ops);
    } catch (Exception e)
    {
    }
    HttpUtil.response(response, r ? "1".getBytes() : "添加角色失败".getBytes());
    return null;
  }

  /**
   * 修改role
   *
   * @param request
   * @param response
   * @param session
   * @return
   */
  @Auth(note = "角色修改")
  @RequestMapping(value = "editRole.man")
  public String editRole(WebRequest request, HttpServletResponse response, HttpSession session)
  {
    Map<String, String[]> ps = request.getParameterMap();
    long id = Long.parseLong(ps.get("id")[0]);
    String name = ps.get("name")[0];
    String note = ps.get("note")[0];
    String[] ops = ps.get("ops");
    boolean r = false;
    try
    {
      r = authService.editRole(id, name, note, ops);
    } catch (Exception e)
    {
    }
    HttpUtil.response(response, r ? "1".getBytes() : "修改角色失败".getBytes());
    return null;
  }

  /**
   * role的操作列表
   *
   * @param rid role id
   * @param response
   * @return
   * @throws java.io.UnsupportedEncodingException
   */
  @RequestMapping(value = "opsForAuthBox.man")
  public String opsForAuthBox(@RequestParam(value = "rid") long rid, HttpServletResponse response) throws UnsupportedEncodingException
  {
    JSONArray result = new JSONArray();
    Role r = dao.get(Role.class, rid);
    String cur = r == null ? null : r.getOps();
    JSONArray arr = new JSONArray();
    if (cur != null && !cur.isEmpty())
    {
      arr = JSONArray.parseArray(cur);
    }
    for (Map.Entry<String, String> en : this.authService.getOps().entrySet())
    {
      JSONObject item = new JSONObject();
      item.put("op", en.getKey());
      item.put("note", en.getValue());
      if (arr.contains(en.getKey()))
      {
        item.put("selected", true);
      }
      result.add(item);
    }
    HttpUtil.response(response, result.toString().getBytes("utf-8"));
    return null;
  }
}
