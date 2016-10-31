/**
 * 路由
 */
package org.ngame.server;

import org.ngame.app.Application;
import org.ngame.util.ConsistentHash;
import org.ngame.util.Hashing;
import java.net.InetSocketAddress;
import java.util.*;
import org.ngame.Spring;
import org.ngame.script.ScriptEngine;

/**
 * 提供访问不同类型服务器的功能
 *
 * @author beykery
 */
public final class Router
{

  private final BaseServer server;
  private final Map<String, ConsistentHash<InetSocketAddress>> addresses = new HashMap<>();
  private final Map<InetSocketAddress, MultyChannel> channels = new HashMap<>();// 所有的远端连接
  private final Map<String, List<ServerChannel>> typeChannels = new HashMap<>();
  public static final String ROUTER_KEY = "router";//用于设置session的router信息
  public static final String SESSION_KEY = "sessionid";//session
  public static final String CMD_KEY = "cmd";//cmd
  public static final String DATA_KEY = "data";//data
  public static final String SN_KEY = "sn";//sn
  public static final String SENDLIST_KEY = "slist";//slist 
  public static final String SERVER_ID_KEY = "serverid";//serverdid

  /**
   * 路由构造
   *
   * @param server
   */
  @SuppressWarnings("unchecked")
  Router(BaseServer server)
  {
    ScriptEngine engine = Spring.bean(ScriptEngine.class);
    List<Map<String, Object>> cluster = (List<Map<String, Object>>) engine.getProperty(Application.serverFile, "cluster", true);
    for (Map<String, Object> item : cluster)
    {
      String type = (String) item.get("type");//服务器类型
      ConsistentHash<InetSocketAddress> temp = new ConsistentHash<>(Hashing.MD5);
      this.addresses.put(type, temp);
      List<Map<String, Object>> servers = (List<Map<String, Object>>) item.get("servers");
      for (Map<String, Object> ser : servers)
      {
        int p = (int) ser.get("port");
        if (p > 0)
        {
          InetSocketAddress add = new InetSocketAddress((String) ser.get("ip"), (int) ser.get("port"));
          int load = (int) ser.get("load");
          temp.add(add, load);
        }
      }
    }
    this.server = server;
  }

  /**
   * 根据玩家id和服务器类型计算合适的地址
   *
   * @param serverType
   * @param rid
   * @return
   */
  public InetSocketAddress hashingAddress(String serverType, long rid)
  {
    ConsistentHash<InetSocketAddress> ch = this.addresses.get(serverType);
    if (ch != null)
    {
      return ch.get(rid);
    }
    return null;
  }

  /**
   * 计算channel
   *
   * @param serverType
   * @param ucenterid
   * @return
   */
  public ServerChannel hashingChannel(String serverType, long ucenterid)
  {
    InetSocketAddress add = this.hashingAddress(serverType, ucenterid);
    return hashingChannel(add, ucenterid);
  }

  /**
   * 根据add计算channel
   *
   * @param add
   * @param pid
   * @return
   */
  public ServerChannel hashingChannel(InetSocketAddress add, long pid)
  {
    if (add != null)
    {
      MultyChannel mc = this.channels.get(add);
      if (mc == null)
      {
        int concurrency = concurrency(add.getAddress().getHostAddress(), add.getPort());
        mc = new MultyChannel(add, concurrency, this.server.getWorkerGroup());
        mc.connect();
        this.channels.put(add, mc);
      }
      return mc.getChannels()[(int) (pid % mc.count())];
    }
    return null;
  }

  /**
   * 返回指定远端类型的所有channel
   *
   * @param serverType
   * @return
   */
  public List<ServerChannel> channels(String serverType)
  {
    List<ServerChannel> list = this.typeChannels.get(serverType);
    if (list == null)
    {
      list = new ArrayList<>();
      typeChannels.put(serverType, list);
      ConsistentHash<InetSocketAddress> ch = this.addresses.get(serverType);
      if (ch != null)
      {
        Set<InetSocketAddress> set = ch.allNode();
        for (InetSocketAddress add : set)
        {
          ServerChannel sc = this.hashingChannel(add, 1);
          if (sc != null)
          {
            list.add(sc);
          }
        }
      }
    }
    return list;
  }

  //TODO ...
  public Map<String, ConsistentHash<InetSocketAddress>> getAddresses()
  {
    return this.addresses;
  }

  /**
   * 分组
   *
   * @param pids
   * @return
   */
  public Map<ServerChannel, List<Integer>> group(long... pids)
  {
    Map<ServerChannel, List<Integer>> map = new HashMap<>();
    for (int i = 0; i < pids.length; i++)
    {
      ServerChannel sc = hashingChannel("connector", pids[i]);
      List<Integer> l = map.get(sc);
      if (l == null)
      {
        l = new ArrayList<>();
        map.put(sc, l);
      }
      l.add(i);
    }
    return map;
  }

  /**
   * 计算服务器的并发数
   *
   * @param hostAddress
   * @param port
   * @return
   */
  private int concurrency(String addr, int port)
  {
    ScriptEngine engine = Spring.bean(ScriptEngine.class);
    List<Map<String, Object>> cluster = (List<Map<String, Object>>) engine.getProperty(Application.serverFile, "cluster", true);
    for (Map<String, Object> item : cluster)
    {
      List<Map<String, Object>> servers = (List<Map<String, Object>>) item.get("servers");
      for (Map<String, Object> ser : servers)
      {
        String ip = (String) ser.get("ip");
        int p = (int) ser.get("port");
        if (addr.equals(ip) && p == port)
        {
          return (int) ser.get("concurrency");
        }
      }
    }
    return 0;
  }
}
