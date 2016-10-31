/**
 * http服务器测试
 */
package org.ngame.server.test;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.ngame.server.http.NHttpServer;
import org.ngame.util.HttpUtils;

/**
 *
 * @author Beykery
 */
public class TestHttpServer extends NHttpServer implements Runnable
{

  public TestHttpServer(String context, int port, int poolSize) throws IOException
  {
    super(context, port, poolSize);
  }

  @Override
  public void handle(HttpExchange he) throws IOException
  {
    String q = null;
    URI u = he.getRequestURI();
    if (he.getRequestMethod().equalsIgnoreCase("GET"))
    {
      q = u.getQuery();
    } else
    {
      InputStream is = he.getRequestBody();
      byte[] content = HttpUtils.content(is);
      q = new String(content, "UTF-8");
    }
    Map<String,String[]> query=HttpUtils.parseQuery(q);
    System.out.println(q);
    byte[] r = "hi".getBytes("UTF-8");
    he.sendResponseHeaders(200, r.length);
    he.getResponseBody().write(r);
    he.getResponseBody().flush();
    he.close();
  }

  public static void main(String[] args) throws IOException
  {
    TestHttpServer server = new TestHttpServer("/test", 3355, 2);
    server.start();
    new Thread(server).start();
  }

  @Override
  public void run()
  {
    Map<String, Object> q = new HashMap<>();
    q.put("a", 1);
    byte[] content = HttpUtils.post("http://localhost:3355/test", "a=874343737@qq.com&b=1&b=3&b=kl".getBytes());
    System.out.println(new String(content));
  }


}
