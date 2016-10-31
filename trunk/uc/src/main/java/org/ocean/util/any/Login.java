/**
 * anysdk 的登录验证
 */
package org.ocean.util.any;

import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.ocean.domain.Game;
import org.ocean.domain.User;
import org.ocean.service.GameService;
import org.ocean.service.UserService;
import org.ocean.Spring;

/**
 *
 * 登录验证处理类
 *
 * @author zhangjunfei
 * @date 2014-5-26 上午11:31:10
 * @version 1.0
 */
public class Login
{

  private static final Logger LOG = Logger.getLogger(Login.class);
  /**
   * anysdk统一登录地址
   */
  private static String loginCheckUrl = "http://oauth.anysdk.com/api/User/LoginOauth/";

  /**
   * connect time out
   *
   * @var int
   */
  private static int connectTimeOut = 30 * 1000;

  /**
   * time out second
   *
   * @var int
   */
  private static int timeOut = 30 * 1000;

  /**
   * user agent
   *
   * @var string
   */
  private static final String userAgent = "px v1.0";

  /**
   * 检查登录合法性及返回sdk返回的用户id或部分用户信息
   *
   * @param response
   * @param ps
   * @return 验证合法 返回true 不合法返回 false
   */
  public static boolean check(HttpServletResponse response, Map<String, String> ps)
  {
    try
    {
      Map<String, String> params = ps;
      //检测必要参数
      if (parametersIsset(params))
      {
        sendToClient(response, "parameter not complete");
        return false;
      }
      String queryString = getQueryString(ps);
      LOG.error("客户端的登陆请求：" + queryString);
      URL url = new URL(loginCheckUrl);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestProperty("User-Agent", userAgent);
      conn.setReadTimeout(timeOut);
      conn.setConnectTimeout(connectTimeOut);
      conn.setRequestMethod("POST");
      conn.setDoInput(true);
      conn.setDoOutput(true);

      OutputStream os = conn.getOutputStream();
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
      writer.write(queryString);
      writer.flush();
      tryClose(writer);
      tryClose(os);
      conn.connect();

      InputStream is = conn.getInputStream();
      String result = stream2String(is);
      LOG.error("anysdk服务器的登陆返回：" + result);
      JSONObject json = JSONObject.parseObject(result);
      String status = json.getString("status");
      if ("ok".equalsIgnoreCase(status))//登陆成功，检查本地是否存在账号，否则注册一个，写回ext
      {
        JSONObject common = json.getJSONObject("common");
        String channel = common.getString("channel");
        String uid = common.getString("uid");
        String channelid = common.getString("server_id");//客户端调用带serverId的重载登录函数
        JSONObject ext = new JSONObject();
        json.put("ext", ext);
        UserService us = Spring.bean(UserService.class);
        int cid = Integer.parseInt(channelid);
        GameService gs = Spring.bean(GameService.class);
        Game g = gs.getGameByChannelid(cid);
        User u = us.anyLogin(channel, uid, cid, g.getId());
        if (u != null)
        {
          ext.put("loginId", u.getLoginId(g.getDesKey()));
        } else
        {
          ext.put("error", "登录失败");
        }
        LOG.error("返回给客户端的json:" + json.toString());
      }
      sendToClient(response, json.toString());
      return true;
    } catch (Exception e)
    {
      LOG.error("anysdk的check异常" + e.getMessage());
      e.printStackTrace();
    }
    sendToClient(response, "Unknown error!");
    return false;
  }

  public static void setLoginCheckUrl(String loginCheckUrl)
  {
    Login.loginCheckUrl = loginCheckUrl;
  }

  /**
   * 设置连接超时
   *
   * @param connectTimeOut
   */
  public static void setConnectTimeOut(int connectTimeOut)
  {
    Login.connectTimeOut = connectTimeOut;
  }

  /**
   * 设置超时时间
   *
   * @param timeOut
   */
  public static void setTimeOut(int timeOut)
  {
    Login.timeOut = timeOut;
  }

  /**
   * check needed parameters isset 检查必须的参数 channel
   * uapi_key：渠道提供给应用的app_id或app_key（标识应用的id）
   * uapi_secret：渠道提供给应用的app_key或app_secret（支付签名使用的密钥）
   *
   * @param params
   * @return boolean
   */
  private static boolean parametersIsset(Map<String, String> params)
  {
    return !(params.containsKey("channel") && params.containsKey("uapi_key")
            && params.containsKey("uapi_secret"));
  }

  /**
   * 获取查询字符串
   *
   * @param request
   * @return
   */
  private static String getQueryString(Map<String, String> ps)
  {
    Map<String, String> params = ps;
    StringBuilder queryString = new StringBuilder();
    for (String key : params.keySet())
    {
      String v = params.get(key);
      queryString.append(key).append("=").append(v).append("&");
    }
    queryString.setLength(queryString.length() - 1);
    return queryString.toString();
  }

  /**
   * 获取流中的字符串
   *
   * @param is
   * @return
   */
  private static String stream2String(InputStream is)
  {
    BufferedReader br = null;
    try
    {
      br = new BufferedReader(new java.io.InputStreamReader(is));
      String line;
      StringBuilder sb = new StringBuilder();
      while ((line = br.readLine()) != null)
      {
        sb.append(line);
      }
      return sb.toString();
    } catch (Exception e)
    {
      e.printStackTrace();
    } finally
    {
      tryClose(br);
    }
    return "";
  }

  /**
   * 向客户端应答结果
   *
   * @param response
   * @param content
   */
  private static void sendToClient(HttpServletResponse response, String content)
  {
    response.setContentType("text/plain;charset=utf-8");
    try
    {
      PrintWriter writer = response.getWriter();
      writer.write(content);
      writer.flush();
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   * 关闭输出流
   *
   * @param os
   */
  private static void tryClose(OutputStream os)
  {
    try
    {
      if (null != os)
      {
        os.close();
        os = null;
      }
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   * 关闭writer
   *
   * @param writer
   */
  private static void tryClose(java.io.Writer writer)
  {
    try
    {
      if (null != writer)
      {
        writer.close();
        writer = null;
      }
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   * 关闭Reader
   *
   * @param reader
   */
  private static void tryClose(java.io.Reader reader)
  {
    try
    {
      if (null != reader)
      {
        reader.close();
        reader = null;
      }
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
