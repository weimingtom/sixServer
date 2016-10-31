/**
 * 服务器service
 */
package org.ocean.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.Query;
import org.hibernate.Session;
import org.ocean.dao.BaseDao;
import org.ocean.domain.Channel;
import org.ocean.domain.Game;
import org.ocean.domain.GameServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.TimeUnit;
import net.schmizz.keepalive.KeepAliveProvider;
import net.schmizz.sshj.DefaultConfig;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.apache.log4j.Logger;
import org.ocean.domain.LoginHistory;
import org.ocean.util.ExceptionUtil;
import org.springframework.transaction.annotation.Propagation;

/**
 *
 * @author beykery
 */
@Component
public class ServerService
{

  private static final Logger LOG = Logger.getLogger(ServerService.class);
  @Autowired
  private BaseDao dao;

  /**
   * 服务器列表
   *
   * @param gameId
   * @param page
   * @param rows
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<GameServer> getServerList(Long gameId, int page, int rows)
  {
    if (gameId != null)
    {
      Session s = dao.getSession();
      Query q = s.createQuery("from GameServer gs where gs.gameId=:gameId order by gs.id desc");
      q.setLong("gameId", gameId);
      q.setFirstResult((page - 1) * rows);
      q.setMaxResults(rows);
      q.setCacheable(true);
      List r = q.list();
      return r;
    }
    return Collections.EMPTY_LIST;
  }

  /**
   * 添加一个游戏服务器
   *
   * @param tarGame
   * @param serverId
   * @param serverName
   * @param serverAddr
   * @param serverStatus
   * @param ts
   * @param serverDesc
   * @param riverPort
   * @return
   */
  public boolean addServer(Long tarGame, int serverId, String serverName, String serverAddr, int serverStatus, String[] ts, String serverDesc, int riverPort)
  {
    if (tarGame != null)
    {
      Game g = dao.get(Game.class, tarGame);
      if (g != null)
      {
        GameServer gs = new GameServer();
        JSONArray arr = new JSONArray();
        if (ts != null && ts.length > 0)
        {
          arr.addAll(Arrays.asList(ts));
        }
        gs.setTags(arr.toString());
        gs.setAddress(serverAddr);
        gs.setGameId(g.getId());
        gs.setName(serverName);
        gs.setNote(serverDesc);
        gs.setStatus(serverStatus);
        gs.setServerId(serverId);
        gs.setRiverPort(riverPort);
        gs.setCreateTime(new Date());
        gs = dao.saveOrUpdate(gs);
        return gs != null;
      }
    }
    return false;
  }

  /**
   * 编辑server
   *
   * @param id
   * @param serverName
   * @param serverAddr
   * @param serverStatus
   * @param ts
   * @param serverDesc
   * @param riverPort
   * @return
   */
  @Transactional
  public boolean editServer(long id, String serverName, String serverAddr, int serverStatus, String[] ts, String serverDesc, int riverPort)
  {
    Session s = dao.getSession();
    GameServer gs = (GameServer) s.get(GameServer.class, id);
    if (gs != null)
    {
      JSONArray arr = new JSONArray();
      if (ts != null && ts.length > 0)
      {
        arr.addAll(Arrays.asList(ts));
      }
      gs.setTags(arr.toString());
      gs.setAddress(serverAddr);
      gs.setName(serverName);
      gs.setNote(serverDesc);
      gs.setStatus(serverStatus);
      gs.setRiverPort(riverPort);
      s.update(gs);
      s.flush();
      return true;
    }
    return false;
  }

  /**
   * 删除服务器
   *
   * @param id
   * @return
   */
  @Transactional
  public boolean deleteServer(long id)
  {
    Session s = dao.getSession();
    GameServer gs = s.get(GameServer.class, id);
    if (gs != null)
    {
      s.delete(gs);
      s.flush();
      return true;
    }
    return false;
  }

  /**
   * 服务器列表
   *
   * @param channelId
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<GameServer> getServerList(int channelId)
  {
    List<GameServer> l = new ArrayList<>();
    Session s = dao.getSession();
    Query q = s.createQuery("select c from Channel c where c.cid=:cid");
    q.setInteger("cid", channelId);
    q.setCacheable(true);
    Channel c = (Channel) q.uniqueResult();
    if (c != null)
    {
      q = s.createQuery("select gs from GameServer gs where gs.gameId=:gid");
      q.setLong("gid", c.getGid());
      q.setCacheable(true);
      List r = q.list();
      for (Object item : r)
      {
        GameServer gs = (GameServer) item;
        String tags = gs.getTags();
        if (tags != null && !tags.isEmpty())
        {
          try
          {
            JSONArray arr = JSONArray.parseArray(tags);
            if (arr.contains(c.getTag()))
            {
              l.add(gs);
            }
          } catch (Exception e)
          {
          }
        }
      }
    }
    return l;
  }

  /**
   * 服务器总量
   *
   * @param gameId
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public long totalServer(Long gameId)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("select count(s.id) from GameServer s where s.gameId=:gid");
    q.setLong("gid", gameId);
    q.setCacheable(true);
    long c = (Long) q.uniqueResult();
    return c;
  }

  /**
   * 服务器tag的所有tag
   *
   * @param sid
   * @param gid
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public JSONArray getTags(long sid, Long gid)
  {
    JSONArray arr = new JSONArray();
    if (gid != null)
    {
      Session s = dao.getSession();
      Query q = s.createQuery("select c.tag from Channel c where c.gid=:gid");
      q.setLong("gid", gid);
      q.setCacheable(true);
      List l = q.list();
      Set<String> set = new HashSet<>(l);
      GameServer gs = s.get(GameServer.class, sid);
      JSONArray cur = null;
      String ts = gs == null ? null : gs.getTags();
      if (ts != null)
      {
        cur = JSONArray.parseArray(ts);
      }
      for (String t : set)
      {
        JSONObject item = new JSONObject();
        item.put("tag", t);
        if (cur != null && cur.contains(t))
        {
          item.put("selected", true);
        }
        arr.add(item);
      }
    }
    return arr;
  }

  /**
   * server
   *
   * @param gid
   * @param sid
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public GameServer getServer(long gid, int sid)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("select s from GameServer as s where s.gameId=:gid and s.serverId=:sid");
    q.setLong("gid", gid);
    q.setInteger("sid", sid);
    q.setCacheable(true);
    return (GameServer) q.uniqueResult();
  }

  /**
   * 最新的版本
   *
   * @param path
   * @param gid
   * @return
   */
  public String lastVersion(String path, Long gid)
  {
    File dest = null;
    File root = new File(path + "/games/" + gid);
    if (root.exists())
    {
      File[] fs = root.listFiles();
      if (fs != null && fs.length > 0)
      {
        for (File f : fs)
        {
          if (f.isDirectory() && (dest == null || f.getName().compareTo(dest.getName()) > 0))
          {
            dest = f;
          }
        }
      }
    }
    if (dest != null)
    {
      return dest.getAbsolutePath();
    }
    return null;
  }

  /**
   * 配置服务器参数
   *
   * @param id
   * @param redisIp
   * @param redisPort
   * @param redisPwd
   * @param redisDb
   * @param logUrl
   * @param logUser
   * @param logPwd
   * @param muser
   * @param mpwd
   * @param innerIp
   * @param innerPort
   * @return
   */
  @Transactional
  public boolean saveConfig(long id, String redisIp, int redisPort, String redisPwd, int redisDb, String logUrl, String logUser, String logPwd, String muser, String mpwd, String innerIp, int innerPort)
  {
    Session s = dao.getSession();
    GameServer gs = s.get(GameServer.class, id);
    if (gs != null)
    {
      gs.setRedisIp(redisIp);
      gs.setRedisPort(redisPort);
      gs.setRedisDb(redisDb);
      gs.setRedisPwd(redisPwd);
      gs.setLogPwd(logPwd);
      gs.setLogUrl(logUrl);
      gs.setLogUser(logUser);
      gs.setMpwd(mpwd);
      gs.setMuser(muser);
      gs.setInnerIp(innerIp);
      gs.setInnerPort(innerPort);
      s.update(gs);
      Query q = s.createQuery("select gs from GameServer as gs where gs.address=:addr");
      q.setString("addr", gs.getAddress());
      List<GameServer> ls = q.list();
      for (GameServer g : ls)
      {
        if (g.getId() != gs.getId())
        {
          g.setRedisIp(redisIp);
          g.setRedisPort(redisPort);
          g.setRedisDb(redisDb);
          g.setRedisPwd(redisPwd);
          g.setLogPwd(logPwd);
          g.setLogUrl(logUrl);
          g.setLogUser(logUser);
          g.setMpwd(mpwd);
          g.setMuser(muser);
          g.setInnerIp(innerIp);
          g.setInnerPort(innerPort);
          s.update(g);
        }
      }
      return true;
    }
    return false;
  }

  /**
   * 服务器
   *
   * @param id
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public GameServer getServer(long id)
  {
    Session s = dao.getSession();
    GameServer gs = s.get(GameServer.class, id);
    return gs;
  }

  /**
   * 获取合服服务器的最小id的那个gameserver
   *
   * @param id
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public GameServer getServerMinId(long id)
  {
    Session s = dao.getSession();
    GameServer gs = s.get(GameServer.class, id);
    Query q = s.createQuery("select gs from GameServer as gs where gs.address=:addr");
    q.setString("addr", gs.getAddress());
    List<GameServer> ls = q.list();
    for (GameServer ser : ls)
    {
      if (ser.getServerId() < gs.getServerId())
      {
        gs = ser;
      }
    }
    return gs;
  }

  /**
   * 启动服务器
   *
   * @param id
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public String startServer(long id)
  {
    GameServer gs = this.getServerMinId(id);
    if (gs != null)
    {
      String ip = gs.getInnerIp();
      int port = gs.getInnerPort();
      String user = gs.getMuser();
      String pwd = gs.getMpwd();
      String dir = "/data/release" + gs.getServerId();
      net.schmizz.sshj.connection.channel.direct.Session s = null;
      SSHClient ssh = null;
      try
      {
        DefaultConfig defaultConfig = new DefaultConfig();
        defaultConfig.setKeepAliveProvider(KeepAliveProvider.KEEP_ALIVE);
        ssh = new SSHClient(defaultConfig);
        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        ssh.connect(ip, port);
        ssh.authPassword(user, pwd);
        s = ssh.startSession();
        StringBuilder sb = new StringBuilder();
        sb.append("cd ").append(dir).append("\n");
        sb.append("./start.sh");
        net.schmizz.sshj.connection.channel.direct.Session.Command c = s.exec(sb.toString());
        c.join(1, TimeUnit.SECONDS);
        String out = IOUtils.readFully(c.getInputStream()).toString();
        out = out + " exit code: " + c.getExitStatus();
        return out;
      } catch (Exception e)
      {
        return ExceptionUtil.stackTrace(e);
      } finally
      {
        try
        {
          if (s != null)
          {
            s.close();
          }
          if (ssh != null)
          {
            ssh.close();
          }
        } catch (Exception e)
        {
        }
      }
    }
    return "unknow exception";
  }

  /**
   * 停止服务器
   *
   * @param id
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public String stopServer(long id)
  {
    GameServer gs = this.getServerMinId(id);
    if (gs != null)
    {
      String ip = gs.getInnerIp();
      int port = gs.getInnerPort();
      String user = gs.getMuser();
      String pwd = gs.getMpwd();
      String dir = "/data/release" + gs.getServerId();
      net.schmizz.sshj.connection.channel.direct.Session s = null;
      SSHClient ssh = null;
      try
      {
        DefaultConfig defaultConfig = new DefaultConfig();
        defaultConfig.setKeepAliveProvider(KeepAliveProvider.KEEP_ALIVE);
        ssh = new SSHClient(defaultConfig);
        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        ssh.connect(ip, port);
        ssh.authPassword(user, pwd);
        s = ssh.startSession();
        StringBuilder sb = new StringBuilder();
        sb.append("cd ").append(dir).append("\n");
        sb.append("./stop.sh");
        net.schmizz.sshj.connection.channel.direct.Session.Command c = s.exec(sb.toString());
        c.join(1, TimeUnit.SECONDS);
        String out = IOUtils.readFully(c.getInputStream()).toString();
        out = out + " exit code: " + c.getExitStatus();
        return out;
      } catch (Exception e)
      {
        return ExceptionUtil.stackTrace(e);
      } finally
      {
        try
        {
          if (s != null)
          {
            s.close();
          }
          if (ssh != null)
          {
            ssh.close();
          }
        } catch (Exception e)
        {
        }
      }
    }
    return "unknow exception";
  }

  /**
   * 更新
   *
   * @param id
   * @param lastVersion
   * @return
   */
  @Transactional
  public String updateServer(long id, String lastVersion)
  {
    GameServer gs = this.getServerMinId(id);
    if (gs != null && lastVersion != null && new File(lastVersion).exists())
    {
      String ip = gs.getInnerIp();
      int port = gs.getInnerPort();
      String user = gs.getMuser();
      String pwd = gs.getMpwd();
      String dest = "/data/release" + gs.getServerId();
      SSHClient ssh = null;
      try
      {
        DefaultConfig defaultConfig = new DefaultConfig();
        defaultConfig.setKeepAliveProvider(KeepAliveProvider.KEEP_ALIVE);
        ssh = new SSHClient(defaultConfig);
        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        ssh.connect(ip, port);
        ssh.authPassword(user, pwd);
        ssh.newSCPFileTransfer().upload(lastVersion, dest);
        gs.setLastRelease(lastVersion);
        dao.update(gs);
        return "ok";
      } catch (Exception e)
      {
        return ExceptionUtil.stackTrace(e);
      } finally
      {
        if (ssh != null)
        {
          try
          {
            ssh.close();
          } catch (Exception e)
          {
          }
        }
      }
    }
    return "unknow exception";
  }

  /**
   * 最近登录
   *
   * @param uid
   * @param g
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<GameServer> getRecentServers(long uid, Game g)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("select h from LoginHistory as h where h.gid=:gid and h.uid=:uid order by h.lastTime desc");
    q.setInteger("gid", g.getGameId());
    q.setLong("uid", uid);
    q.setFirstResult(0);
    q.setMaxResults(3);
    q.setCacheable(true);
    List<LoginHistory> hs = q.list();
    List<GameServer> r = new ArrayList<>();
    if (hs != null)
    {
      for (LoginHistory lh : hs)
      {
        GameServer gs = this.getServer(g.getId(), lh.getSid());
        if (gs != null)
        {
          r.add(gs);
        }
      }
    }
    return r;
  }

  /**
   * 服务器列表
   *
   * @param i
   * @param channel
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<GameServer> getServerListByGameId(long i, String channel)
  {
    List<GameServer> gss = new ArrayList<>();
    Session s = dao.getSession();
    Query q = s.createQuery("select c from Channel as c where c.name=:name and c.gid=:gid");
    q.setString("name", channel);
    q.setLong("gid", i);
    q.setCacheable(true);
    Channel ch = (Channel) q.uniqueResult();
    if (ch != null)
    {
      q = s.createQuery("from GameServer gs where gs.gameId=:gameId order by gs.id desc");
      q.setLong("gameId", i);
      q.setCacheable(true);
      List<GameServer> r = q.list();
      for (GameServer item : r)
      {
        String tags = item.getTags();
        if (tags != null && !tags.isEmpty())
        {
          try
          {
            JSONArray arr = JSONArray.parseArray(tags);
            if (arr.contains(ch.getTag()))
            {
              gss.add(item);
            }
          } catch (Exception e)
          {
            LOG.error(ExceptionUtil.stackTrace(e));
          }
        }
      }
    }
    return gss;
  }
}
