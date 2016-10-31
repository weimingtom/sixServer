/**
 * 应用启动入口
 */
package org.ngame.app;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.ngame.script.ScriptEngine;
import org.ngame.server.BaseServer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.ngame.Spring;
import org.ngame.protocol.annotation.Proto;
import org.ngame.script.ScriptConsole;
import org.ngame.server.connector.ConnectorServer;
import org.ngame.server.http.River;
import org.ngame.socket.protocol.Protocol;
import org.ngame.util.ClassUtil;

/**
 * 事有不平则鸣
 * @author csf
 */
public final class Application
{

  public static boolean DEBUG;//是否为debug模式
  private static Logger LOG;
  public static String localIP;//本机地址
  public String name;//app name
  public static int gameId;
  private final BaseServer server;//app的服务器
  public static File serverFile;
  public static boolean linux;//本机是否为linux
  public static final File system = new File("./script/system/system.groovy");
  private static List<Application> apps = new ArrayList<>();
  private static final Map<Integer, Class> protos = new HashMap<>();//所有的proto类，需要扫描出来
  private static final Map<Integer, Codec> codecs = new HashMap<>();//所有的proto类，需要扫描出来
  private static int riverPort;//准备好的第三方接口，用来处理各种第三方回调
  private static River river;//http服务器
  public static byte[] ks;//加密key
  public static String DES_KEY = "421w6tW1ivg=";//des key
  public static boolean console;
  private static boolean ready;//是否为准备状态
  public static String recentUrl;
  public static Class<? extends Protocol> protocol;//分帧

  /**
   * 初始化系统配置
   */
  static
  {
    try
    {
      DOMConfigurator.configureAndWatch("./log4j.xml");
      LOG = Logger.getLogger(Application.class);
      LogManager.getLogManager().readConfiguration(new FileInputStream(new File("./logging.properties")));
      Properties p = new Properties();//系统属性
      p.load(new InputStreamReader(new FileInputStream(new File("./system.properties")), "UTF-8"));
      Set<Object> set = p.keySet();
      for (Object key : set)
      {
        System.getProperties().setProperty(key.toString(), p.getProperty(key.toString()));
      }
      Spring.init();
      gameId = Integer.valueOf(System.getProperty("gameId"));
      recentUrl = System.getProperty("game.recent.login.url");
      serverFile = new File(System.getProperty("game.server.config"));
      linux = System.getProperty("os", "win").contains("linux");
      localIP = System.getProperty("ip.local");
      String key = System.getProperty("game.xor.key");
      ks = key == null || key.isEmpty() ? null : key.getBytes();
      key = System.getProperty("game.des.key");
      DES_KEY = key == null || key.isEmpty() ? null : key;
      console = Boolean.parseBoolean(System.getProperty("game.server.console", "false"));
      protocol = (Class<? extends Protocol>) Class.forName(System.getProperty("game.socket.protocol.header", "org.ngame.socket.protocol.Varint32HeaderProtocol"));
      scanProtos();
    } catch (Exception e)
    {
      e.printStackTrace();
      LOG.error("读取系统配置文件失败");
    }
  }

  /**
   * 扫描所有的proto类
   */
  private static void scanProtos()
  {
    Set<Class<?>> set = ClassUtil.scan(System.getProperty("game.protocol.scan.path"));
    for (Class<?> c : set)
    {
      Proto p = c.getAnnotation(Proto.class);
      if (p != null && p.value() != 0)
      {
        protos.put(p.value(), c);
        Codec<?> codec = ProtobufProxy.create(c);
        codecs.put(p.value(), codec);
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
   * 解码器
   *
   * @param cmd
   * @return
   */
  public static Codec getCodec(int cmd)
  {
    return codecs.get(cmd);
  }

  private Application(BaseServer server)
  {
    this.server = server;
  }

  /**
   * 获取server
   *
   * @return
   */
  public BaseServer getServer()
  {
    return server;
  }

  /**
   * 状态
   *
   * @param ready
   */
  public static void setReady(boolean ready)
  {
    Application.ready = ready;
  }

  /**
   * 状态
   *
   * @return
   */
  public static boolean isReady()
  {
    return ready;
  }

  /**
   * 开启应用
   *
   * @param input
   * @param bossGroup
   * @param workGroup
   * @throws InterruptedException
   */
  public void start(boolean input, EventLoopGroup bossGroup, EventLoopGroup workGroup) throws InterruptedException
  {
    server.start(bossGroup, workGroup);
    if (input)
    {
      this.input();
    }
  }

  /**
   * 允许输入脚本命令
   */
  public void input()
  {
    Thread in = new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        new ScriptConsole().inputScript();
      }
    });
    in.setName(this.server.getName() + " script-input");
    in.start();
  }

  /**
   * 启动的app列表
   *
   * @return
   */
  public static List<Application> apps()
  {
    return apps;
  }

  /**
   * connector
   *
   * @return
   */
  public static ConnectorServer connector()
  {
    for (Application app : apps)
    {
      BaseServer server = app.getServer();
      if (server instanceof ConnectorServer)
      {
        return (ConnectorServer) server;
      }
    }
    return null;
  }

  /**
   * start
   */
  private static void start() throws InterruptedException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException
  {
    final int bossSize = Integer.parseInt(System.getProperty("game.socket.server.thread.selector", "2"));
    final int workSize = Integer.parseInt(System.getProperty("game.socket.server.thread.io", "4"));
    final EventLoopGroup boss = linux ? new EpollEventLoopGroup(bossSize) : new NioEventLoopGroup(bossSize);
    final EventLoopGroup work = linux ? new EpollEventLoopGroup(workSize) : new NioEventLoopGroup(workSize);
    ScriptEngine engine = Spring.bean(ScriptEngine.class);
    Application app;
    Application last = null;
    Application lastHome = null;
    int count = 0;
    riverPort = engine.getInt(serverFile, "riverPort");
    List<Map<String, Object>> servers = (List<Map<String, Object>>) engine.getProperty(serverFile, "cluster", true);
    for (Map<String, Object> item : servers)
    {
      Class c = Class.forName((String) item.get("class"));
      String type = (String) item.get("type");
      boolean autoTrans = (boolean) item.get("autoTrans");
      List<Map<String, Object>> ss = (List<Map<String, Object>>) item.get("servers");
      for (Map<String, Object> s : ss)
      {
        String ip = (String) s.get("ip");
        if (localIP.equals(ip))
        {
          Constructor constructor = c.getConstructor(Integer.TYPE, Integer.TYPE);
          Object clientPort = s.get("clientPort");
          Object clientIp = s.get("clientIp");
          BaseServer bs = (BaseServer) constructor.newInstance((int) s.get("port"), clientPort == null ? 0 : (int) clientPort);
          bs.setName((String) s.get("name"));
          bs.setServerType(type);
          bs.setAutoTrans(autoTrans);
          app = new Application(bs);
          apps.add(app);
          app.name = bs.getName();
          app.start(false, boss, work);
          count++;
          switch (type)
          {
            case "connector":
              last = app;
              break;
            case "home":
              lastHome = app;
              break;
          }
        }
      }
    }
    if (console)
    {
      if (last != null)
      {
        last.input();//启动输入
      } else if (lastHome != null)
      {
        lastHome.input();
      }
    }
    LOG.info(String.format("cluster已经启动%d个服务器", count));
    if (last != null)//connector的话，启动一个river
    {
      river = new River("/river", riverPort, 2);
      river.start();
      LOG.info("river已启动");
    }
  }
  private static final String fz = "\n"
          + "                   _ooOoo_\n"
          + "                  o8888888o\n"
          + "                  88\" . \"88\n"
          + "                  (| -_- |)\n"
          + "                  O\\  =  /O\n"
          + "               ____/`---'\\____\n"
          + "             .'  \\\\|     |//  `.\n"
          + "            /  \\\\|||  :  |||//  \\\n"
          + "           /  _||||| -:- |||||-  \\\n"
          + "           |   | \\\\\\  -  /// |   |\n"
          + "           | \\_|  ''\\---/''  |   |\n"
          + "           \\  .-\\__  `-`  ___/-. /\n"
          + "         ___`. .'  /--.--\\  `. . __\n"
          + "      .\"\" '<  `.___\\_<|>_/___.'  >'\"\".\n"
          + "     | | :  `- \\`.;`\\ _ /`;.`/ - ` : | |\n"
          + "     \\  \\ `-.   \\_ __\\ /__ _/   .-` /  /\n"
          + "======`-.____`-.___\\_____/___.-`____.-'======\n"
          + "                   `=---='\n"
          + "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n"
          + "         佛祖保佑              本作大卖\n";

  /**
   * 应用入口
   *
   * @param args
   * @throws java.lang.Exception
   */
  public static void main(String... args) throws Exception
  {
    System.out.println(fz);
    Thread.currentThread().setName("Application");
    start();//go
  }
}
