/**
 * 请求上下文
 */
package org.ngame.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.FastThreadLocal;
import org.apache.log4j.Logger;
import org.ngame.app.Application;
import org.ngame.protocol.annotation.Proto;
import org.ngame.server.connector.ConnectorServer;
import org.ngame.socket.NClient;
import org.ngame.socket.framing.Framedata;
import org.ngame.socket.framing.Varint32Framedata;
import org.ngame.socket.protocol.Varint32HeaderProtocol;
import org.ngame.util.MathUtil;

/**
 *
 * @author beykery
 */
public class Context
{

  private static final Logger LOG = Logger.getLogger(Context.class);
  private static final FastThreadLocal<Context> ftl = new FastThreadLocal<>();
  private BaseServer server;
  private NClient client;
  private String sessionid;
  private long routerid;
  private boolean sameProcess;//请求和处理是否在同一进程
  private Object request;
  private int cmd;
  private static Context lastContext;//最后访问的上下文

  /**
   * 构造上下文
   *
   * @param bs
   * @param so
   * @param cmd
   * @param request
   * @param routeid
   * @param sessionid
   * @param sameProcess
   */
  protected Context(BaseServer bs, NClient so, int cmd, Object request, long routeid, String sessionid, boolean sameProcess)
  {
    this.init(bs, so, cmd, request, routeid, sessionid, sameProcess);
  }

  private void init(BaseServer bs, NClient so, int cmd, Object request, long routeid, String sessionid, boolean sameProcess)
  {
    this.server = bs;
    this.client = so;
    this.request = request;
    this.sessionid = sessionid;
    this.routerid = routeid;
    this.sameProcess = sameProcess;
    this.cmd = cmd;
  }

  /**
   * server
   *
   * @return
   */
  public BaseServer getServer()
  {
    return server;
  }

  /**
   * 连接
   *
   * @return
   */
  public NClient getClient()
  {
    return client;
  }

  /**
   * 请求的信息
   *
   * @return
   */
  public Object getRequest()
  {
    return request;
  }

  /**
   * session
   *
   * @return
   */
  public String getSessionId()
  {
    return sessionid;
  }

  /**
   * routerid
   *
   * @return
   */
  public long getRouterId()
  {
    return routerid;
  }

  public boolean isSameProcess()
  {
    return sameProcess;
  }

  public int getCmd()
  {
    return cmd;
  }

  /**
   * 设置当前上下文
   *
   * @param server
   * @param conn
   * @param cmd
   * @param request
   * @param routeid
   * @param sessionid
   * @param sameProcess
   * @return
   */
  protected static Context current(BaseServer server, NClient conn, int cmd, Object request, long routeid, String sessionid, boolean sameProcess)
  {
    Context c = ftl.get();
    if (c != null)
    {
      c.init(server, conn, cmd, request, routeid, sessionid, sameProcess);
    } else
    {
      c = new Context(server, conn, cmd, request, routeid, sessionid, sameProcess);
      ftl.set(c);
    }
    lastContext = c;
    return c;
  }

  /**
   * 当前的上下文
   *
   * @return
   */
  public static Context current()
  {
    return ftl.get();
  }

  /**
   * 最后访问的上下文
   *
   * @return
   */
  public static Context last()
  {
    return lastContext;
  }

  /**
   * 发送response到客户端
   *
   * @param response
   * @param cmd
   * @return
   */
  public ChannelFuture send(Object response, int cmd)
  {
    try
    {
      Codec codec = ProtobufProxy.create(response.getClass());
      byte[] content = codec.encode(response);
      MathUtil.xor(content, Application.ks);
      LOG.info(response.getClass().getSimpleName() + "::" + content.length + "::" + JSON.toJSON(response));
      if (this.sameProcess)//直接发送
      {
        Framedata fd;
        if (this.client.getProtocol() instanceof Varint32HeaderProtocol)
        {
          fd = new Varint32Framedata(content.length + 7);
        } else
        {
          fd = new Framedata(content.length + 4);
        }
        fd.putShort(cmd);
        fd.putBytes(content);
        fd.end();
        return this.client.sendFrame(fd.getData());
      } else//寻找connector
      {
        ServerChannel sc = server.router.hashingChannel("connector", this.routerid);
        Framedata fd;
        if (this.client.getProtocol() instanceof Varint32HeaderProtocol)
        {
          fd = new Varint32Framedata(content.length + 128);
        } else
        {
          fd = new Framedata(content.length + 128);
        }
        fd.putLong(routerid);
        fd.putString(sessionid, "UTF-8");
        fd.putShort(cmd);
        fd.putByte(0);//发送给sessionid
        fd.putBytes(content);
        fd.end();
        return sc.send(fd.getData());
      }
    } catch (Exception e)
    {
      LOG.error("无法发送response：" + cmd + ":" + e.getMessage());
    }
    return null;
  }

  /**
   * 发送给指定玩家
   *
   * @param response
   * @return
   */
  public ChannelFuture send(Object response)
  {
    Proto p = response.getClass().getAnnotation(Proto.class);
    if (p != null)
    {
      return this.send(response, p.value());
    }
    return null;
  }

  /**
   * 发送给指定玩家
   *
   * @param response
   * @param pids
   * @return
   */
  public ChannelFuture send(Object response, String... pids)
  {
    Proto p = response.getClass().getAnnotation(Proto.class);
    if (p != null)
    {
      return this.send(response, p.value(), pids);
    }
    return null;
  }

  /**
   * 发送给指定玩家
   *
   * @param response
   * @param cmd
   * @param pids
   * @return
   */
  public ChannelFuture send(Object response, int cmd, String... pids)
  {
    try
    {
      Codec codec = ProtobufProxy.create(response.getClass());
      byte[] content = codec.encode(response);
      LOG.info(response.getClass().getSimpleName() + "::" + content.length + "::" + JSON.toJSON(response));
      MathUtil.xor(content, Application.ks);
      if (this.sameProcess)//直接发送
      {
        Framedata fd;
        if (this.client.getProtocol() instanceof Varint32HeaderProtocol)
        {
          fd = new Varint32Framedata(content.length + 4 + 3);
        } else
        {
          fd = new Framedata(content.length + 4);
        }
        fd.putShort(cmd);
        fd.putBytes(content);
        fd.end();
        for (String pid : pids)
        {
          NClient nc = ((ConnectorServer) server).getSocket(pid);
          if (nc != null)
          {
            nc.sendFrame(fd.duplicate().getData());
          }
        }
        fd.release(1);
      } else//寻找connector
      {
        ServerChannel sc = server.router.hashingChannel("connector", 0);//只有一个connector服务器
        Framedata fd;
        if (this.client.getProtocol() instanceof Varint32HeaderProtocol)
        {
          fd = new Varint32Framedata(content.length + 128);
        } else
        {
          fd = new Framedata(content.length + 128);
        }
        fd.putLong(routerid);
        fd.putString(sessionid, "UTF-8");
        fd.putShort(cmd);
        fd.putByte(pids.length);//数量
        for (String pid : pids)
        {
          fd.putString(pid, "UTF-8");
        }
        fd.putBytes(content);
        fd.end();
        return sc.send(fd.getData());
      }
    } catch (Exception e)
    {
      LOG.error("无法发送response:" + cmd + ":" + e.getMessage());
    }
    return null;
  }
  
  public BaseServer getBaseServer(){
	  return this.server;
  }
}
