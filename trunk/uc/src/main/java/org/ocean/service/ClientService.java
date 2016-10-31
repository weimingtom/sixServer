/**
 * 客户端版本管理
 */
package org.ocean.service;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.ocean.dao.BaseDao;
import org.ocean.domain.Channel;
import org.ocean.domain.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author beykery
 */
@Component
public class ClientService
{

  @Autowired
  private BaseDao dao;

  /**
   * 版本列表
   *
   * @param gid
   * @param page
   * @param rows
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<Client> getClients(long gid, int page, int rows)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("select c from Client c where c.gid=:gid");
    q.setLong("gid", gid);
    q.setFirstResult((page - 1) * rows);
    q.setMaxResults(rows);
    q.setCacheable(true);
    return q.list();
  }

  /**
   * 数量
   *
   * @param gid
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public long clientTotal(long gid)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("select count(c.id) from Client c where c.gid=:gid");
    q.setLong("gid", gid);
    q.setCacheable(true);
    return (long) q.uniqueResult();
  }

  /**
   * 计算绑定的渠道
   *
   * @param clientId
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<Channel> getChannelsByClientId(long clientId)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("select c from Channel c where c.clid=:clid");
    q.setLong("clid", clientId);
    q.setCacheable(true);
    return q.list();
  }

  /**
   * 删除client
   *
   * @param id
   * @return
   */
  public boolean deleteClient(long id)
  {
    Client c = dao.get(Client.class, id);
    if (c != null)
    {
      return dao.delete(c);
    }
    return false;
  }

  /**
   * 计算游戏的全部渠道
   *
   * @param gid
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<Channel> getChannelsByGameId(long gid)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("select c from Channel c where c.gid=:gid");
    q.setLong("gid", gid);
    q.setCacheable(true);
    return q.list();
  }

  /**
   * 清除所有的channel绑定
   *
   * @param cid client id
   */
  @Transactional
  public void clearBinding(long cid)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("update Channel c set c.clid=0 where c.clid=:clid");
    q.setLong("clid", cid);
    q.executeUpdate();
  }

  /**
   * 查询client
   *
   * @param id
   * @return
   */
  public Client getClientById(long id)
  {
    return dao.get(Client.class, id);
  }

}
