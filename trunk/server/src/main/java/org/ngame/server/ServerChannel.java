/**
 * 到别的服务器的连接
 */
package org.ngame.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.timeout.IdleStateEvent;
import java.net.InetSocketAddress;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ngame.app.Application;
import org.ngame.socket.LocalClient;
import org.ngame.socket.NClient;
import org.ngame.socket.NListener;
import org.ngame.socket.SocketClient;
import org.ngame.socket.protocol.LVProtocol;

/**
 * 用来转发或者rpc调用(多是单向的用户发送数据)
 *
 * @author beykery
 */
public class ServerChannel extends NListener
{

  public static final int STATUS_INIT = 0;
  public static final int STATUS_CONNECTING = 1;
  public static final int STATUS_OPEN = 2;
  public static final int STATUS_CLOSED = 3;
  private static final Logger LOG = Logger.getLogger(ServerChannel.class.getName());
  private int status;
  private final Deque<ByteBuf> queue = new LinkedList<>();//缓存尚未连接成功时的发送数据
  private ChannelMessageListener messageListener;
  private final Lock lock = new ReentrantLock();
  private LocalClient lc;//本地类型的客户端
  private SocketClient sc;//socket类型的客户端
  private final InetSocketAddress remote;//远端地址

  /**
   * 通道
   *
   * @param add
   * @param group
   */
  public ServerChannel(InetSocketAddress add, EventLoopGroup group)
  {
    this.remote = add;
    if (add.getAddress().getHostAddress().equals(Application.localIP))//本地
    {
      lc = new LocalClient(add.getPort(), group)
      {

        @Override
        public void onOpen(NClient conn)
        {
          ServerChannel.this.onOpen(conn);
        }

        @Override
        public void onClose(NClient conn, boolean local)
        {
          ServerChannel.this.onClose(conn, local);
        }

        @Override
        public void onMessage(NClient conn, Object message)
        {
          ServerChannel.this.onMessage(conn, message);
        }

        @Override
        public void onError(NClient conn, Throwable ex)
        {
          ServerChannel.this.onError(conn, ex);
        }

        @Override
        public void onIdle(NClient conn, IdleStateEvent event)
        {
          ServerChannel.this.onIdle(conn, event);
        }

        @Override
        public void onBusy(NClient conn)
        {
          ServerChannel.this.onBusy(conn);
        }
      };
    } else//非本地
    {
      sc = new SocketClient(add, new LVProtocol(), group)
      {

        @Override
        public void onOpen(NClient conn)
        {
          ServerChannel.this.onOpen(conn);
        }

        @Override
        public void onClose(NClient conn, boolean local)
        {
          ServerChannel.this.onClose(conn, local);
        }

        @Override
        public void onMessage(NClient conn, Object message)
        {
          ServerChannel.this.onMessage(conn, message);
        }

        @Override
        public void onError(NClient conn, Throwable ex)
        {
          ServerChannel.this.onError(conn, ex);
        }

        @Override
        public void onIdle(NClient conn, IdleStateEvent event)
        {
          ServerChannel.this.onIdle(conn, event);
        }

        @Override
        public void onBusy(NClient conn)
        {
          ServerChannel.this.onBusy(conn);
        }
      };
    }
  }

  public int getStatus()
  {
    return status;
  }

  /**
   * 连接
   *
   * @return
   */
  public ChannelFuture connect()
  {
    status = STATUS_CONNECTING;
    if (this.lc != null)
    {
      return lc.connect();
    }
    return sc.connect();
  }

  @Override
  public void onOpen(NClient so)
  {
    status = STATUS_OPEN;
    this.lock.lock();
    LOG.log(Level.FINE, "连接到：" + remote.getAddress().getHostAddress() + ":" + remote.getPort());
    try
    {
      while (!queue.isEmpty())
      {
        ByteBuf fd = queue.removeFirst();
        if (lc != null)
        {
          lc.sendFrame(fd);
        } else
        {
          sc.sendFrame(fd);
        }
      }
    } finally
    {
      this.lock.unlock();
    }
  }

  @Override
  public void onMessage(NClient so, Object message)
  {
    if (this.messageListener != null)
    {
      this.messageListener.onChannelMessage(this, message);
    }
  }

  @Override
  public void onClose(NClient so, boolean remote)
  {
    LOG.log(Level.SEVERE, "到远程服务器的连接断开：" + so.getCloseReason());
    status = STATUS_CLOSED;
  }

  @Override
  public void onError(NClient so, Throwable ex)
  {
    ex.printStackTrace();
    LOG.log(Level.SEVERE, "到其他服务器的数据传输异常");
    this.close();
  }

  /**
   * 发送
   *
   * @param data
   * @return
   */
  public ChannelFuture send(ByteBuf data)
  {
    if (status == STATUS_OPEN)
    {
      if (lc != null)
      {
        return lc.sendFrame(data);
      } else
      {
        return sc.sendFrame(data);
      }
    } else
    {
      lock.lock();
      try
      {
        this.queue.offer(data);
      } finally
      {
        lock.unlock();
      }
    }
    return null;
  }

  public void setMessageListener(ChannelMessageListener messageListener)
  {
    this.messageListener = messageListener;
  }

  public ChannelMessageListener getMessageListener()
  {
    return messageListener;
  }

  @Override
  public String toString()
  {
    return remote.toString();
  }

  @Override
  public void onIdle(NClient conn, IdleStateEvent event)
  {
    conn.close("服务器连接超时");
  }

  @Override
  public void onBusy(NClient conn)
  {
    conn.close("通信频繁");
  }

  /**
   * 状态
   *
   * @return
   */
  public boolean isOpen()
  {
    if (lc != null)
    {
      return lc.isOpen();
    }
    return sc.isOpen();
  }

  /**
   * 关闭连接
   *
   * @return
   */
  public ChannelFuture close()
  {
    if (lc != null)
    {
      return lc.close();
    }
    return sc.close();
  }

}
