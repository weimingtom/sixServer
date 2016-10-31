/**
 * 连接器（用来处理前端连接）
 */
package org.ngame.server.connector;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.ngame.app.Application;
import org.ngame.protocol.annotation.Proto;
import org.ngame.server.BaseServer;
import org.ngame.socket.NClient;
import org.ngame.socket.framing.Framedata;
import org.ngame.socket.framing.Varint32Framedata;
import org.ngame.socket.protocol.Varint32HeaderProtocol;
import org.ngame.util.MathUtil;

import com.alibaba.fastjson.JSON;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;

/**
 * 转发、处理玩家在backend服务器间切换、可响应rpc调用
 *
 * @author beykery
 */
public class ConnectorServer extends BaseServer
{

  private static final Logger LOG = Logger.getLogger(ConnectorServer.class);
  protected final Map<String, NClient> connections;// 保存所有连接

  /**
   * 连接器
   *
   * @param port
   * @param clientPort
   * @throws java.lang.ClassNotFoundException
   */
  public ConnectorServer(int port, int clientPort) throws ClassNotFoundException
  {
    super(port, clientPort);
    connections = new ConcurrentHashMap<>();
  }

  /**
   * 发送给所有客户端
   *
   * @param fd
   */
  public void sendFrameToAll(Framedata fd)
  {
    for (NClient s : connections.values())
    {
      s.sendFrame(fd.duplicate());
    }
    fd.release(1);
  }

  /**
   * 发送给所有客户端
   *
   * @param message
   */
  public void sendFrameToAll(ByteBuf message)
  {
    for (NClient s : connections.values())
    {
      s.sendFrame(message.retain().duplicate());
    }
    message.release();
  }

  /**
   * 玩家登陆进来（connector服务器需要记录玩家session）
   *
   * @param conn
   */
  public void login(NClient conn)
  {
    final String session = conn.getSessionId();
    if (session != null)
    {
      NClient old = this.connections.put(session, conn);
      if (old != null)
      {
        old.setSessionId(null);
        old.close("被踢出");
      }
    } else
    {
      LOG.warn("尚未登录");
    }
  }

  /**
   * 重载onclose，实现客户端连接管理
   *
   * @param conn
   * @param local
   */
  @Override
  public void onClose(NClient conn, boolean local, int from)
  {
    super.onClose(conn, local, from);
    if (from == FROM_OUTER)
    {
      final String session = conn.getSessionId();
      if (session != null)
      {
        this.connections.remove(session);
      }
    }
  }

  /**
   * 发送消息给客户端（connector转发给cli）
   *
   * @param fd
   * @param sid
   * @return
   */
  public ChannelFuture sendFrame(Framedata fd, String sid)
  {
    NClient socket = connections.get(sid);
    if (socket != null)
    {
      return socket.sendFrame(fd);
    } else
    {
      fd.release();
      LOG.error("玩家：" + sid + "未登录");
    }
    return null;
  }

  /**
   * 发送一帧
   *
   * @param message
   * @param sid
   * @return
   */
  public ChannelFuture sendFrame(ByteBuf message, String sid)
  {
    NClient socket = connections.get(sid);
    if (socket != null)
    {
      return socket.sendFrame(message);
    } else
    {
      message.release();
      LOG.error("玩家：" + sid + "未登录");
    }
    return null;
  }

  /**
   * 发送给玩家消息
   *
   * @param response
   * @param pids
   */
  public void send(Object response, String... pids)
  {
    try
    {
      int cmd = response.getClass().getAnnotation(Proto.class).value();
      Codec codec = ProtobufProxy.create(response.getClass());
      byte[] content = codec.encode(response);
      MathUtil.xor(content, Application.ks);
      LOG.info(response.getClass().getSimpleName() + "::" + content.length + "::" + JSON.toJSON(response));
      Framedata fd;
      if (Application.protocol.equals(Varint32HeaderProtocol.class))
      {
        fd = new Varint32Framedata(content.length + 7);
      } else
      {
        fd = new Framedata(content.length + 4);
      }
      fd.putShort(cmd);
      fd.putBytes(content);
      fd.end();
      for (String pid : pids)
      {
        NClient client = connections.get(pid);
        if (client != null)
        {
          client.sendFrame(fd.getData().duplicate());
        }
      }
      fd.release(1);
    } catch (Exception e)
    {
      LOG.error("无法发送response：" + ":" + response.getClass());
    }
  }

  /**
   * 发送给玩家消息
   *
   * @param response
   * @param pid
   * @return
   */
  public ChannelFuture send(Object response, String pid)
  {
    NClient client = connections.get(pid);
    if (client != null)
    {
      try
      {
        int cmd = response.getClass().getAnnotation(Proto.class).value();
        Codec codec = ProtobufProxy.create(response.getClass());
        byte[] content = codec.encode(response);
        MathUtil.xor(content, Application.ks);
        LOG.info(response.getClass().getSimpleName() + "::" + content.length + "::" + JSON.toJSON(response));
        Framedata fd;
        if (client.getProtocol() instanceof Varint32HeaderProtocol)
        {
          fd = new Varint32Framedata(content.length + 7);
        } else
        {
          fd = new Framedata(content.length + 4);
        }
        fd.putShort(cmd);
        fd.putBytes(content);
        fd.end();
        return client.sendFrame(fd.getData());
      } catch (Exception e)
      {
        LOG.error("无法发送response：" + ":" + response.getClass());
      }
    }
    return null;
  }

  public ChannelFuture send(Object response, NClient client)
  {
    if (client != null)
    {
      try
      {
        int cmd = response.getClass().getAnnotation(Proto.class).value();
        Codec codec = ProtobufProxy.create(response.getClass());
        byte[] content = codec.encode(response);
        MathUtil.xor(content, Application.ks);
        LOG.info(response.getClass().getSimpleName() + "::" + content.length + "::" + JSON.toJSON(response));
        Framedata fd;
        if (client.getProtocol() instanceof Varint32HeaderProtocol)
        {
          fd = new Varint32Framedata(content.length + 7);
        } else
        {
          fd = new Framedata(content.length + 4);
        }
        fd.putShort(cmd);
        fd.putBytes(content);
        fd.end();
        return client.sendFrame(fd.getData());
      } catch (Exception e)
      {
        LOG.error("无法发送response：" + ":" + response.getClass());
      }
    }
    return null;
  }

  /**
   * 是否已经登录
   *
   * @param id
   * @return
   */
  public boolean isLogin(String id)
  {
    return connections.get(id) != null;
  }

  /**
   * 返回客户端连接
   *
   * @param id
   * @return
   */
  public NClient getSocket(String id)
  {
    return connections.get(id);
  }

  public Map<String, NClient> connections()
  {
    return connections;
  }

  /**
   * 发送
   *
   * @param sessionid
   * @param cmd
   * @param target
   * @param sl
   * @param bb
   */
  public void sendToClient(String sessionid, int cmd, int target, List<String> sl, ByteBuf bb)
  {
    Framedata fd = new Framedata(bb.readableBytes() + 4);
    fd.putShort(cmd);
    fd.putBytes(bb);
    fd.end();
    if (sl != null)
    {
      for (String cid : sl)
      {
        NClient cli = this.connections.get(cid);
        if (cli != null)
        {
          cli.sendFrame(fd.duplicate().getData());
        }
      }
      fd.release(1);
    } else
    {
      switch (target)//0表示发送给sessionid；-1表示发送给所有人；-2表示发送给公会；大于0表示发送给指定的列表
      {
        case 0:
          NClient cli = this.connections.get(sessionid);
          if (cli != null)
          {
            cli.sendFrame(fd.getData());
          }
          break;
        case -1:
          for (NClient c : this.connections.values())
          {
            c.sendFrame(fd.duplicate().getData());
          }
          fd.release(1);
          break;
        default:
          break;
      }
    }
  }
}
