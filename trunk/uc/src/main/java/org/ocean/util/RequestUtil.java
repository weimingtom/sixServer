/**
 * 处理请求
 */
package org.ocean.util;

import java.util.Map;

/**
 *
 * @author beykery
 */
public class RequestUtil
{

  /**
   * String
   *
   * @param m
   * @param key
   * @param def
   * @return
   */
  public static String getStringParam(Map<String, String[]> m, String key, String def)
  {
    String[] v = m.get(key);
    if (isEmpty(v))
    {
      return def;
    }
    return notEmptyItem(v);
  }

  /**
   * int值
   *
   * @param m
   * @param key
   * @param def
   * @return
   */
  public static int getIntParam(Map<String, String[]> m, String key, int def)
  {
    String v = getStringParam(m, key, null);
    if (v != null)
    {
      return Integer.parseInt(v);
    }
    return def;
  }

  /**
   * long值
   *
   * @param m
   * @param key
   * @param def
   * @return
   */
  public static long getLongParam(Map<String, String[]> m, String key, long def)
  {
    String v = getStringParam(m, key, null);
    if (v != null)
    {
      return Long.parseLong(v);
    }
    return def;
  }

  /**
   * 是否为空
   *
   * @param v
   * @return
   */
  private static boolean isEmpty(String[] v)
  {
    if (v == null || v.length <= 0)
    {
      return true;
    }
    for (String item : v)
    {
      if (!isEmpty(item))
      {
        return false;
      }
    }
    return true;
  }

  /**
   * 是否为空串
   *
   * @param item
   * @return
   */
  private static boolean isEmpty(String item)
  {
    return item == null || item.isEmpty();
  }

  /**
   * 取一个非空元素
   *
   * @param v
   * @return
   */
  private static String notEmptyItem(String[] v)
  {
    for (String item : v)
    {
      if (!isEmpty(item))
      {
        return item;
      }
    }
    return null;
  }

}
