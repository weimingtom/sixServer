/**
 * 处理时间相关
 */
package org.ocean.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author beykery
 */
public class TimeUtil
{

  public final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public final static SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);

  /**
   * 格式化时间
   *
   * @param d
   * @return
   */
  public static String format(Date d)
  {
    return df.format(d);
  }

  /**
   * 解析时间
   *
   * @param t
   * @return
   */
  public static Date parse(String t)
  {
    try
    {
      return df.parse(t);
    } catch (ParseException ex)
    {
      return null;
    }
  }

  /**
   * 转换
   *
   * @param t
   * @param format
   * @return
   */
  public static Date parse(String t, String format)
  {
    try
    {
      SimpleDateFormat sf = new SimpleDateFormat(format);
      return sf.parse(t);
    } catch (ParseException ex)
    {
      return null;
    }
  }
}
