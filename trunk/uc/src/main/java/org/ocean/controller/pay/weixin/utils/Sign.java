package org.ocean.controller.pay.weixin.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class Sign
{

  /**
   * 微信支付签名算法sign
   *
   * @param characterEncoding
   * @param parameters
   * @return
   */
  @SuppressWarnings("unchecked")
  public static String createSign(String characterEncoding, SortedMap<String, String> parameters, String key)
  {
    StringBuilder sb = new StringBuilder();
    Set es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
    Iterator it = es.iterator();
    while (it.hasNext())
    {
      Map.Entry entry = (Map.Entry) it.next();
      String k = (String) entry.getKey();
      Object v = entry.getValue();
      if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k))
      {
        sb.append(k).append("=").append(v).append("&");
      }
    }
    sb.append("key=").append(key);
    String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
    return sign;
  }
}
