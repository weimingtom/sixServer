/**
 * 渠道服务
 */
package org.ocean.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.ocean.dao.BaseDao;
import org.ocean.domain.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author beykery
 */
@Component
public class ChannelService
{

  @Autowired
  private BaseDao dao;

  /**
   * 添加一个channel
   *
   * @param gid
   * @param cid
   * @param name
   * @param tag
   * @param note
   * @param bid
   * @return
   */
  public boolean addChannel(Long gid, int cid, String name, String tag, String note, String bid)
  {
    if (gid != null)
    {
      Channel c = new Channel();
      c.setGid(gid);
      c.setCid(cid);
      c.setName(name);
      c.setTag(tag);
      c.setNote(note);
      c.setCreateTime(new Date());
      c.setBid(bid);
      c = dao.saveOrUpdate(c);
      return c != null;
    }
    return false;
  }

  /**
   * 修改channel
   *
   * @param id
   * @param cid
   * @param name
   * @param tag
   * @param note
   * @param bid
   * @return
   */
  @Transactional
  public boolean editChannel(long id, int cid, String name, String tag, String note, String bid)
  {
    Session s = dao.getSession();
    Channel c = s.get(Channel.class, id);
    if (c != null)
    {
      c.setCid(cid);
      c.setName(name);
      c.setTag(tag);
      c.setNote(note);
      c.setBid(bid);
      s.update(c);
      s.flush();
      return true;
    }
    return false;
  }

  /**
   * 删除渠道
   *
   * @param id
   * @return
   */
  @Transactional
  public boolean deleteChannel(long id)
  {
    Session s = dao.getSession();
    Channel c = s.get(Channel.class, id);
    if (c != null)
    {
      s.delete(c);
      s.flush();
      return true;
    }
    return false;
  }

  /**
   * 分页数据
   *
   * @param gid
   * @param page
   * @param rows
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<Channel> channelList(Long gid, int page, int rows)
  {
    if (gid != null)
    {
      Session s = dao.getSession();
      Query q = s.createQuery("from Channel c where c.gid=:gid order by c.id desc");
      q.setLong("gid", gid);
      q.setFirstResult((page - 1) * rows);
      q.setMaxResults(rows);
      q.setCacheable(true);
      return q.list();
    }
    return Collections.EMPTY_LIST;
  }

  /**
   * 总数量
   *
   * @param gid
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public long channelTotal(Long gid)
  {
    if (gid != null)
    {
      Session s = dao.getSession();
      Query q = s.createQuery("select count(c.id) from Channel c where c.gid=:gid");
      q.setLong("gid", gid);
      q.setCacheable(true);
      return (Long) q.uniqueResult();
    }
    return 0;
  }

  /**
   * 查询channel
   *
   * @param cid
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public Channel getChannelByCid(int cid)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("from Channel c where c.cid=:cid");
    q.setLong("cid", cid);
    q.setCacheable(true);
    return (Channel) q.uniqueResult();
  }

  /**
   * 查找channel
   *
   * @param gid
   * @param cname
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  Channel getChannel(long gid, String cname)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("select c from Channel as c where c.name=:name and c.gid=:gid");
    q.setString("name", cname);
    q.setLong("gid", gid);
    q.setCacheable(true);
    Channel ch = (Channel) q.uniqueResult();
    return ch;
  }
}
