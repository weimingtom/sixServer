/**
 * 处理脚本执行等操作
 */
package org.ngame.server.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import org.ngame.annotation.RiverMapping;
import org.ngame.app.Application;
import org.ngame.script.ScriptEngine;
import org.ngame.service.ScriptService;
import org.ngame.util.DESMD5;
import org.ngame.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author beykery
 */
@Component
public class ScriptHandler
{

  @Autowired
  private ScriptService ss;
  @Autowired
  private ScriptEngine engine;

  /**
   * 删除脚本
   *
   * @param query
   * @param out
   * @throws java.io.IOException
   */
  @RiverMapping(value = "script/deleteScript.pf", method = "POST")
  public void delete(Map<String, String[]> query, OutputStream out) throws IOException
  {
    String r = ss.deleteScript(query.get("file")[0]);
    Result re = new Result();
    re.setSuccess(r == null);
    if (r != null)
    {
      re.setInfo(r);
    }
    out.write(JSON.toJSONString(re).getBytes("utf-8"));
  }

  /**
   * 推送脚本
   *
   * @param in
   * @param out
   * @throws IOException
   */
  @RiverMapping(value = "script/pushScript.pf", method = "POST")
  public void push(InputStream in, OutputStream out) throws Exception
  {
    byte[] content = HttpUtils.content(in);
    String q = new String(DESMD5.decrypt(content, Application.DES_KEY), "UTF-8");
    int index = q.indexOf('|');
    Object r = ss.pushScript(q.substring(0, index), q.substring(index + 1));
    Result re = new Result();
    re.setSuccess(r == null);
    if (r != null)
    {
      re.setInfo(engine.isPrimitive(r) ? r.toString() : JSON.toJSONString(r));
    }
    out.write(JSON.toJSONString(re).getBytes("utf-8"));
  }

  /**
   * 执行脚本
   *
   * @param in
   * @param out
   * @throws IOException
   */
  @RiverMapping(value = "script/exeScript.pf", method = "POST")
  public void exe(InputStream in, OutputStream out) throws IOException, Exception
  {
    byte[] content = HttpUtils.content(in);
    String q = new String(DESMD5.decrypt(content, Application.DES_KEY), "UTF-8");
    int index = q.indexOf('|');
    Object r = ss.exeScript(q.substring(0, index), q.substring(index + 1));
    Result re = new Result();
    re.setSuccess(r == null);
    if (r != null)
    {
      re.setInfo(engine.isPrimitive(r) ? r.toString() : JSON.toJSONString(r));
    }
    out.write(JSON.toJSONString(re).getBytes("utf-8"));
  }
}
