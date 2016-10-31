/**
 * 权限管理
 */
package org.ocean.service;

import com.alibaba.fastjson.JSONArray;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.Query;
import org.hibernate.Session;
import org.ocean.annotation.Auth;
import org.ocean.dao.BaseDao;
import org.ocean.domain.Role;
import org.ocean.util.ClassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author beykery
 */
@Component
public class AuthService
{

  @Autowired
  private BaseDao dao;
  private final Map<String, String> ops;

  public AuthService()
  {
    ops = new HashMap<>();
    Set<Class<?>> set = ClassUtil.scan("org.ocean.controller.man");
    for (Class<?> c : set)
    {
      if (c.isAnnotationPresent(Controller.class))//所有的controller
      {
        Method[] ms = c.getMethods();
        for (Method m : ms)
        {
          Auth au = m.getAnnotation(Auth.class);
          if (au != null)
          {
            RequestMapping ma = m.getAnnotation(RequestMapping.class);
            ops.put(ma.value()[0], au.note());
          }
        }
      }
    }
  }

  /**
   * 所有需要鉴权的操作
   *
   * @return
   */
  public Map<String, String> getOps()
  {
    return ops;
  }

  /**
   * 鉴权
   *
   * @param r
   * @param op
   * @return
   */
  public boolean auth(Role r, String op)
  {
    if (r != null && this.ops.containsKey(op))//需要鉴权的操作
    {
      String os = r.getOps();
      return (os == null || os.isEmpty() || os.equals("[]")) ? true : os.contains(op);
    }
    return true;
  }

  /**
   * 角色列表
   *
   * @param page
   * @param rows
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<Role> roleList(int page, int rows)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("from Role r order by r.id desc");
    q.setFirstResult((page - 1) * rows);
    q.setMaxResults(rows);
    q.setCacheable(true);
    List<Role> r = q.list();
    return r;
  }

  /**
   * 角色总量
   *
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public long roleTotal()
  {
    Session s = dao.getSession();
    Query q = s.createQuery("select count(r.id) from Role r");
    q.setCacheable(true);
    long c = (Long) q.uniqueResult();
    return c;
  }

  /**
   * 删除
   *
   * @param id
   * @return
   */
  public boolean deleteRole(long id)
  {
    Role r = dao.get(Role.class, id);
    if (r != null)
    {
      return dao.delete(r);
    }
    return false;
  }

  /**
   * 添加一个角色
   *
   * @param name
   * @param note
   * @param ops
   * @return
   */
  public boolean addRole(String name, String note, String[] ops)
  {
    Role r = new Role();
    r.setName(name);
    r.setNote(note);
    JSONArray arr = new JSONArray();
    if (ops != null && ops.length > 0)
    {
      arr.addAll(Arrays.asList(ops));
    }
    r.setOps(arr.isEmpty() ? null : arr.toString());
    return dao.saveOrUpdate(r) != null;
  }

  /**
   * 修改角色
   *
   * @param id
   * @param name
   * @param note
   * @param ops
   * @return
   */
  public boolean editRole(long id, String name, String note, String[] ops)
  {
    Role r = dao.get(Role.class, id);
    if (r != null)
    {
      r.setName(name);
      r.setNote(note);
      JSONArray arr = new JSONArray();
      if (ops != null && ops.length > 0)
      {
        arr.addAll(Arrays.asList(ops));
      }
      r.setOps(arr.isEmpty() ? null : arr.toString());
      return dao.saveOrUpdate(r) != null;
    }
    return false;
  }
}
