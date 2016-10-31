/**
 * 玩家接口
 */
package org.ngame.server.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import org.ngame.annotation.RiverMapping;
import org.ngame.domain.Player;
import org.ngame.script.ScriptEngine;
import org.ngame.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author beykery
 */
@Component(value = "playerHandlerFor3part")
public class PlayerHandler
{

  @Autowired
  private PlayerService ps;
  @Autowired
  private ScriptEngine engine;

  @RiverMapping(value = "player/playerInfo.pf", method = "POST")
  public void playerInfo(Map<String, String[]> query, OutputStream out) throws IOException
  {
    Player p = ps.getPlayer(Long.parseLong(query.get("ucenterId")[0]), Integer.parseInt(query.get("serverid")[0]));
    Result re = new Result();
    re.setSuccess(p != null);
    if (p != null)
    {
      re.setInfo(JSON.toJSONString(p));
    }
    out.write(JSON.toJSONString(re).getBytes("utf-8"));
  }
}
