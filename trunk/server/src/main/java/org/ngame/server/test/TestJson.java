/**
 * json测试
 */
package org.ngame.server.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.bouncycastle.util.encoders.UrlBase64;
import org.ngame.util.DESMD5;

/**
 *
 * @author beykery
 */
public class TestJson
{

  public static void main(String... args)
  {
    String t = "gcGSDx11_MkAp2yTGczHlN1FHrP-bWPpXREk5xeVzdNnbh5F8Yn0m75LAuZZZQbTrguX6ewl30s.";
    JSONObject json;
    long ucenterId = -1;
    boolean testing = false;
    String userCode = null;
    try
    {
      json = JSON.parseObject(new String(DESMD5.decrypt(UrlBase64.decode(t.getBytes("UTF-8")), "421w6tW1ivg=")));
      ucenterId = json.getLong("id");
      testing = json.getBooleanValue("test");
      userCode = json.getString("usercode");
    } catch (Exception e)
    {
      e.printStackTrace();
    }
    System.out.println(userCode);
    System.out.println(testing);
    System.out.println(ucenterId);
  }
}
