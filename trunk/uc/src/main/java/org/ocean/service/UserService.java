/**
 * user操作
 */
package org.ocean.service;

import com.alibaba.fastjson.JSON;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.ocean.controller.man.Result;
import org.ocean.controller.pay.Player;
import org.ocean.dao.BaseDao;
import org.ocean.domain.Game;
import org.ocean.domain.GameServer;
import org.ocean.domain.LoginHistory;
import org.ocean.domain.User;
import org.ocean.util.ExceptionUtil;
import org.ocean.util.HttpUtil;
import org.ocean.Spring;
import org.ocean.util.DESMD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author beykery
 */
@Component
public class UserService
{

  private static final Logger LOG = Logger.getLogger(UserService.class);
  @Autowired
  private BaseDao dao;
  @Autowired
  private KeyWordsService keyWordsService;
  private static final int USERCODE_MAX_LENGTH = 16;
  private static final int USERCODE_MIN_LENGTH = 3;
  private static final int PASSWORD_MAX_LENGTH = 16;
  private static final int PASSWORD_MIN_LENGTH = 6;
  private static final String PRE_ANY = "any_";
  private static final String PRE_GUEST = "guest_";

  /**
   * 是否合法用户名
   *
   * @param userCode
   * @return
   */
  public String checkUserCodeValidity(String userCode)
  {
    String message = null;
    if (userCode.length() > USERCODE_MAX_LENGTH)
    {
      message = Spring.message("user_usercode_length_too_long");
    } else if (userCode.length() < USERCODE_MIN_LENGTH)
    {
      message = Spring.message("user_usercode_length_too_short");
    } else if (!keyWordsService.validate(userCode))
    {
      message = Spring.message("user_usercode_sensitive_word");
    }
    return message;
  }

  /**
   * 检查密码是否合法
   *
   * @param password
   * @return
   */
  public String checkPasswordValidity(String password)
  {
    String message = null;
    if (password.length() > PASSWORD_MAX_LENGTH)
    {
      message = Spring.message("user_password_length_too_long");
    } else if (password.length() < PASSWORD_MIN_LENGTH)
    {
      message = Spring.message("user_password_length_too_short");
    }
    return message;
  }

  /**
   * 用户名密码校验
   *
   * @param userName
   * @param userPassword
   * @return
   */
  public String checkUsercodeAndPassword(String userName, String userPassword)
  {
    String r = this.checkUserCodeValidity(userName);
    if (r == null)
    {
      r = this.checkPasswordValidity(userPassword);
    }
    return r;
  }

  /**
   * 是否已经存在
   *
   * @param userCode
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public boolean userCodeExists(String userCode)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("from User as u where u.usercode=:code");
    q.setString("code", userCode);
    q.setCacheable(true);
    User u = (User) q.uniqueResult();
    return u != null;
  }

  /**
   * 创建一个新用户账号
   *
   * @param userCode
   * @param password
   * @param channelId
   * @param testing
   * @param unlockTime
   * @param phoneMsg
   * @param params
   * @param originGame
   * @return
   */
  public User createUser(String userCode, String password, int channelId, boolean testing, Date unlockTime, String phoneMsg, String params, long originGame)
  {
    if (!(userCode.startsWith(PRE_ANY) || userCode.startsWith(PRE_GUEST)))
    {
      User u = new User(userCode, password, channelId, phoneMsg, params);
      u.setTesting(testing);
      u.setUnlockTime(unlockTime);
      u.setOriginGame(originGame);
      u.setRegTime(new Date());
      return dao.saveOrUpdate(u);
    }
    return null;
  }

  /**
   * any登陆
   *
   * @param channel
   * @param uid
   * @param channelid
   * @param gid
   * @return
   */
  @Transactional
  public User anyLogin(String channel, String uid, int channelid, long gid)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("from User as u where u.anyChannel=:channel and u.anyUid=:uid");
    q.setString("channel", channel);
    q.setString("uid", uid);
    q.setCacheable(true);
    User u = (User) q.uniqueResult();
    if (u == null)//为空则创建一个
    {
      u = new User(channel + "_" + uid, UUID.randomUUID().toString(), channelid, null, null);
      u.setTesting(false);
      u.setOriginGame(gid);
      u.setRegTime(new Date());
      s.save(u);
      u.setUsercode("any_" + u.getId());
      s.update(u);
    }
    return u;
  }

  /**
   * 用户名密码登陆
   *
   * @param userCode
   * @param password
   * @return
   */
  @Transactional
  public User userLogin(String userCode, String password)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("from User as u where u.usercode=:code");
    q.setString("code", userCode);
    q.setCacheable(true);
    User u = (User) q.uniqueResult();
    if (u != null)
    {
      if (u.getPassword() != null)
      {
        if (u.getPassword().equals(password))
        {
          return u;
        }
      } else if (u.getPassmd5() != null)
      {
        String md5 = DESMD5.MD5Encoder(password, "utf-8");
        if (md5.equals(u.getPassmd5()))
        {
          u.setPassword(password);
          s.update(u);
          s.flush();
          return u;
        }
      }
    }
    return null;
  }

  /**
   * 修改用户账号信息
   *
   * @param id
   * @param userName
   * @param userPassword
   * @param userChannelid
   * @param testing
   * @param unlockTime
   * @return
   */
  public boolean editUser(long id, String userName, String userPassword, int userChannelid, boolean testing, Date unlockTime)
  {
    User u = dao.get(User.class, id);
    if (u != null)
    {
      u.setUsercode(userName);
      u.setPassword(userPassword);
      u.setChannelId(userChannelid);
      u.setTesting(testing);
      u.setUnlockTime(unlockTime);
      u = dao.saveOrUpdate(u);
      return u != null;
    }
    return false;
  }

  /**
   * 删除user
   *
   * @param id
   * @return
   */
  public boolean deleteUser(long id)
  {
    User u = dao.get(User.class, id);
    if (u != null)
    {
      return dao.delete(u);
    }
    return false;
  }

  /**
   * 用户列表
   *
   * @param page
   * @param rows
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<User> userList(int page, int rows)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("from User u order by u.id desc");
    q.setFirstResult((page - 1) * rows);
    q.setMaxResults(rows);
    List<User> r = q.list();
    return r;
  }

  /**
   * 用户数量
   *
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public long userTotal()
  {
    Session s = dao.getSession();
    Query q = s.createQuery("select count(u.id) from User u");
    long c = (Long) q.uniqueResult();
    return c;
  }

  /**
   * 查找user
   *
   * @param uid
   * @return
   */
  public User getUser(long uid)
  {
    return dao.get(User.class, uid);
  }

  /**
   * 查找user
   *
   * @param uname
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public User getUser(String uname)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("from User u where u.usercode=:name");
    q.setString("name", uname);
    q.setCacheable(true);
    User u = (User) q.uniqueResult();
    return u;
  }

  /**
   * 玩家数据
   *
   * @param gameId
   * @param serverId
   * @param usercode
   * @return
   */
  public Player getPlayer(int gameId, int serverId, String usercode)
  {
    Game g = Spring.bean(GameService.class).getGame(gameId);
    GameServer gs = Spring.bean(ServerService.class).getServer(g.getId(), serverId);
    if (gs != null)
    {
      User u = this.getUser(usercode);
      String addr = gs.getAddress();
      int index = addr.indexOf(":");
      String ip = addr.substring(0, index);
      String url = "http://" + ip + ":" + gs.getRiverPort() + "/river/player/playerInfo.pf";//玩家基本数据
      StringBuilder sb = new StringBuilder();
      sb.append("serverid=").append(serverId).append("&");
      sb.append("ucenterId=").append(u.getId());
      try
      {
        byte[] r = HttpUtil.post(url, sb.toString().getBytes("UTF-8"), g.getDesKey());
        Result re = JSON.parseObject(new String(r, "utf-8"), Result.class);
        if (re.isSuccess())
        {
          return JSON.parseObject(re.getInfo(), Player.class);
        }
      } catch (Exception e)
      {
        LOG.error("获取playerInfo失败:" + ExceptionUtil.stackTrace(e));
      }
    }
    return null;
  }

  /**
   * 游客
   *
   * @param cid
   * @return
   */
  @Transactional
  public User createGuest(int cid)
  {
    User u = new User();
    u.setUsercode(UUID.randomUUID().toString());
    u.setChannelId(cid);
    u.setPassword("123456");
    Session s = dao.getSession();
    s.save(u);
    u.setUsercode("guest_" + u.getId());
    s.update(u);
    return u;
  }

  /**
   * 检查角色是否有最近登陆
   *
   * @param ucenterId
   * @param gameId
   * @param serverId
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public LoginHistory checkLoginHistory(long ucenterId, int gameId, int serverId)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("from LoginHistory l where l.uid=:ucenterId and "
            + " l.gid=:gameId and l.sid=:serverId ");
    q.setLong("ucenterId", ucenterId);
    q.setInteger("gameId", gameId);
    q.setInteger("serverId", serverId);
    q.setCacheable(true);
    LoginHistory l = (LoginHistory) q.uniqueResult();
    return l;
  }

  /**
   * 更新角色最新登陆
   *
   * @param l
   */
  @Transactional
  public void updateLoginHistory(LoginHistory l)
  {
    Session s = dao.getSession();
    s.saveOrUpdate(l);
  }

}
