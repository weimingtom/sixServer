/**
 * 订单
 */
package org.ocean.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ocean.dao.BaseDao;
import org.ocean.domain.Game;
import org.ocean.domain.GameServer;
import org.ocean.domain.Orderform;
import org.ocean.domain.User;
import org.ocean.Spring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author beykery
 */
@Component
public class OrderService
{

  @Autowired
  private BaseDao dao;

  /**
   * 查找订单
   *
   * @param gid
   * @param serverId
   * @param name
   * @param uid
   * @param start
   * @param end
   * @param page
   * @param rows
   * @param sort
   * @param order
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<Orderform> getOrders(long gid, int serverId, String name, long uid, Date start, Date end, int page, int rows, String sort, String order)
  {
    Game g = Spring.bean(GameService.class).getGameById(gid);
    if (g == null)
    {
      return Collections.EMPTY_LIST;
    }
    String usercode;
    User u = null;
    if (uid > 0)
    {
      u = Spring.bean(UserService.class).getUser(uid);
    }
    usercode = u == null ? name : u.getUsercode();
    Session s = dao.getSession();
    Criteria c = s.createCriteria(Orderform.class);
    c.add(Restrictions.eq("gameId", g.getGameId()));
    if (serverId > 0)
    {
      c.add(Restrictions.eq("serverId", serverId));
    }
    if (usercode != null && !usercode.trim().isEmpty())
    {
      c.add(Restrictions.eq("usercode", usercode));
    }
    if (start != null)
    {
      c.add(Restrictions.gt("ctime", start));
    }
    if (end != null)
    {
      c.add(Restrictions.lt("ctime", end));
    }
    if (sort == null || sort.isEmpty())
    {
      c.addOrder(Order.desc("ctime"));
    } else//带排序的
    {
      c.addOrder(order.equalsIgnoreCase("asc") ? Order.asc(sort) : Order.desc(sort));
    }
    c.setMaxResults(rows);
    c.setFirstResult((page - 1) * rows);
    return c.list();
  }

  /**
   * 查找总数
   *
   * @param gid
   * @param serverId
   * @param name
   * @param uid
   * @param start
   * @param end
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public long total(long gid, int serverId, String name, long uid, Date start, Date end)
  {
    User u = null;
    String usercode;
    if (uid > 0)
    {
      u = Spring.bean(UserService.class).getUser(uid);
    }
    usercode = u == null ? name : u.getUsercode();
    Session s = dao.getSession();
    StringBuilder sb = new StringBuilder("select count(o.id) from Orderform as o where 1=1");
    if (gid > 0)
    {
      Game g = Spring.bean(GameService.class).getGameById(gid);
      sb.append(" and o.gameId=").append(g.getGameId());
    }
    if (serverId > 0)
    {
      sb.append(" and o.serverId=").append(serverId);
    }
    if (usercode != null && !usercode.trim().isEmpty())
    {
      sb.append(" and o.usercode=:usercode");
    }
    if (start != null)
    {
      sb.append(" and o.ctime>=:start");
    }
    if (end != null)
    {
      sb.append(" and o.ctime<=:end");
    }
    Query q = s.createQuery(sb.toString());
    if (start != null)
    {
      q.setDate("start", start);
    }
    if (end != null)
    {
      q.setDate("end", end);
    }
    if (usercode != null && !usercode.trim().isEmpty())
    {
      q.setString("usercode", usercode);
    }
    return (long) q.uniqueResult();
  }

  /**
   * 服务器名字
   *
   * @param gameId
   * @param serverId
   * @return
   */
  public String getServerName(int gameId, int serverId)
  {
    Game g = Spring.bean(GameService.class).getGame(gameId);
    if (g != null)
    {
      GameServer gs = Spring.bean(ServerService.class).getServer(g.getId(), serverId);
      return gs == null ? null : gs.getName();
    }
    return null;
  }

  /**
   * 玩家usercode
   *
   * @param uid
   * @return
   */
  public String getUserName(long uid)
  {
    User u = Spring.bean(UserService.class).getUser(uid);
    return u == null ? null : u.getUsercode();
  }

  /**
   * 完成订单
   *
   * @param id
   * @param manual
   * @return
   */
  public boolean overOrder(long id, boolean manual)
  {
    Orderform of = dao.get(Orderform.class, id);
    if (of != null && of.getStatus() != Orderform.STATUS_OVER)
    {
      of.setStatus(Orderform.STATUS_OVER);
      of.setFtime(new Date());
      of.setManual(manual);
      dao.update(of);
      return Spring.bean(PayService.class).notifyPayOver(of);
    }
    return false;
  }

  /**
   * 完成订单
   *
   * @param orderId
   * @return
   */
  @Transactional
  public boolean overOrder(String orderId)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("select o from Orderform as o where o.oid=:oid");
    q.setString("oid", orderId);
    q.setCacheable(true);
    Orderform of = (Orderform) q.uniqueResult();
    if (of != null && of.getStatus() != Orderform.STATUS_OVER)
    {
      of.setStatus(Orderform.STATUS_OVER);
      of.setFtime(new Date());
      s.update(of);
      s.flush();
      return Spring.bean(PayService.class).notifyPayOver(of);
    }
    return false;
  }

  /**
   * 删除订单
   *
   * @param id
   * @return
   */
  public boolean deleteOrder(long id)
  {
    Orderform of = dao.get(Orderform.class, id);
    if (of != null)
    {
      return dao.delete(of);
    }
    return false;
  }

  /**
   * 订单总价值
   *
   * @param gid
   * @param serverId
   * @param name
   * @param uid
   * @param start
   * @param end
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public double totalPrice(long gid, int serverId, String name, long uid, Date start, Date end)
  {
    User u = null;
    String usercode;
    if (uid > 0)
    {
      u = Spring.bean(UserService.class).getUser(uid);
    }
    usercode = u == null ? name : u.getUsercode();
    Session s = dao.getSession();
    StringBuilder sb = new StringBuilder("select sum(o.price) from Orderform as o where o.status=1");
    if (gid > 0)
    {
      Game g = Spring.bean(GameService.class).getGameById(gid);
      sb.append(" and o.gameId=").append(g.getGameId());
    }
    if (serverId > 0)
    {
      sb.append(" and o.serverId=").append(serverId);
    }
    if (usercode != null && !usercode.trim().isEmpty())
    {
      sb.append(" and o.usercode=:usercode");
    }
    if (start != null)
    {
      sb.append(" and o.ctime>=:start");
    }
    if (end != null)
    {
      sb.append(" and o.ctime<=:end");
    }
    Query q = s.createQuery(sb.toString());
    if (start != null)
    {
      q.setDate("start", start);
    }
    if (end != null)
    {
      q.setDate("end", end);
    }
    if (usercode != null && !usercode.trim().isEmpty())
    {
      q.setString("usercode", usercode);
    }
    Double d = (Double) q.uniqueResult();
    return d == null ? 0 : d;
  }

  /**
   * 创建订单
   *
   * @param usercode
   * @param money
   * @param gameId
   * @param serverId
   * @param orderType
   * @param channel
   * @return
   */
  public Orderform createOrder(String usercode, int money, int gameId, int serverId, int orderType, String channel)
  {
    Orderform of = new Orderform();
    of.setPrice(money);
    of.setAnyChannel(channel);
    of.setChargeType(orderType);
    of.setGameId(gameId);
    of.setServerId(serverId);
    of.setCtime(new Date());
    of.setOid(UUID.randomUUID().toString().trim().replaceAll("-", ""));
    of.setUsercode(usercode);
    of = dao.saveOrUpdate(of);
    return of;
  }

  /**
   * 查询订单
   *
   * @param oid
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public Orderform getOrderByOid(String oid)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("select o from Orderform as o where o.oid=:oid");
    q.setString("oid", oid);
    q.setCacheable(true);
    Orderform of = (Orderform) q.uniqueResult();
    return of;
  }

}
