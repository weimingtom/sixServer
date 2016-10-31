/**
 * 处理spring上下文相关
 */
package org.ocean;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 *
 * @author beykery
 */
public final class Spring implements ApplicationContextAware
{

  private static ApplicationContext context;

  @SuppressWarnings("static-access")
  @Override
  public void setApplicationContext(ApplicationContext contex) throws BeansException
  {
    this.context = contex;
  }

  /**
   * 获取bean
   *
   * @param <T>
   * @param c
   * @return
   */
  public static <T> T bean(Class<T> c)
  {
    return context.getBean(c);
  }

  /**
   * 获取bean
   *
   * @param name
   * @return
   */
  public static Object bean(String name)
  {
    return context.getBean(name);
  }

  /**
   * message
   *
   * @param key
   * @return
   */
  public static String message(String key)
  {
    try
    {
      Locale l = getLocale();
      return context.getMessage(key, null, l);
    } catch (Exception e)
    {
      return key;
    }
  }

  /**
   * message 带参数的
   *
   * @param key
   * @param args
   * @return
   */
  public static String message(String key, String... args)
  {
    try
    {
      Locale l = getLocale();
      return context.getMessage(key, args, l);
    } catch (Exception e)
    {
      return key;
    }
  }

  /**
   * context
   *
   * @return
   */
  public ApplicationContext context()
  {
    return context;
  }

  /**
   * 当前请求对象
   *
   * @return
   */
  private static HttpServletRequest getRequest()
  {
    return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
  }

  /**
   * 当前locale
   *
   * @return
   */
  private static Locale getLocale()
  {
    Locale l = Locale.CHINA;
    try
    {
      HttpServletRequest request = getRequest();
      if (request != null)
      {
        l = RequestContextUtils.getLocale(request);
      }
    } catch (Exception e)
    {
    }
    return l;
  }
}
