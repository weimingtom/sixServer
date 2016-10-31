/**
 * map操作
 */
package org.ocean.util;

import com.alibaba.fastjson.JSONObject;
import java.util.Map;

/**
 *
 * @author beykery
 */
public class MapUtil
{

  /**
   * 转换成字符串输出
   *
   * @param ps
   * @return
   */
  public static String toString(Map<String, String> ps)
  {
    JSONObject json = new JSONObject();
    json.putAll(ps);
    return json.toString();
  }

}
