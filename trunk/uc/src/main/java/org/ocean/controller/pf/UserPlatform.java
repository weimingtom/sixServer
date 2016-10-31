/**
 * 用户相关
 */
package org.ocean.controller.pf;

import com.alibaba.fastjson.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.UrlBase64;
import org.ocean.domain.Game;
import org.ocean.domain.LoginHistory;
import org.ocean.domain.User;
import org.ocean.service.GameService;
import org.ocean.service.UserService;
import org.ocean.util.DESMD5;
import org.ocean.util.ExceptionUtil;
import org.ocean.util.HttpUtil;
import org.ocean.util.RequestUtil;
import org.ocean.Spring;
import org.ocean.util.StringUtil;
import org.ocean.util.any.Login;
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
public class UserPlatform
{

  private static final Logger LOG = Logger.getLogger(UserPlatform.class);
  @Autowired
  private UserService us;
  @Autowired
  private GameService gs;

  /**
   * 游客登录
   *
   * @param request
   * @param response
   * @return
   * @throws java.io.UnsupportedEncodingException
   */
  @RequestMapping(value = "guestLogin.pf")
  public String guestLogin(WebRequest request, HttpServletResponse response) throws UnsupportedEncodingException, Exception
  {
    JSONObject json = new JSONObject();
    Map<String, String[]> m = request.getParameterMap();
    int channelId = RequestUtil.getIntParam(m, "channelid", -1);
    String loginId = RequestUtil.getStringParam(m, "loginid", null);
    Game g = gs.getGameByChannelid(channelId);
    if (g != null)
    {
      User u = null;
      if (loginId != null)
      {
        JSONObject loginJson = JSONObject.parseObject(new String(DESMD5.decrypt(UrlBase64.decode(loginId.getBytes("UTF-8")), g.getDesKey())));
        long ucenterId = loginJson.getLong("id");
        boolean testing = loginJson.getBooleanValue("test");
        String usercode = loginJson.getString("usercode");
        u = us.getUser(ucenterId);
      } else
      {
        u = us.createGuest(channelId);
      }
      if (u != null)
      {
        json.put("successFlag", true);
        json.put("loginId", u.getLoginId(g.getDesKey()));
      } else
      {
        json.put("successFlag", false);
        json.put("message", Spring.message("error_unknown"));
      }
    } else
    {
      json.put("successFlag", false);
      json.put("message", Spring.message("game_no_exist"));
    }
    HttpUtil.response(response, json.toString().getBytes("UTF-8"));
    return null;
  }

  /**
   * 注册新号
   *
   * @param request
   * @param response
   * @return
   * @throws UnsupportedEncodingException
   */
  @RequestMapping(value = "userregister.pf")
  public String regist(WebRequest request, HttpServletResponse response) throws UnsupportedEncodingException
  {
    Map<String, String[]> m = request.getParameterMap();
    String userCode = RequestUtil.getStringParam(m, "usercode", "");
    String password = RequestUtil.getStringParam(m, "password", "");
    int channelId = RequestUtil.getIntParam(m, "channelid", -1);
    String phoneMsg = RequestUtil.getStringParam(m, "phonemsg", null);
    String params = RequestUtil.getStringParam(m, "params", null);
    String code = us.checkUserCodeValidity(userCode);
    code = code == null ? us.checkPasswordValidity(password) : code;
    JSONObject json = new JSONObject();
    if (code == null)//合法的
    {
      Game g = this.gs.getGameByChannelid(channelId);
      if (g != null)
      {
        User user = null;
        try
        {
          user = us.createUser(userCode, password, channelId, false, null, phoneMsg, params, g.getId());
        } catch (Exception e)
        {
          LOG.error(ExceptionUtil.stackTrace(e));
        }
        if (user != null)
        {
          json.put("loginid", user.getLoginId(g.getDesKey()));
          json.put("successFlag", true);
        } else
        {
          json.put("message", Spring.message("user_usercode_exists"));
          json.put("successFlag", false);
        }
      } else
      {
        json.put("message", Spring.message("game_no_exist"));
        json.put("successFlag", false);
      }
    } else//非法
    {
      json.put("message", code);
      json.put("successFlag", false);
    }
    HttpUtil.response(response, json.toString().getBytes("UTF-8"));
    return null;
  }

  /**
   * 用户名密码登陆
   *
   * @param request
   * @param response
   * @return
   * @throws UnsupportedEncodingException
   */
  @RequestMapping(value = "login.pf")
  public String login(WebRequest request, HttpServletResponse response) throws UnsupportedEncodingException
  {
    Map<String, String[]> m = request.getParameterMap();
    String userCode = RequestUtil.getStringParam(m, "usercode", "");
    String password = RequestUtil.getStringParam(m, "password", null);
    int channelId = RequestUtil.getIntParam(m, "channelid", -1);
    JSONObject json = new JSONObject();
    Game g = this.gs.getGameByChannelid(channelId);
    if (g != null)
    {
      User user = us.userLogin(userCode, password);
      if (user == null)
      {
        json.put("message", Spring.message("user_is_notfind"));
        json.put("successFlag", false);
      } else if (user.getUnlockTime() != null && user.getUnlockTime().getTime() > System.currentTimeMillis())
      {
        json.put("message", Spring.message("user_is_locked"));
        json.put("successFlag", false);
      } else
      {
        json.put("successFlag", true);
        json.put("loginid", user.getLoginId(g.getDesKey()));
      }
    } else
    {
      json.put("message", Spring.message("game_no_exist"));
      json.put("successFlag", false);
    }
    HttpUtil.response(response, json.toString().getBytes("UTF-8"));
    return null;
  }

  /**
   * anysdk登陆
   *
   * @param request
   * @param response
   * @return
   * @throws UnsupportedEncodingException
   */
  @RequestMapping(value = "anylogin.pf")
  public String anyLogin(WebRequest request, HttpServletResponse response) throws UnsupportedEncodingException
  {
    Map<String, String[]> ps = request.getParameterMap();
    Map<String, String> pss = new HashMap<>();
    for (Map.Entry<String, String[]> en : ps.entrySet())
    {
      pss.put(en.getKey(), en.getValue()[0]);
    }
    Login.check(response, pss);
    return null;
  }

  /**
   * 玩家登陆进游戏之后记录进最近登陆列表
   *
   * @param request
   * @param response
   * @return
   * @throws UnsupportedEncodingException
   */
  @SuppressWarnings("null")
  @RequestMapping(value = "playerLoginGameServer.pf")
  public String playerLoginGameServer(WebRequest request, HttpServletResponse response) throws UnsupportedEncodingException
  {
    long ucenterId = StringUtil.getLongParameter(request, "uid");
    int serverId = StringUtil.getIntParameter(request, "sid");
    int gameId = StringUtil.getIntParameter(request, "gid");

    //查找是否有   有更新时间  无添加
    LoginHistory l = us.checkLoginHistory(ucenterId, gameId, serverId);
    if (l == null)
    {
      l = new LoginHistory();
      l.setUid(ucenterId);
      l.setSid(serverId);
      l.setGid(gameId);
    }
    l.setLastTime(new Date());
    us.updateLoginHistory(l);
    // 日志 TODO
    HttpUtil.response(response, "ok");
    return null;
  }

  /**
   * 灌篮登陆log
   *
   * @param request
   * @param response
   * @return
   * @throws UnsupportedEncodingException
   */
  @RequestMapping(value = "sdPlayerLoginGameServer.pf")
  public String sdPlayerLoginGameServer(WebRequest request, HttpServletResponse response) throws UnsupportedEncodingException
  {
    String ucode = StringUtil.getParameter(request, "code");
    int serverId = StringUtil.getIntParameter(request, "sid");
    int gameId = StringUtil.getIntParameter(request, "gid");
    User user = us.getUser(ucode);
    if (user != null)
    {
      //查找是否有   有更新时间  无添加
      LoginHistory l = us.checkLoginHistory(user.getId(), gameId, serverId);
      if (l == null)
      {
        l = new LoginHistory();
        l.setUid(user.getId());
        l.setSid(serverId);
        l.setGid(gameId);
      }
      l.setLastTime(new Date());
      us.updateLoginHistory(l);
    }
    // 日志 TODO
    HttpUtil.response(response, "ok");
    return null;
  }

  /**
   * 灌篮登陆
   *
   * @param request
   * @param response
   * @return
   * @throws UnsupportedEncodingException
   */
  @RequestMapping(value = "sdLogin.pf")
  public String sdLogin(WebRequest request, HttpServletResponse response) throws UnsupportedEncodingException
  {
    Map<String, String[]> m = request.getParameterMap();
    String userCode = RequestUtil.getStringParam(m, "username", "");
    String password = RequestUtil.getStringParam(m, "password", null);
    JSONObject json = new JSONObject();
    User user = us.userLogin(userCode, password);
    if (user == null)
    {
      if (us.getUser(userCode) == null)
      {
        json.put("msg", Spring.message("user_is_notfind"));
      } else
      {
        json.put("msg", Spring.message("user_password_error"));
      }
      json.put("flag", "null");
    } else if (user.getUnlockTime() != null && user.getUnlockTime().getTime() > System.currentTimeMillis())
    {
      json.put("msg", Spring.message("user_is_locked"));
      json.put("flag", "null");
    } else
    {
      json.put("flag", "f0");
      json.put("url", "null");
      json.put("token", user.getLoginId("421w6tW1ivg="));
    }
    HttpUtil.response(response, json.toString().getBytes("UTF-8"));
    return null;
  }

  /**
   * 灌篮注册
   *
   * @param request
   * @param response
   * @return
   * @throws UnsupportedEncodingException
   */
  @RequestMapping(value = "sdRegist.pf")
  public String sdRegist(WebRequest request, HttpServletResponse response) throws UnsupportedEncodingException
  {
    Map<String, String[]> m = request.getParameterMap();
    String userCode = RequestUtil.getStringParam(m, "username", "");
    String password = RequestUtil.getStringParam(m, "password", "");
    int channelId = RequestUtil.getIntParam(m, "channelid", -1);
    String phoneMsg = RequestUtil.getStringParam(m, "phonemsg", null);
    String params = RequestUtil.getStringParam(m, "params", null);
    String code = us.checkUserCodeValidity(userCode);
    code = code == null ? us.checkPasswordValidity(password) : code;
    JSONObject json = new JSONObject();
    if (code == null)//合法的
    {
      User user = null;
      try
      {
        user = us.createUser(userCode, password, channelId, false, null, phoneMsg, params, 2);
      } catch (Exception e)
      {
        LOG.error(ExceptionUtil.stackTrace(e));
      }
      if (user != null)
      {
        json.put("flag", "f0");
        json.put("url", "null");
        json.put("token", user.getLoginId("421w6tW1ivg="));
      } else
      {
        json.put("msg", Spring.message("user_usercode_exists"));
        json.put("flag", "null");
      }
    } else//非法
    {
      json.put("msg", code);
      json.put("flag", "null");
    }
    HttpUtil.response(response, json.toString().getBytes("UTF-8"));
    return null;
  }
}
