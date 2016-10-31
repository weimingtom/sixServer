package org.ngame.util;

import java.sql.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @ClassName: TimeUtils
 * @Description: 时间处理
 * @author: namei
 * @date:2013-5-23 下午4:22:03
 */
public abstract class TimeUtils
{

  public final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public final static String DATE_FORMAT2 = "yyyy-MM-dd";
  public final static String DATE_FORMAT3 = "HH:mm:ss";
  public final static String DATE_STRING = "yyyyMMddhhmmssSSS";

  public static Date parseStringToDate(String time, String style)
  {
    SimpleDateFormat df = new SimpleDateFormat(style);
    Date date = null;
    try
    {
      date = df.parse(time);
    } catch (ParseException e)
    {
      e.printStackTrace();
    }
    return date;
  }

  /**
   * 格式转换 字符串转日期时间
   *
   * @param time
   * @return
   */
  public static Date parseStringToTime(String time)
  {
    SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
    Date date = null;
    try
    {
      date = df.parse(time);
    } catch (ParseException e)
    {
      e.printStackTrace();
    }
    return date;
  }

  /**
   * 格式转换 字符串转日期
   *
   * @param time
   * @return
   */
  public static Date parseStringToDate(String time)
  {
    SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT2);
    Date date = null;
    try
    {
      date = df.parse(time);
    } catch (ParseException e)
    {
      e.printStackTrace();
    }
    return date;
  }

  public static void main(String[] args)
  {
    System.out.println(millisecondToDate(1401325200026L));
  }

  public static String getDay(String time)
  {
    String times[] = time.split(" ");
    return times[0];
  }

  @SuppressWarnings("deprecation")
  public static Date getDateForMonth(Date date)
  {
    date.setDate(1);
    return date;
  }

  public static Date getDay(Date time)
  {
    SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
    String timeStr = df.format(time);
    String times[] = timeStr.toString().split(" ");
    try
    {
      time = df.parse(times[0] + " 00:00:00");
    } catch (ParseException e)
    {
      e.printStackTrace();
    }
    return time;
  }

  public static Date getScheduleTime(Date now, int num)
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(now);
    int day = calendar.get(Calendar.DAY_OF_YEAR);
    calendar.set(Calendar.DAY_OF_YEAR, day + num);
    Date d = calendar.getTime();
    return d;
  }

  /**
   * 计算时间
   *
   * @param hour
   * @param minite
   * @return
   */
  public static long getTime(int hour, int minite)
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    calendar.set(Calendar.HOUR_OF_DAY, hour);
    calendar.set(Calendar.MINUTE, minite);
    calendar.set(Calendar.SECOND, 0);
    return calendar.getTimeInMillis();
  }

  /**
   * @param time
   * @return
   */
  public static Timestamp parseStringToTimestamp(String time)
  {
    Date date = TimeUtils.parseStringToTime(time);
    return new Timestamp(date.getTime());
  }

  /**
   * 格式转换 时间串转 字符
   *
   * @param date
   * @return
   */
  public static String parseDateToString(Date date)
  {
    SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
    return df.format(date);
  }

  /**
   * 格式转换 时间串转 字符串data_formate2
   *
   * @param date
   * @return
   */
  public static String parseDateToString2(Date date)
  {
    SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT2);
    return df.format(date);
  }

  /**
   * 格式转换 时间串转 字符
   *
   * @param date
   * @return
   */
  public static String parseDateToString(Date date, String style)
  {
    SimpleDateFormat df = new SimpleDateFormat(style);
    return df.format(date);
  }

  /**
   * 格式转换 时间串转 字符
   *
   * @param timestamp
   * @return
   */
  public static String parseTimestampToString(Timestamp timestamp)
  {
    SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
    return df.format(timestamp);
  }

  /**
   * 获取当前时间
   *
   * @return
   */
  public static Timestamp getNowTime()
  {
    return new Timestamp(System.currentTimeMillis());
  }

  /**
   * 取得当前时间无格式
   */
  public static String getTimestampToString()
  {
    Timestamp timestamp = getNowTime();
    SimpleDateFormat df = new SimpleDateFormat(DATE_STRING);
    return df.format(timestamp);
  }

  /**
   * 获取本周第一天的时间
   */
  public static String getSundayDate()
  {
    int nowWeek = getNowWeek();
    Calendar calendar = Calendar.getInstance();
    // getTime()方法是取得当前的日期，其返回值是一个java.util.Date类的对象
    int day = calendar.get(Calendar.DAY_OF_YEAR);
    calendar.set(Calendar.DAY_OF_YEAR, day - nowWeek + 2);
    Date d = calendar.getTime();
    Timestamp dateTime = new Timestamp(d.getTime());
    return parseTimestampToString(dateTime);
  }

  /**
   * 获取一周前的日期
   * @return 
   */
  public static String getLastWeek()
  {
    Calendar calendar = Calendar.getInstance();
    // getTime()方法是取得当前的日期，其返回值是一个java.util.Date类的对象
    int day = calendar.get(Calendar.DAY_OF_YEAR);
    calendar.set(Calendar.DAY_OF_YEAR, day - 7);
    Date d = calendar.getTime();
    Timestamp dateTime = new Timestamp(d.getTime());
    return parseTimestampToString(dateTime);
  }

  /**
   * 获得当前星期
   */
  public static int getNowWeek()
  {
    Calendar calendar = Calendar.getInstance();
    // 获得日期在本周的天数， Sun=1, Mon=2 ... Sta=7
    int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
    return day_of_week;
  }

  /**
   * 获取当前时间，不带日期 "HH:mm:ss"
   *
   * @return String
   */
  public static String getNowTimeNoDay()
  {
    SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT3);
    Date date = new Date();
    return df.format(date);
  }

  /**
   * @param time1 "HH:mm:ss"
   * @param time2 "HH:mm:ss"
   * @return boolean
   */
  public static boolean after(String time1, String time2)
  {
    Date date1 = parseStringToDate(time1, DATE_FORMAT2);
    Date date2 = parseStringToDate(time2, DATE_FORMAT2);
    return date1.after(date2);
  }

  /**
   * 获得当前日期 yyyy-MM-dd
   *
   * @return String
   */
  public static String getNowDay()
  {
    SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT2);
    return df.format(new Date());
  }

  @SuppressWarnings("deprecation")
  public static int setDay(Date date)
  {
    // Date time=TimeUtils.getDateForMonth(date);
    int week = date.getDay();
    if (week == 0)
    {
      week = 7;
    }
    return week;
  }

  /**
   * 获得当月天数
   *
   * @param time yyyy-MM-dd
   * @return
   */
  public static int getDaysForMonth(String time)
  {
    Date dateTime = parseStringToDate(time, DATE_FORMAT2);
    Date dateTime2 = nextMonth(dateTime);
    long day = TimeUtils.getBetweenTime(dateTime, dateTime2);
    long day2 = day / 60 / 60 / 24 / 1000;
    return (int) day2;
  }

  /**
   * 获得日期的前几天日期
   *
   * @param date 日期 yyyy-MM-dd
   * @param day 前几天
   * @return yyyy-MM-dd
   */
  public static String lastDay(String date, int day)
  {
    Date date1 = parseStringToDate(date, DATE_FORMAT2);
    long datel1 = date1.getTime();
    long day1 = 60 * 60 * 24 * 1000 * day;
    long date2 = datel1 - day1;
    return parseDateToString(new Date(date2), DATE_FORMAT2);
  }

  /**
   * 获得下一个月的日期
   *
   * @param time
   * @return
   */
  public static Date nextMonth(Date time)
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(time);
    // getTime()方法是取得当前的日期，其返回值是一个java.util.Date类的对象
    int month = calendar.get(Calendar.MONTH);
    calendar.set(Calendar.MONTH, month + 1);
    return calendar.getTime();
  }

  /**
   * @return Date
   * @throws
   * @Title: getBeforeMonth
   * @Description: 获取前一个月的日期
   */
  public static Date beforeMonth(Date time)
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(time);
    // getTime()方法是取得当前的日期，其返回值是一个java.util.Date类的对象
    int month = calendar.get(Calendar.MONTH);
    calendar.set(Calendar.MONTH, month + 1);
    return calendar.getTime();
  }

  /**
   * 比较当天时间
   *
   * @param date1
   * @param date2
   * @return
   */
  public static long getBetweenTime(Date date1, Date date2)
  {
    long l1 = 0;
    long l2 = 0;
    if (date1.after(date2))
    {
      l1 = date2.getTime();
      l2 = date1.getTime();
    } else
    {
      l1 = date1.getTime();
      l2 = date2.getTime();
    }
    long l3 = l2 - l1;
    return l3;
  }

  /**
   * 比较两个日期
   *
   * @param s1
   * @param s2
   * @return
   */
  public static long compareDate(String s1, String s2)
  {
    Date a = parseStringToDate(s1);
    Date b = parseStringToDate(s2);
    long l1 = a.getTime();
    long l2 = b.getTime();
    return l2 - l1;
  }

  /**
   * 获得当月日期列表<br/>
   * 比如输入 2012-2-10<br/>
   *
   * @param time
   * @return List<String>
   */
  public static List<String> getMonthList(String time)
  {
    int days = getDaysForMonth(time);
    List<String> dayList = new ArrayList<String>();
    for (int i = 1; i <= days; i++)
    {
      String[] dateTime = time.split("-");
      String day = dateTime[0] + "-" + dateTime[1] + "-" + i;
      dayList.add(day);
    }
    return dayList;
  }

  /**
   * 两个日期比较
   *
   * @param day1 yyyy-MM-dd
   * @param day2 yyyy-MM-dd
   * @return
   */
  public static boolean timeEquals(String day1, String day2)
  {
    Date date1 = parseStringToDate(day1, DATE_FORMAT2);
    Date date2 = parseStringToDate(day2, DATE_FORMAT2);
    return date1.equals(date2);
  }

  /**
   * 获取指定时间的年份
   *
   * @param date
   * @return int
   */
  public static int getYear(Date date)
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(Calendar.YEAR);
  }

  /**
   * 按指定的格式进行时间比较
   *
   * @param time1
   * @param time2
   * @param pattern
   * @return boolean
   */
  public static boolean after(String time1, String time2, String pattern)
  {
    Date date1 = parseStringToDate(time1, pattern);
    Date date2 = parseStringToDate(time2, pattern);
    return date1.after(date2);
  }

  /**
   * 获取昨天
   *
   * @param now
   * @return
   */
  public static Date getYesterday(Date now)
  {
    GregorianCalendar gc = new GregorianCalendar();
    gc.setTime(now);
    gc.set(Calendar.HOUR_OF_DAY, 0);
    gc.set(Calendar.MINUTE, 0);
    gc.set(Calendar.SECOND, 0);
    gc.set(Calendar.MILLISECOND, 0);
    gc.add(Calendar.DATE, -1);
    return gc.getTime();
  }

  /**
   * 获取昨天
   *
   * @return
   */
  public static String getYesterdayFormat()
  {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, -1);
    String yesterday = new SimpleDateFormat(DATE_FORMAT2).format(cal.getTime());
    return yesterday;
  }

  /**
   * 得到N天后的日期
   *
   * @param num num为负值获取前num天的日期
   * @return
   */
  public static String getDate(int num)
  {
    long time = System.currentTimeMillis() + (1000L * 60 * 60 * 24 * num);
    Date date = new Date();
    if (time > 0)
    {
      date.setTime(time);
    }
    SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT2);
    return format.format(date);
  }
  /*
   * 毫秒格式化成日期类型
   */

  public static String millisecondToDate(Long time)
  {

    Date date = new Date(time);
    SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT2);
    //         String s = String.valueOf(calendar.getTimeInMillis());
    return format.format(date);
  }

  /**
   * 是否同一天
   *
   * @param t1
   * @param t2
   * @return
   */
  public static boolean isTheSameDay(long t1, long t2)
  {
    Calendar d1 = Calendar.getInstance();
    d1.setTimeInMillis(t1);
    Calendar d2 = Calendar.getInstance();
    d2.setTimeInMillis(t2);
    return d1.get(Calendar.YEAR) == d2.get(Calendar.YEAR) && d1.get(Calendar.DAY_OF_YEAR) == d2.get(Calendar.DAY_OF_YEAR);
  }

  /**
   * 计算所在月份的天
   *
   * @param m
   * @return
   */
  public static int dayofMonth(long m)
  {
    Calendar c = Calendar.getInstance();
    c.setTimeInMillis(m);
    return c.get(Calendar.DAY_OF_MONTH) - 1;
  }

  /**
   * 是否为同一个月
   *
   * @param t1
   * @param t2
   * @return
   */
  public static boolean isTheSameMonth(long t1, long t2)
  {
    Calendar d1 = Calendar.getInstance();
    d1.setTimeInMillis(t1);
    Calendar d2 = Calendar.getInstance();
    d2.setTimeInMillis(t2);
    return d1.get(Calendar.YEAR) == d2.get(Calendar.YEAR) && d1.get(Calendar.MONTH) == d2.get(Calendar.MONTH);
  }

  /**
   * 计算月份
   *
   * @param t
   * @return
   */
  public static int monthofYear(long t)
  {
    Calendar d1 = Calendar.getInstance();
    d1.setTimeInMillis(t);
    return d1.get(Calendar.MONTH);
  }
}
