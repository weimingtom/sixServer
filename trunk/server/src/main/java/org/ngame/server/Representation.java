/**
 * controller的mapping分析
 */
package org.ngame.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.ngame.Spring;
import org.ngame.annotation.RequestMapping;
import org.ngame.socket.NClient;

/**
 *
 * @author beykery
 */
public class Representation
{
  
  private static final Map<String, Representation> res = new HashMap<>();
  private final String className;
  private Object entity;
  private final Map<String, Method> cons = new HashMap<>();//方法映射
  private final Map<Method, Class[]> ps = new HashMap<>();//参数类型

  /**
   * @param className
   * @return
   * @throws java.lang.ClassNotFoundException
   */
  public static Representation get(String className) throws ClassNotFoundException
  {
    Representation re = res.get(className);
    if (re == null)
    {
      re = new Representation(className);
      re.init();
      res.put(className, re);
    }
    return re;
  }
  
  private Representation(String className)
  {
    this.className = className;
  }

  /**
   * 初始化
   *
   * @throws ClassNotFoundException
   */
  private void init() throws ClassNotFoundException
  {
    Class claz = Class.forName(className);
    this.entity = Spring.bean(claz);
    Method[] ms = claz.getMethods();
    for (Method m : ms)
    {
      RequestMapping rm = m.getAnnotation(RequestMapping.class);
      if (rm != null)
      {
        String v = rm.value();
        if (!isEmpty(v))
        {
          cons.put(v, m);
          Class<?>[] pts = m.getParameterTypes();
          ps.put(m, pts);
        }
      }
    }
  }

  /**
   * entity
   *
   * @return
   */
  public Object getEntity()
  {
    return entity;
  }

  /**
   * 映射的方法
   *
   * @param m
   * @return
   */
  private Method mapping(String m)
  {
    return this.cons.get(m);
  }
  
  private static boolean isEmpty(String v)
  {
    return v == null || v.isEmpty();
  }

  /**
   * 调用方法
   *
   * @param mapping
   * @param server
   * @param conn
   * @param cmd
   * @param request
   * @param routeid
   * @param sessionid
   * @param sameProcess
   * @return
   * @throws java.lang.IllegalAccessException
   * @throws java.lang.reflect.InvocationTargetException
   */
  public Object invoke(String mapping, BaseServer server, NClient conn, int cmd, Object request, long routeid, String sessionid, boolean sameProcess) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
  {
    Method m = this.mapping(mapping);
    Class[] types = this.ps.get(m);
    Object[] params = new Object[types.length];
    Context context = Context.current(server, conn, cmd, request, routeid, sessionid, sameProcess);
    for (int i = 0; i < types.length; i++)
    {
      Class c = types[i];
      if (c.equals(Context.class))//context
      {
        params[i] = context;
      } else if (c.equals(request.getClass()))
      {
        params[i] = request;
      } else
      {
        throw new IllegalArgumentException("不能包含Context和" + request.getClass() + "之外的类型：" + c);
      }
    }
    Object r = m.invoke(entity, params);
    if (r != null)
    {
      context.send(r);
    }
    return r;
  }
}
