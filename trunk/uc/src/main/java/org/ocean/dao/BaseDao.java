/**
 * dao层
 */
package org.ocean.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.ocean.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 *
 * @author beykery
 */
@Repository
public class BaseDao
{

  private static final Logger LOG = Logger.getLogger(BaseDao.class);
  /**
   * Autowired 自动装配
   */
  @Autowired
  private SessionFactory sessionFactory;

  /**
   *
   * @return
   */
  public Session getSession()
  {
    try
    {
      Session s = sessionFactory.getCurrentSession();
      if (!s.isOpen())
      {
        LOG.error("无法从当前上下文取得数据库连接，将尝试打开新的连接");
        s = sessionFactory.openSession();
      }
      return s;
    } catch (Exception e)
    {
      LOG.error(ExceptionUtil.stackTrace(e));
      return sessionFactory.openSession();
    }
  }

  /**
   *
   * @param <T>
   * @param c
   * @param id
   * @return
   */
  @SuppressWarnings(
          {
            "rawtypes", "unchecked"
          })
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public <T> T get(Class<T> c, long id)
  {
    Session session = getSession();
    return (T) session.get(c, id);
  }

  /**
   * * 获取总数量
   *
   * @param c
   * @return
   */
  @SuppressWarnings("rawtypes")
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public Long getTotalCount(Class c)
  {
    Session session = getSession();
    String hql = "select count(*) from " + c.getName();
    Long count = (Long) session.createQuery(hql).uniqueResult();
    session.close();
    return count != null ? count : 0;
  }

  /**
   * * 保存
   *
   *
   * @param <T>
   * @param bean
   * @return
   */
  @Transactional
  public <T> T saveOrUpdate(T bean)
  {
    Session session = getSession();
    try
    {
      session.saveOrUpdate(bean);
      session.flush();
    } catch (Exception e)
    {
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      return null;
    }
    return bean;
  }

  /**
   * 更新
   *
   * @param bean
   */
  @Transactional
  public void update(Object bean)
  {
    Session session = getSession();
    try
    {
      session.update(bean);
      session.flush();
    } catch (Exception e)
    {
      e.printStackTrace();
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
  }

  /**
   * 删除
   *
   *
   * @param bean
   * @return
   */
  @Transactional
  public boolean delete(Object bean)
  {
    Session session = getSession();
    try
    {
      session.delete(bean);
      session.flush();
    } catch (Exception e)
    {
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      return false;
    }
    return true;
  }

  /**
   * 访问factory，可以获取关于二级缓存的内容
   *
   * @return
   */
  public SessionFactory getSessionFactory()
  {
    return sessionFactory;
  }

}
