/**
 * 用来处理第三方的通信连接
 */
package org.ngame.server.http;

import com.sun.net.httpserver.HttpExchange;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.ngame.Spring;
import org.ngame.annotation.RiverMapping;
import org.ngame.app.Application;
import org.ngame.util.ClassUtil;
import org.ngame.util.DESMD5;
import org.ngame.util.HttpUtils;

/**
 *
 * @author beykery
 */
public class River extends NHttpServer
{

  private static final Logger LOG = Logger.getLogger(River.class);
  private final Map<String, Method> methods = new HashMap<>();//请求所对应的方法
  private final Map<Method, Object> references = new HashMap<>();//方法对应的对象
  private final Map<Method, Class[]> ps = new HashMap<>();//参数类型
  private static byte[] err;

  /**
   * 初始化mapping
   *
   * @param context
   * @param port
   * @param poolSize
   * @throws IOException
   */
  public River(String context, int port, int poolSize) throws IOException
  {
    super(context, port, poolSize);
    err = "服务器内部错误".getBytes("utf-8");
    String scanPath = System.getProperty("river.handler.scan.path");
    Set<Class<?>> set = ClassUtil.scan(scanPath);
    for (Class<?> claz : set)
    {
      Method[] ms = claz.getDeclaredMethods();
      for (Method m : ms)
      {
        RiverMapping mapping = m.getAnnotation(RiverMapping.class);
        if (mapping != null)
        {
          Method old = methods.put(mapping.value(), m);
          if (old != null)
          {
            throw new RuntimeException("重复的river mapping");
          }
          this.ps.put(m, m.getParameterTypes());
          references.put(m, Spring.bean(claz));
        }
      }
    }
  }

  @Override
  public void handle(HttpExchange he)
  {
    try
    {
      URI u = he.getRequestURI();
      String mapping = u.getPath();
      mapping = mapping.substring(mapping.indexOf('/', 1) + 1);
      ByteArrayOutputStream r = invoke(mapping, he);
      he.sendResponseHeaders(200, r.size());
      he.getResponseBody().write(r.toByteArray());
      he.getResponseBody().flush();
    } catch (Exception e)
    {
      e.printStackTrace();
      LOG.error(e);
      try
      {
        he.sendResponseHeaders(500, err.length);
        he.getResponseBody().write(err);
        he.getResponseBody().flush();
      } catch (Exception ee)
      {
      }
    } finally
    {
      he.close();
    }
  }

  /**
   * 调用处理方法
   *
   * @param mapping
   * @param query
   * @param os
   */
  private ByteArrayOutputStream invoke(String mapping, HttpExchange he) throws Exception
  {
    Method m = this.methods.get(mapping);
    if (m != null)
    {
      RiverMapping rm = m.getAnnotation(RiverMapping.class);
      if (rm.method().isEmpty() || rm.method().equalsIgnoreCase(he.getRequestMethod()))
      {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Object handler = this.references.get(m);
        Class<?>[] pts = this.ps.get(m);
        Object[] params = new Object[pts.length];
        for (int i = 0; i < pts.length; i++)
        {
          Class c = pts[i];
          if (c.equals(Map.class))//context
          {
            params[i] = getQuery(he);
          } else if (c.equals(OutputStream.class))
          {
            params[i] = baos;
          } else if (c.equals(InputStream.class))
          {
            params[i] = he.getRequestBody();
          } else
          {
            throw new IllegalArgumentException("不能包含Map和OutputStream、InputStream之外的类型");
          }
        }
        m.invoke(handler, params);
        return baos;
      } else
      {
        throw new IllegalArgumentException("请求非法");
      }
    } else//找不到处理方法
    {
      throw new IllegalArgumentException("找不到合适的处理方法");
    }
  }

  /**
   * 请求参数
   *
   * @param he
   * @return
   */
  private Map<String, String[]> getQuery(HttpExchange he) throws Exception
  {
    String q;
    URI u = he.getRequestURI();
    if (he.getRequestMethod().equalsIgnoreCase("GET"))
    {
      q = u.getQuery();
    } else//POST
    {
      InputStream is = he.getRequestBody();
      byte[] content = HttpUtils.content(is);
      q = new String(DESMD5.decrypt(content, Application.DES_KEY), "UTF-8");
    }
    return HttpUtils.parseQuery(q);
  }
}
