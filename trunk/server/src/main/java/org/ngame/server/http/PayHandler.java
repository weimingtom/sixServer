/**
 * 充值
 */
package org.ngame.server.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import org.ngame.Spring;
import org.ngame.annotation.RiverMapping;
import org.ngame.domain.Player;
import org.ngame.script.ScriptEngine;
import org.ngame.service.PlayerService;
import org.ngame.service.ChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author beykery
 */
@Component
public class PayHandler
{

  @Autowired
  private PlayerService ps;
  @Autowired
  private ScriptEngine engine;

  /**
   * 充值到账
   *
   * @param query
   * @param out
   * @throws IOException
   */
  @RiverMapping(value = "pay/pay.pf", method = "POST")
  public void playerInfo(Map<String, String[]> query, OutputStream out) throws IOException
  {
    String oid = query.get("oid")[0];
    int type = Integer.parseInt(query.get("type")[0]);
    double price = Double.parseDouble(query.get("price")[0]);
    long uid = Long.parseLong(query.get("ucenterId")[0]);
    int sid = Integer.parseInt(query.get("serverid")[0]);
    Player p = ps.getPlayer(uid, sid);
    Result re = new Result();
    re.setSuccess(p != null);
    if (p != null)
    {
      Spring.bean(ChargeService.class).charge(p, type, price);
      re.setInfo("充值成功");
    } else
    {
      re.setInfo("充值失败，不存在指定的玩家：" + sid + ":" + uid);
    }
    out.write(JSON.toJSONString(re).getBytes("utf-8"));
  }
}
