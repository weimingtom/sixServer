/**
 * 拥有两个端口的server
 */
package org.ngame.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.bjf.remoting.protobuf.Codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.timeout.IdleStateEvent;

import org.ngame.app.Application;
import org.ngame.protocol.test.VersionRequest;
import org.ngame.socket.NClient;
import org.ngame.socket.NServer;
import org.ngame.socket.framing.Framedata;
import org.ngame.socket.protocol.LVProtocol;
import org.ngame.socket.protocol.Protocol;
import org.ngame.util.IOUtil;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.ngame.Spring;
import org.ngame.script.ScriptEngine;
import org.ngame.server.connector.ConnectorServer;
import org.ngame.socket.framing.Varint32Framedata;
import org.ngame.socket.protocol.Varint32HeaderProtocol;
import org.ngame.util.MathUtil;

/**
 * @author Beykery
 */
public class BaseServer
{

  private static final Logger LOG = Logger.getLogger(BaseServer.class);
  protected int readerIdle;
  protected int writerIdle;
  protected int allIdle;
  protected int clientFrameSize;
  public static final File protocol = new File("./script/protocol.groovy");
  protected String serverType;
  protected NServer inner;
  protected NServer outer;
  protected final Router router;
  protected EventLoopGroup bossGroup;
  protected EventLoopGroup workerGroup;
  protected String name;// 服务器name
  protected boolean autoTrans;
  public static final int FROM_INNER = 0;//内部链接
  public static final int FROM_OUTER = 1;//客户端
  public static final int FROM_LOCAL = 2;//本地连接
  protected final ScriptEngine engine;

  /**
   * 构造一个两端口server
   *
   * @param port
   * @param clientPort
   * @throws java.lang.ClassNotFoundException
   */
  public BaseServer(int port, int clientPort) throws ClassNotFoundException
  {
    engine = Spring.bean(ScriptEngine.class);
    this.router = new Router(this);
    try
    {
      readerIdle = Integer.parseInt(System.getProperty("game.socket.client.idle.read"));
      writerIdle = Integer.parseInt(System.getProperty("game.socket.client.idle.write"));
      allIdle = Integer.parseInt(System.getProperty("game.socket.client.idle.all"));
      clientFrameSize = Integer.parseInt(System.getProperty("game.socket.protocol.maxFrameSize"));
    } catch (NumberFormatException e)
    {
      LOG.error("idle设置失败");
    }
    if (port > 0)
    {
      this.inner = new NServer(new InetSocketAddress("localhost", port), NServer.NETWORK_SOCKET)
      {

        @Override
        protected void preStop()
        {
          BaseServer.this.preStop(FROM_INNER);
        }

        @Override
        public void onOpen(NClient conn)
        {
          BaseServer.this.onOpen(conn, FROM_INNER);
        }

        @Override
        public void onClose(NClient conn, boolean local)
        {
          BaseServer.this.onClose(conn, local, FROM_INNER);
        }

        @Override
        public void onMessage(NClient conn, Object message)//服务器之间的消息，
        {
          BaseServer.this.onMessage(conn, message, FROM_INNER);
        }

        @Override
        public void onError(NClient conn, Throwable ex)
        {
          BaseServer.this.onError(conn, ex, FROM_INNER);
        }

        @Override
        public void onIdle(NClient conn, IdleStateEvent event)
        {
          BaseServer.this.onIdle(conn, event, FROM_INNER);
        }

        @Override
        public void onBusy(NClient conn)
        {
          BaseServer.this.onBusy(conn, FROM_INNER);
        }
      };
      this.inner.setProtocol(Varint32HeaderProtocol.class);
    }
    if (clientPort > 0)
    {
      this.outer = new NServer(new InetSocketAddress("localhost", clientPort), NServer.NETWORK_SOCKET)
      {

        @Override
        protected void preStop()
        {
          BaseServer.this.preStop(FROM_OUTER);
        }

        @Override
        public void onOpen(NClient conn)
        {
          BaseServer.this.onOpen(conn, FROM_OUTER);
        }

        @Override
        public void onClose(NClient conn, boolean local)
        {
          BaseServer.this.onClose(conn, local, FROM_OUTER);
        }

        @Override
        public void onMessage(NClient conn, Object message)
        {
          BaseServer.this.onMessage(conn, message, FROM_OUTER);
        }

        @Override
        public void onError(NClient conn, Throwable ex)
        {
          BaseServer.this.onError(conn, ex, FROM_OUTER);
        }

        @Override
        public void onIdle(NClient conn, IdleStateEvent event)
        {
          BaseServer.this.onIdle(conn, event, FROM_OUTER);
        }

        @Override
        public void onBusy(NClient conn)
        {
          BaseServer.this.onBusy(conn, FROM_OUTER);
        }
      };
      this.outer.setProtocol(Application.protocol);
    }
  }

  /**
   * 服务器类型
   *
   * @param serverType
   */
  public void setServerType(String serverType)
  {
    this.serverType = serverType;
  }

  /**
   * 服务器类型设置
   *
   * @return
   */
  public String getServerType()
  {
    return serverType;
  }

  public boolean isAutoTrans()
  {
    return autoTrans;
  }

  public void setAutoTrans(boolean autoTrans)
  {
    this.autoTrans = autoTrans;
  }

  /**
   * 工作线程池
   *
   * @return
   */
  public EventLoopGroup getWorkerGroup()
  {
    return workerGroup;
  }

  /**
   * 内存
   *
   * @return
   */
  public long getMemoryTotal()
  {
    return Runtime.getRuntime().totalMemory();
  }

  /**
   * 剩余
   *
   * @return
   */
  public long getFreeMemory()
  {
    return Runtime.getRuntime().freeMemory();
  }

  /**
   * gc一次
   */
  public void gc()
  {
    System.gc();
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  /**
   * 启动服务器
   *
   * @param bossGroup
   * @param workerGroup
   * @throws InterruptedException
   */
  public void start(EventLoopGroup bossGroup, EventLoopGroup workerGroup) throws InterruptedException
  {
    this.bossGroup = bossGroup;
    this.workerGroup = workerGroup;
    if (this.inner != null)
    {
      this.inner.start(bossGroup, workerGroup);
    }
    if (this.outer != null)
    {
      this.outer.start(bossGroup, workerGroup);
    }
  }

  public Router getRouter()
  {
    return router;
  }

  /**
   * 动态调用方法
   *
   * @param conn
   * @param json
   */
  private Object invoke(NClient conn, String method, int cmd, Object request, long routeid, String sessionid, boolean sameProcess) throws Exception
  {
    int index = method.lastIndexOf('.');
    String className = method.substring(0, index);
    String mapping = method.substring(index + 1);
    Representation re = Representation.get(className);
    return re.invoke(mapping, this, conn, cmd, request, routeid, sessionid, sameProcess);
  }

  /**
   * outer服务器
   *
   * @return
   */
  public NServer getOuter()
  {
    return outer;
  }

  /**
   * inner服务器
   *
   * @return
   */
  public NServer getInner()
  {
    return inner;
  }

  /**
   * 停服
   *
   * @param from
   */
  protected void preStop(int from)
  {
    LOG.error("即将停止 " + from + " 端口");
  }

  /**
   * 连接到来
   *
   * @param conn
   * @param from
   */
  public void onOpen(NClient conn, int from)
  {
    LOG.info(from == FROM_INNER ? "服务器连接到来" : ("客户端连接到来：" + conn));
    if (from == FROM_OUTER)
    {
      conn.idle(readerIdle, writerIdle, allIdle, TimeUnit.SECONDS);// 客户端访问服务器设置idle过期时间
      Protocol p = conn.getProtocol();
      if (p instanceof LVProtocol)
      {
        LVProtocol lv = (LVProtocol) p;
        lv.setMaxFrameSize(clientFrameSize);
      }
    }
  }

  /**
   * 连接关闭
   *
   * @param conn
   * @param local
   * @param from
   */
  public void onClose(NClient conn, boolean local, int from)
  {
    LOG.info(String.format("%s%s%s:%s", from == FROM_OUTER ? "客户端连接关闭：" : "服务器连接断开：", conn, conn.getCloseReason(), local));
  }

  /**
   * 消息
   *
   * @param conn
   * @param message
   * @param from
   */
  public void onMessage(NClient conn, Object message, int from)
  {
    ByteBuf bb = (ByteBuf) message;
    bb.readerIndex(conn.getProtocol().headerLen());
    try
    {
      switch (from)
      {
        case FROM_LOCAL://本地消息   
        case FROM_INNER://服务器消息
          long routeid = bb.readLong();
          String sessionid = IOUtil.readString(bb);
          int cmd = bb.readShort();
          if (cmd > 0)//需要调用本地服务的
          {
            Map<Integer, List<String>> p = (Map<Integer, List<String>>) engine.getProperty(protocol, "p");
            List<String> r = p.get(cmd);
            String sType = r.get(0);
            String path = r.get(1);
            Object request = parseRequest(bb, cmd);
            if (request != null)
            {
              invoke(conn, path, cmd, request, routeid, sessionid, false);
            } else
            {
              LOG.error(String.format("尚未实现的协议%d", cmd));
            }
          } else if (cmd < 0)//已经处理完毕，需要发送给客户端
          {
            List<String> sl = null;
            int target = bb.readByte();//0表示发送给sessionid；-1表示发送给所有人；-2表示发送给公会；大于0表示发送给指定的列表
            if (target > 0)//取发送列表
            {
              sl = new ArrayList<>(target);
              for (int i = 0; i < target; i++)
              {
                sl.add(IOUtil.readString(bb));
              }
            }
            ((ConnectorServer) this).sendToClient(sessionid, cmd, target, sl, bb);
          }
          break;
        case FROM_OUTER://客户端消息
          cmd = bb.readShort();//cmd
          if (cmd > 0)
          {
            Map<Integer, List<String>> p = (Map<Integer, List<String>>) engine.getProperty(protocol, "p");
            List<String> r = p.get(cmd);
            String sType = r.get(0);
            String path = r.get(1);
            routeid = (Long) conn.getSession(Router.ROUTER_KEY, 0L);
            sessionid = conn.getSessionId();
            if (sType.equals(serverType) || inSameProcess(sType, routeid))
            {
              Object request = parseRequest(bb, cmd);
              if (request != null)
              {
                LOG.info("请求 " + cmd + " " + request.getClass().getSimpleName() + ":" + JSON.toJSON(request));
                invoke(conn, path, cmd, request, routeid, sessionid, true);
              } else
              {
                LOG.error(String.format("尚未实现的协议：%d", cmd));
              }
            } else if (this.autoTrans)//转发 
            {
              Framedata fd;
              ServerChannel sc = BaseServer.this.router.hashingChannel(sType, routeid);
              if (conn.getProtocol() instanceof Varint32HeaderProtocol)
              {
                fd = new Varint32Framedata(512);
              } else
              {
                fd = new Framedata(512);
              }
              fd.putLong(routeid);
              fd.putString(sessionid, "UTF-8");
              bb.readerIndex(conn.getProtocol().headerLen());
              fd.putBytes(bb);
              fd.end();
              sc.send(fd.getData());
            } else//客户端想要调用非法操作，阻断它 
            {
              conn.close("客户端想要调用非法操作");
            }
          } else
          {
            conn.close("客户端发送非法消息：" + cmd);
          }
          break;
        default:
          throw new RuntimeException("错误的消息来源");
      }
    } catch (Exception e)
    {
      e.printStackTrace();
      conn.close(e.getMessage());
    } finally
    {
      if (bb.refCnt() > 0)
      {
        bb.release(bb.refCnt());
      }
    }
  }

  /**
   * error
   *
   * @param conn
   * @param ex
   * @param from
   */
  public void onError(NClient conn, Throwable ex, int from)
  {
//    ex.printStackTrace();
    LOG.error(String.format("%s%s", from == FROM_OUTER ? "客户端连接error:" : "服务器连接error:", conn));
    conn.close("连接错误：" + ex.getMessage());
  }

  /**
   * idle
   *
   * @param conn
   * @param event
   * @param from
   */
  public void onIdle(NClient conn, IdleStateEvent event, int from)
  {
    LOG.error(String.format("%s%s", from == FROM_OUTER ? "客户端连接idle:" : "服务器连接idle:", conn));
    conn.close("连接超时");
  }

  /**
   * 繁忙
   *
   * @param conn
   * @param from
   */
  public void onBusy(NClient conn, int from)
  {
    if (from == FROM_OUTER)
    {
      LOG.error(String.format("%s%s", from == FROM_OUTER ? "客户端连接频繁:" : "服务器连接频繁:", conn));
      conn.close("连接频繁");
    }
  }

  /**
   * 停服
   *
   * @throws java.lang.InterruptedException
   */
  public void stop() throws InterruptedException
  {
    if (this.outer != null)
    {
      this.outer.stop().sync();
    }
    if (this.inner != null)
    {
      inner.stop().sync();
    }
  }

  /**
   * 目标服务是否也在本地进程
   *
   * @param serverType
   * @param rid
   * @return
   */
  private boolean inSameProcess(String serverType, long rid)
  {
    InetSocketAddress add = this.router.hashingAddress(serverType, rid);
    return add == null || add.getAddress().getHostAddress().equals(Application.localIP);
  }

  /**
   * 解析request对象
   *
   * @param bb
   * @param name
   * @return
   */
  private Object parseRequest(ByteBuf bb, int cmd) throws IOException
  {
    Codec codec = Application.getCodec(cmd);
    if (codec != null)
    {
      byte[] content = new byte[bb.readableBytes()];
      bb.readBytes(content);
      MathUtil.xor(content, Application.ks);
      return codec.decode(content);
    }
    return null;
  }
  
  
  public static void printHexString(byte[] b) { 
	  for (int i = 0; i < b.length; i++) { 
	  String hex = Integer.toHexString(b[i] & 0xFF); 
	  if (hex.length() == 1) { 
	  hex = '0' + hex; 
	  } 
	  System.out.print("0x"+hex.toUpperCase()+","); 
	  } 

	  }
}
