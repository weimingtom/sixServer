/**
 * http工具
 */
package org.ocean.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author beykery
 */
public class HttpUtil
{

  private static final Logger LOG = Logger.getLogger(HttpUtil.class);

  /**
   * 发送响应
   *
   * @param response
   * @param data
   */
  public static void response(HttpServletResponse response, byte[] data)
  {
    OutputStream os = null;
    try
    {
      os = response.getOutputStream();
      response.setContentLength(data.length);
      os.write(data);
      os.flush();
    } catch (IOException ex)
    {
    } finally
    {
      if (os != null)
      {
        try
        {
          os.close();
        } catch (IOException ex1)
        {
        }
      }
    }
  }

  /**
   * 发送响应
   *
   * @param response
   * @param content
   */
  public static void response(HttpServletResponse response, String content)
  {
    try
    {
      response(response, content.getBytes("utf-8"));
    } catch (UnsupportedEncodingException ex)
    {
    }
  }

  /**
   * 读取流
   *
   * @param u
   * @param post
   * @param deskey
   * @return
   */
  public static byte[] post(String u, byte[] post, String deskey)
  {
    byte[] data = null;
    HttpURLConnection con = null;
    try
    {
      URL url = new URL(u);
      con = (HttpURLConnection) url.openConnection();
      con.setReadTimeout(3000);
      con.setConnectTimeout(3000);
      con.setRequestMethod("POST");
      con.setDoOutput(true);
      con.setDoInput(true);
      DataOutputStream dos = new DataOutputStream(con.getOutputStream());
      dos.write(deskey == null ? post : DESMD5.encrypt(post, deskey));
      dos.flush();
      DataInputStream dis = new DataInputStream(con.getInputStream());
      data = new byte[con.getContentLength()];
      dis.readFully(data);
    } catch (Exception e)
    {
      e.printStackTrace();
    } finally
    {
      if (con != null)
      {
        con.disconnect();
      }
    }
    return data;
  }

  /**
   * 以POST方式向指定地址发送数据包请求,并取得返回的数据包
   *
   * @param urlString
   * @param requestData
   * @param requestProperties
   * @return 返回的数据包
   */
  public static byte[] request(String urlString, byte[] requestData, Properties requestProperties)
  {
    byte[] responseData = null;
    HttpURLConnection con = null;
    try
    {
      URL url = new URL(urlString);
      con = (HttpURLConnection) url.openConnection();
      con.setReadTimeout(3000);
      con.setConnectTimeout(3000);
      if ((requestProperties != null) && (requestProperties.size() > 0))
      {
        for (Map.Entry<Object, Object> entry : requestProperties.entrySet())
        {
          String key = String.valueOf(entry.getKey());
          String value = String.valueOf(entry.getValue());
          con.setRequestProperty(key, value);
        }
      }
      con.setRequestMethod("POST");
      con.setDoOutput(true);
      con.setDoInput(true);
      DataOutputStream dos = new DataOutputStream(con.getOutputStream());
      dos.write(requestData);
      dos.flush();
      dos.close();
      DataInputStream dis = new DataInputStream(con.getInputStream());
      responseData = new byte[con.getContentLength()];
      dis.readFully(responseData);
      dis.close();
    } catch (Exception e)
    {
      e.printStackTrace();
    } finally
    {
      con.disconnect();
    }
    return responseData;
  }

  /**
   * get请求
   *
   * @param u
   * @param query
   * @return
   */
  public static byte[] get(String u, Map<String, Object> query)
  {
    byte[] data = null;
    HttpURLConnection con = null;
    try
    {
      StringBuilder sb = new StringBuilder(u);
      if (!query.isEmpty())
      {
        sb.append("?");
        for (Map.Entry<String, Object> en : query.entrySet())
        {
          sb.append(en.getKey()).append("=").append(en.getValue().toString()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
      }
      URL url = new URL(sb.toString());
      con = (HttpURLConnection) url.openConnection();
      con.setReadTimeout(30000);
      con.setConnectTimeout(30000);
      con.setRequestMethod("GET");
      con.setDoOutput(true);
      con.setDoInput(true);
      DataInputStream dis = new DataInputStream(con.getInputStream());
      data = new byte[con.getContentLength()];
      dis.readFully(data);
    } catch (Exception e)
    {
      e.printStackTrace();
    } finally
    {
      if (con != null)
      {
        con.disconnect();
      }
    }
    return data;
  }
}
