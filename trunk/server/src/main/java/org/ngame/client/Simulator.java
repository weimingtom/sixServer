/**
 * 模拟器，用来模拟用户行为
 */
package org.ngame.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.internal.StringUtil;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.ngame.script.ScriptEngine;
import org.ngame.socket.NClient;
import org.ngame.socket.SocketClient;
import org.ngame.socket.protocol.LVProtocol;
import org.ngame.socket.protocol.Protocol;
import org.ngame.util.Console;
import org.ngame.util.HttpUtils;
import org.bouncycastle.util.encoders.UrlBase64;
import org.ngame.app.Application;
import org.ngame.protocol.annotation.Proto;
import org.ngame.protocol.auth.AuthRequest;
import org.ngame.socket.framing.Framedata;
import org.ngame.socket.framing.Varint32Framedata;
import org.ngame.socket.protocol.Varint32HeaderProtocol;
import org.ngame.util.ClassUtil;
import org.ngame.util.DESMD5;
import org.ngame.util.MathUtil;

/**
 *
 * @author beykery
 */
public class Simulator
{

  private static final Map<Integer, Class> protos = new HashMap<>();//所有的proto类，需要扫描出来
  private final String loginUrl = "http://119.29.189.247:8080/uc/login.pf";
  private final String registUrl = "http://119.29.189.247:8080/uc/userregister.pf";
  private final String serverListUrl = "http://119.29.189.247:8080/uc/serverlist.pf";

//  private final String loginUrl = "http://localhost:8080/ocean/login.pf";
//  private final String registUrl = "http://localhost:8080/ocean/userregister.pf";
//  private final String serverListUrl = "http://localhost:8080/ocean/serverlist.pf";
  private Net net;
  private final String name;
  private final String pass;
  private final List<ServerItem> servers;
  private static final Console console = new Console();
  private static final ScriptEngine engine = new ScriptEngine();
  private ServerItem curServer;
  private static final String pathPre = "./script/simulate/";
  private static final Object lock = new Object();
  private long ucid;//用户中心id
  private String loginid;//加密过的ucid
  private String pid;//游戏用户id
  private int channelid = 9;//客户端来源(也是版本等标示)
  private int gameid=3;
  private final byte[] ks = "whoareyou?".getBytes();
  //private final byte[] ks = null;
  private Protocol p = new Varint32HeaderProtocol();

  /**
   * 用户名密码
   *
   * @param name
   * @param string
   */
  private Simulator(String name, String pass)
  {
    this.name = name;
    this.pass = pass;
    this.servers = new ArrayList<>();
    scanProtos();
  }
//{"data":[{"serverid":12,"status":1,"showindex":12,"servername":"beykery debug","gameid":1,"serverchannelList":[{"serverid":12,"channeltype":"andriod","gameid":1},{"serverid":12,"channeltype":"ios","gameid":1}],"serveraddress":"http://10.18.121.28:8091"},{"serverid":11,"status":1,"showindex":11,"servername":"�������2","gameid":1,"serverchannelList":[{"serverid":11,"channeltype":"andriod","gameid":1},{"serverid":11,"channeltype":"ios","gameid":1}],"serveraddress":"http://10.18.103.141:8090"},{"serverid":10,"status":1,"showindex":10,"servername":"�������1","gameid":1,"serverchannelList":[{"serverid":10,"channeltype":"andriod","gameid":1},{"serverid":10,"channeltype":"ios","gameid":1}],"serveraddress":"http://10.18.103.141:8089"},{"serverid":4,"status":1,"showindex":1,"servername":"������Է�","gameid":1,"serverchannelList":[{"serverid":4,"channeltype":"andriod","gameid":1},{"serverid":4,"channeltype":"ios","gameid":1}],"serveraddress":"http://182.254.177.64:8080"},{"serverid":3,"status":1,"showindex":0,"servername":"csf���Է�","gameid":1,"serverchannelList":[{"serverid":3,"channeltype":"andriod","gameid":1},{"serverid":3,"channeltype":"APPSHENHE","gameid":1},{"serverid":3,"channeltype":"ios","gameid":1}],"serveraddress":"http://10.18.121.24:8080"},{"serverid":2,"status":1,"showindex":2,"servername":"cp���Է�","gameid":1,"serverchannelList":[{"serverid":2,"channeltype":"andriod","gameid":1},{"serverid":2,"channeltype":"ios","gameid":1}],"serveraddress":"http://10.18.121.202:8080"}]}

  public static void main(String[] args)
  {
    try
    {
      while (true)
      {
        System.out.println("---请输入用户名密码(用空格隔开)---");
        String line = console.readLine();
        String[] nameAndPass = StringUtil.split(line, ' ');
        if (nameAndPass != null && nameAndPass.length == 2)
        {
          Simulator s = new Simulator(nameAndPass[0], nameAndPass[1]);
          s.loginUserCenter();
          System.out.println("loginid:" + s.loginid);
          if (s.ucid > 0)//已经拿到ucid
          {
            s.serverList();
          } else
          {
            System.out.println("无法登陆，请检查用户名密码");
            continue;
          }
          s.selectServer();
          s.connect(s.curServer);
          s.authConnector();
          s.simulate();
        } else
        {
          System.out.println("---输入非法，请重新输入---");
        }
      }
    } catch (Exception e)
    {
      e.printStackTrace();
      System.out.println("异常：" + e);
    }
  }

  /**
   * 连接
   */
  private void connect(ServerItem server) throws InterruptedException
  {
    if (this.net != null)
    {
      this.net.close().sync();
    }
    int index = server.serveraddress.lastIndexOf(':');
    int port = Integer.parseInt(server.serveraddress.substring(index + 1));
    this.net = new Net(new InetSocketAddress(server.serveraddress.substring(0, index), port), p, new NioEventLoopGroup(1));
    //this.net = new Net(new InetSocketAddress("localhost", 4887), p, new NioEventLoopGroup(1));
    this.net.connect();
  }

  /**
   * 登录用户中心
   */
  private long loginUserCenter() throws Exception
  {
    Map<String, Object> query = new HashMap<>();
    query.put("usercode", name);
    query.put("password", pass);
    query.put("channelid", channelid);
    byte[] data = HttpUtils.get(this.loginUrl, query);
    JSONObject resp = JSON.parseObject(new String(data, "UTF-8"));
    loginid = resp.getString("loginid");
    if (loginid != null)
    {
      long ucenterId = -1;
      try
      {
        String c = new String(DESMD5.decrypt(UrlBase64.decode(loginid.getBytes("UTF-8")), Application.DES_KEY));
        JSONObject json = JSON.parseObject(c);
        ucenterId = json.getLong("id");
      } catch (Exception e)
      {
        System.out.println(e.getMessage());
      }
      this.ucid = ucenterId;
      return this.ucid;
    } else//无法登陆成功，尝试一下注册吧
    {
      data = HttpUtils.get(this.registUrl, query);
      resp = JSON.parseObject(new String(data, "UTF-8"));
      loginid = resp.getString("loginId");
      if (loginid != null)
      {
        long ucenterId = -1;
        try
        {
          ucenterId = Long.parseLong(new String(DESMD5.decrypt(UrlBase64.decode(loginid.getBytes("UTF-8")), Application.DES_KEY)));
        } catch (Exception e)
        {
          System.out.println(e.getMessage());
        }
        this.ucid = ucenterId;
        return this.ucid;
      }
    }
    return -1;
  }

  /**
   * 请求服务器列表
   */
  @SuppressWarnings("empty-statement")
  private void serverList() throws Exception
  {
    Map<String, Object> query = new HashMap<>();
    query.put("channelid", channelid);
    query.put("loginid", loginid);
    byte[] data = HttpUtils.get(this.serverListUrl, query);
    JSONObject resp = JSON.parseObject(new String(data, "UTF-8"));
    System.out.println(resp);
    JSONArray arr = resp.getJSONArray("servers");
    if (arr != null)
    {
      for (int i = 0; i < arr.size(); i++)
      {
        JSONObject item = arr.getJSONObject(i);
        ServerItem si = new ServerItem();
        si.serverid = item.getIntValue("serverId");
        si.status = item.getIntValue("status");
        si.servername = item.getString("name");
        si.serveraddress = item.getString("address");
        this.servers.add(si);
      }
    }
  }

  /**
   * 选择服务器
   */
  @SuppressWarnings("empty-statement")
  private void selectServer() throws InterruptedException
  {
    while (true)
    {
      System.out.println("----选择一个服务器登入----");
      for (ServerItem item : servers)
      {
        System.out.println(item);
      }
      System.out.println("-----------------------");
      try
      {
        String line = console.readLine();
        int id = Integer.parseInt(line);
        for (ServerItem s : servers)
        {
          if (s.getServerId() == id)
          {
            this.curServer = s;
            break;
          }
        }
      } catch (Exception e)
      {
      }
      if (this.curServer != null)
      {
        break;
      }
    }
  }

  /**
   * 在连接器验证
   */
  private void authConnector() throws Exception
  {
    AuthRequest ar = new AuthRequest();
    //TODO ..这个是因为没接用户中心导致没有loginid 临时改掉的，以后会改回来
//    ar.setLoginid(loginid);
    ar.setServerid(curServer.serverid);
    while (!net.ok);
    this.send(ar, 6);
  }

  /**
   * 模拟业务逻辑请求
   */
  @SuppressWarnings("empty-statement")
  private void simulate() throws InterruptedException
  {
    while (true)
    {
      try
      {
        System.out.println("---请输入待模拟的脚本文件和方法--");
        String line = console.readLine();
        String[] fileAndMethod = StringUtil.split(line, ' ');
        if (net.ok && fileAndMethod != null && fileAndMethod.length == 2)
        {
          Object request = engine.executeMethod(new File(pathPre + fileAndMethod[0] + ".groovy"), fileAndMethod[1], null);
          byte[] content = encode(request);
          MathUtil.xor(content, ks);
          int cmd = getCmd(request);
          if (cmd > 0)
          {
            Framedata fd = p instanceof LVProtocol ? new Framedata(512) : new Varint32Framedata(512);
            fd.putShort(cmd);
            fd.putBytes(content);
            fd.end();
            net.sendFrame(fd);
          } else
          {
            System.out.println("此对象并非可发送的协议对象：" + request.getClass());
          }
        } else if (line.equals("quit") || line.equals("exit") || line.equals("q"))
        {
          System.out.println("--模拟结束--  bye.");
          net.close().sync();
          break;
        } else if (!net.ok)
        {
          break;
        } else
        {
          System.out.println("---输入非法，请重新输入---");
        }
      } catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }

  /**
   * 发送请求
   *
   * @param ar
   */
  private ChannelFuture send(Object request, int cmd) throws IOException
  {
    Codec codec = ProtobufProxy.create(request.getClass());
    byte[] content = codec.encode(request);
    MathUtil.xor(content, ks);
    Framedata fd = p instanceof LVProtocol ? new Framedata(512) : new Varint32Framedata(512);
    fd.putShort(cmd);
    fd.putBytes(content);
    fd.end();
    return this.net.sendFrame(fd.getData());
  }

  /**
   * 服务器列表里面的服务器
   */
  class ServerItem
  {

    private int serverid;
    private int status;
    private String servername;
    private String serveraddress;

    public int getServerId()
    {
      return serverid;
    }

    public int getStatus()
    {
      return status;
    }

    public void setServerId(int serverId)
    {
      this.serverid = serverId;
    }

    public void setStatus(int status)
    {
      this.status = status;
    }

    @Override
    public String toString()
    {
      StringBuilder sb = new StringBuilder();
      sb.append(this.serverid).append(" ");
      sb.append(this.servername).append(" ");
      sb.append(this.serveraddress).append(" ");
      sb.append(this.status).append(" ");
      return sb.toString();
    }

  }

  /**
   * 客户端网络
   */
  class Net extends SocketClient
  {

    public volatile boolean ok;

    public Net(InetSocketAddress address, Protocol protocol, EventLoopGroup group)
    {
      super(address, protocol, group);
    }

    @Override
    public void onOpen(NClient nc)
    {
      System.out.println("connect ok." + nc);
      this.ok = true;
      nc.idle(10, 10, 10, TimeUnit.MINUTES);
    }

    @Override
    public void onClose(NClient nc, boolean bln)
    {
      System.out.println("连接已关闭" + nc);
      ok = false;
    }

    @Override
    public void onMessage(NClient nc, Object o)
    {
      ByteBuf bb = (ByteBuf) o;
      bb.readerIndex(nc.getProtocol().headerLen());
      int cmd = bb.readShort();
      byte[] content = new byte[bb.readableBytes()];
      bb.readBytes(content);
      bb.release();
      Class c = getProto(cmd);
      if (c != null)
      {
        MathUtil.xor(content, ks);
        Object entity = decode(content, c);
        System.out.println(entity.getClass().getSimpleName() + ":" + JSON.toJSON(entity));
      } else
      {
        System.out.println("无法解析：" + cmd);
      }
    }

    @Override
    public void onError(NClient nc, Throwable thrwbl)
    {
      System.out.println("连接错误：" + nc + ":" + thrwbl.getMessage());
      ok = false;
    }

    @Override
    public void onIdle(NClient conn, IdleStateEvent event)
    {
      System.out.println("连接超时：" + conn + ":" + event);
      ok = false;
    }

    @Override
    public void onBusy(NClient conn)
    {
      System.out.println("频繁：" + conn);
      ok = false;
    }
  }

  /**
   * 扫描所有的proto类
   */
  private static void scanProtos()
  {
    Set<Class<?>> set = ClassUtil.scan("org.ngame.protocol");
    for (Class c : set)
    {
      Proto p = (Proto) c.getAnnotation(Proto.class);
      if (p != null && p.value() != 0)
      {
        protos.put(p.value(), c);
      }
    }
  }

  /**
   * 查找proto类
   *
   * @param cmd
   * @return
   */
  public static Class getProto(int cmd)
  {
    return protos.get(cmd);
  }

  /**
   * decode
   *
   * @param <T>
   * @param content
   * @param c
   * @return
   */
  private static <T> T decode(byte[] content, Class<T> c)
  {
    try
    {
      Codec<T> codec = ProtobufProxy.create(c);
      return codec.decode(content);
    } catch (Exception e)
    {
      System.out.println("无法反序列化");
      return null;
    }
  }

  /**
   * 序列化
   *
   * @param request
   * @return
   */
  private static byte[] encode(Object request)
  {
    try
    {
      Codec codec = ProtobufProxy.create(request.getClass());
      byte[] content = codec.encode(request);
      return content;
    } catch (Exception e)
    {
      System.out.println("无法序列化");
      return null;
    }
  }

  /**
   * 计算cmd
   *
   * @param request
   * @return
   */
  private static int getCmd(Object request)
  {
    Proto p = request.getClass().getAnnotation(Proto.class);
    if (p != null)
    {
      return p.value();
    }
    return 0;
  }
}
