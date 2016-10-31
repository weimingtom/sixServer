/**
 * 登陆账号服务
 */
package org.ocean.service;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.ocean.dao.BaseDao;
import org.ocean.domain.Account;
import org.ocean.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author beykery
 */
@Component
public class AccountService
{

  @Autowired
  private BaseDao dao;

  /**
   * 计算account
   *
   * @param email
   * @param pass
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public Account getAccount(String email, String pass)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("from Account a where a.email=:email and a.pass=:pass");
    q.setString("email", email);
    q.setString("pass", pass);
    q.setCacheable(true);
    Account account = (Account) q.uniqueResult();
    return account;
  }

  /**
   * 获取所有的role
   *
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<Role> getRoles()
  {
    Session s = dao.getSession();
    Query q = s.createQuery("from Role");
    q.setCacheable(true);
    return q.list();
  }

  /**
   * account列表
   *
   * @param page
   * @param rows
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<Account> accountList(int page, int rows)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("from Account a order by a.createTime desc");
    q.setFirstResult((page - 1) * rows);
    q.setMaxResults(rows);
    q.setCacheable(true);
    return q.list();
  }

  /**
   * account数量
   *
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public long accountTotal()
  {
    Session s = dao.getSession();
    Query q = s.createQuery("select count(a.id) from Account a");
    q.setCacheable(true);
    long c = (Long) q.uniqueResult();
    return c;
  }

  /**
   * 删除account
   *
   * @param aid
   * @return
   */
  public boolean deleteAccount(long aid)
  {
    Account a = dao.get(Account.class, aid);
    if (a != null)
    {
      return dao.delete(a);
    }
    return false;
  }

}
