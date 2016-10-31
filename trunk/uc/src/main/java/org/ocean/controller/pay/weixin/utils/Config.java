/**
 * 微信支付配置
 */
package org.ocean.controller.pay.weixin.utils;

/**
 *
 * @author beykery
 */
public class Config
{

  public static String appid = "wx52d56e1edce27e110";
  public static String mch_id = "12361786012";
  public static String key = "413347275599095525723695658024413";
  public static String notify_url = "http://ocean.nookjoy.com/ocean/wpaynotify.pf";
  public static String trade_type = "NATIVE";

  public static String getAppid()
  {
    return appid;
  }

  public static void setAppid(String appid)
  {
    Config.appid = appid;
  }

  public static String getKey()
  {
    return key;
  }

  public static void setKey(String key)
  {
    Config.key = key;
  }

  public static String getMch_id()
  {
    return mch_id;
  }

  public static void setMch_id(String mch_id)
  {
    Config.mch_id = mch_id;
  }

  public static String getNotify_url()
  {
    return notify_url;
  }

  public static void setNotify_url(String notify_url)
  {
    Config.notify_url = notify_url;
  }

  public static String getTrade_type()
  {
    return trade_type;
  }

  public static void setTrade_type(String trade_type)
  {
    Config.trade_type = trade_type;
  }
}
