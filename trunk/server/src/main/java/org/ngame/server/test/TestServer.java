/**
 * 测试
 */
package org.ngame.server.test;

import org.ngame.socket.NClient;
import io.netty.buffer.ByteBuf;
import io.netty.handler.timeout.IdleStateEvent;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ngame.socket.NServer;
import org.ngame.socket.protocol.LVProtocol;
import org.ngame.util.IOUtil;

/**
 *
 * @author beykery
 */
public class TestServer extends NServer
{

  public TestServer(InetSocketAddress address)
  {
    super(address, NServer.NETWORK_SOCKET);
  }
  private static final Logger LOG = Logger.getLogger(TestServer.class.getName());

  @Override
  protected void preStop()
  {
    LOG.log(Level.WARNING, "即将停止服务器");
  }

  @Override
  public void onOpen(NClient conn)
  {
    LOG.log(Level.WARNING, "有个客户端连接到来" + conn);
//		Framedata fd = new Framedata();
//		fd.putString("哈哈");
//		fd.end();
//		conn.sendFrame(fd);
  }

  @Override
  public void onClose(NClient conn, boolean local)
  {
    LOG.log(Level.WARNING, "客户端断开连接" + conn + local);
  }

  @Override
  public void onMessage(NClient conn, Object message)
  {
    ByteBuf bb = (ByteBuf) message;
    bb.readerIndex(0);
    System.out.println("长度：" + bb.readableBytes());
    bb.readerIndex(2);
    String s = IOUtil.readString(bb);
    System.out.println(s);
    bb.readerIndex(0);
    conn.sendFrame(bb);
  }

  @Override
  public void onError(NClient conn, Throwable ex)
  {
    ex.printStackTrace();
    LOG.log(Level.WARNING, "客户端异常" + ex);
    conn.close("客户端异常");
  }

  /**
   * 测试服务器
   *
   * @param args
   * @throws InterruptedException
   */
  public static void main(String... args) throws InterruptedException
  {
    NServer x = new TestServer(new InetSocketAddress(4887));
    x.setProtocol(LVProtocol.class);
    x.start();
  }

  @Override
  public void onIdle(NClient conn, IdleStateEvent event)
  {
    conn.close();
  }

  @Override
  public void onBusy(NClient conn)
  {
    conn.close("频繁");
  }
}
