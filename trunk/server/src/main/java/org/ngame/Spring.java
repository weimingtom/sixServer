/**
 * 用来启动
 */
package org.ngame;

import java.util.Locale;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 *
 * @author Beykery
 */
public class Spring
{

  private static final Logger LOG = Logger.getLogger(Spring.class);
  private static Locale defaultLocale = Locale.CHINA;
  private static ApplicationContext springContext;

  static
  {
    try
    {
      String lang = System.getProperty("i18n.locale.lang");
      String country = System.getProperty("i18n.locale.country");
      defaultLocale = new Locale(lang, country);
    } catch (Exception e)
    {
      LOG.error("locale读取失败");
    }
  }

  /**
   * 初始化
   */
  public static void init()
  {
    springContext = new FileSystemXmlApplicationContext("./spring.xml");
  }

  /**
   * spring上下文
   *
   * @return
   */
  public static ApplicationContext context()
  {
    return springContext;
  }

  /**
   * 获取bean
   *
   * @param <T>
   * @param claz
   * @return
   */
  public static <T extends Object> T bean(Class<T> claz)
  {
    return springContext.getBean(claz);
  }

  /**
   * message
   *
   * @param key
   * @return
   */
  public static String message(String key)
  {
    return springContext.getMessage(key, null, key, defaultLocale);
  }

  /**
   * message
   *
   * @param key
   * @param l
   * @param ps
   * @return
   */
  public static String message(String key, Object[] ps, Locale l)
  {
    return springContext.getMessage(key, ps, key, l);
  }

  /**
   * message
   *
   * @param key
   * @param ps
   * @return
   */
  public static String message(String key, Object... ps)
  {
    return springContext.getMessage(key, ps, key, defaultLocale);
  }
}
