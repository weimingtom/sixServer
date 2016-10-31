/**
 * 客户端测试
 */
package org.ngame.server.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.timeout.IdleStateEvent;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ngame.socket.NClient;
import org.ngame.socket.SocketClient;
import org.ngame.socket.framing.Framedata;
import org.ngame.socket.protocol.LVProtocol;
import org.ngame.socket.protocol.Protocol;

/**
 *
 * @author Administrator
 */
public class TestClient extends SocketClient
{

  private static final Logger LOG = Logger.getLogger(TestClient.class.getName());

  public TestClient(InetSocketAddress address, Protocol protocol)
  {
    super(address, protocol, new NioEventLoopGroup(1));
  }

  @Override
  public void onOpen(NClient so)
  {
    System.out.println("链接");
    Framedata fd = new Framedata(512);
    fd.putString("你好");
    fd.end();
    so.sendFrame(fd);
  }

  @Override
  public void onMessage(NClient so, Object message)
  {
    ByteBuf bb = (ByteBuf) message;
    System.out.println(bb.readableBytes());
  }

  @Override
  public void onClose(NClient so, boolean remote)
  {
    LOG.log(Level.WARNING, "连接断开：" + remote);
  }

  @Override
  public void onError(NClient so, Throwable ex)
  {
    LOG.log(Level.WARNING, "连接异常");
  }

  /**
   * 测试客户端
   *
   * @param args
   */
  public static void main(String... args) throws InterruptedException
  {
    TestClient sc = new TestClient(new InetSocketAddress("182.254.247.34", 4887), new LVProtocol());
    sc.connect().sync();
    //sc.input();
  }

  @Override
  public void onIdle(NClient conn, IdleStateEvent event)
  {
    conn.close("超时");
  }

  @Override
  public void onBusy(NClient conn)
  {
    conn.close("频繁");
  }
}
