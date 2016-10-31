/**
 * 管理用户账号
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
import org.apache.log4j.Logger;
import org.ocean.annotation.Auth;
import org.ocean.domain.User;
import org.ocean.service.UserService;
import org.ocean.util.HttpUtil;
import org.ocean.util.TimeUtil;
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
public class UserController
{

  private static final Logger LOG = Logger.getLogger(UserController.class);
  @Autowired
  private UserService userService;

  @Auth(note = "用户列表")
  @RequestMapping(value = "userList.man", method = RequestMethod.GET)
  public String userList()
  {
    return "userList";
  }

  /**
   * 添加一个用户
   *
   * @param request
   * @param response
   * @param session
   * @return
   */
  @Auth(note = "用户新增")
  @RequestMapping(value = "addUser.man", method = RequestMethod.POST)
  public String addUser(WebRequest request, HttpServletResponse response, HttpSession session)
  {
    Map<String, String[]> ps = request.getParameterMap();
    String userName = ps.get("usercode")[0];
    String userPassword = ps.get("password")[0];
    boolean testing = Boolean.parseBoolean(ps.get("testing")[0]);
    int userChannelid = Integer.parseInt(ps.get("channelId")[0]);
    String unlockTime = ps.get("unlockTime") == null ? null : ps.get("unlockTime")[0];
    Date d = unlockTime == null || unlockTime.isEmpty() ? null : TimeUtil.parse(unlockTime);
    User u = null;
    String info = userService.checkUsercodeAndPassword(userName, userPassword);
    if (info == null)
    {
      u = this.userService.createUser(userName, userPassword, userChannelid, testing, d, null, null, -1);
    }
    HttpUtil.response(response, u == null ? (info == null ? "无法创建" : info) : "1");
    return null;
  }

  @Auth(note = "用户修改")
  @RequestMapping(value = "editUser.man", method = RequestMethod.POST)
  public String editUser(WebRequest request, HttpServletResponse response, HttpSession session)
  {
    Map<String, String[]> ps = request.getParameterMap();
    String userName = ps.get("usercode")[0];
    String userPassword = ps.get("password")[0];
    int userChannelid = Integer.parseInt(ps.get("channelId")[0]);
    long id = Long.parseLong(ps.get("id")[0]);
    boolean testing = Boolean.parseBoolean(ps.get("testing")[0]);
    String unlockTime = ps.get("unlockTime") == null ? null : ps.get("unlockTime")[0];
    Date d = unlockTime == null || unlockTime.isEmpty() ? null : TimeUtil.parse(unlockTime);
    String info = userService.checkUsercodeAndPassword(userName, userPassword);
    boolean r = false;
    if (info == null)
    {
      r = this.userService.editUser(id, userName, userPassword, userChannelid, testing, d);
    }
    HttpUtil.response(response, r ? "1" : (info == null ? "无法修改" : info));
    return null;
  }

  /**
   * 删除服务器
   *
   * @param response
   * @param id
   * @return
   */
  @Auth(note = "用户删除")
  @RequestMapping(value = "deleteUser.man")
  public String deleteUser(HttpServletResponse response, @RequestParam(value = "id") long id)
  {
    boolean r = false;
    try
    {
      r = userService.deleteUser(id);
    } catch (Exception e)
    {
      LOG.error("无法删除user：" + e.getMessage());
    }
    HttpUtil.response(response, r ? "1".getBytes() : "0".getBytes());
    return null;
  }

  @RequestMapping(value = "userJsonList.man")
  public String userJsonList(HttpServletResponse response, WebRequest request) throws UnsupportedEncodingException
  {
    Map<String, String[]> ps = request.getParameterMap();
    int page = Integer.parseInt(ps.get("page")[0]);
    int rows = Integer.parseInt(ps.get("rows")[0]);
    long uid = ps.get("userId") == null || ps.get("userId")[0].trim().isEmpty() ? 0 : Long.parseLong(ps.get("userId")[0]);
    String uname = ps.get("userName") == null ? null : ps.get("userName")[0];
    JSONObject json = new JSONObject();
    if (uid > 0)//按照id查找
    {
      User u = userService.getUser(uid);
      json.put("total", u == null ? 0 : 1);
      JSONArray arr = new JSONArray();
      json.put("rows", arr);
      if (u != null)
      {
        JSONObject item = new JSONObject();
        item.put("usercode", u.getUsercode());
        item.put("password", u.getPassword());
        item.put("channelId", u.getChannelId());
        item.put("regTime", TimeUtil.format(u.getRegTime()));
        item.put("id", u.getId());
        item.put("testing", u.isTesting());
        item.put("unlockTime", u.getUnlockTime() == null ? null : TimeUtil.format(u.getUnlockTime()));
        item.put("anyChannel", u.getAnyChannel());
        item.put("anyUid", u.getAnyUid());
        arr.add(item);
      }
    } else if (uname != null && !uname.trim().isEmpty())//按照name查找
    {
      User u = userService.getUser(uname);
      json.put("total", u == null ? 0 : 1);
      JSONArray arr = new JSONArray();
      json.put("rows", arr);
      if (u != null)
      {
        JSONObject item = new JSONObject();
        item.put("usercode", u.getUsercode());
        item.put("password", u.getPassword());
        item.put("channelId", u.getChannelId());
        item.put("regTime", TimeUtil.format(u.getRegTime()));
        item.put("unlockTime", u.getUnlockTime() == null ? null : TimeUtil.format(u.getUnlockTime()));
        item.put("id", u.getId());
        item.put("testing", u.isTesting());
        arr.add(item);
      }
    } else//分页查看
    {
      List<User> list = userService.userList(page, rows);
      json.put("total", userService.userTotal());
      JSONArray arr = new JSONArray();
      json.put("rows", arr);
      for (User u : list)
      {
        JSONObject item = new JSONObject();
        item.put("usercode", u.getUsercode());
        item.put("password", u.getPassword());
        item.put("channelId", u.getChannelId());
        item.put("regTime", TimeUtil.format(u.getRegTime()));
        item.put("unlockTime", u.getUnlockTime() == null ? null : TimeUtil.format(u.getUnlockTime()));
        item.put("id", u.getId());
        item.put("testing", u.isTesting());
        arr.add(item);
      }
    }
    HttpUtil.response(response, json.toString().getBytes("UTF-8"));
    return null;
  }
}
