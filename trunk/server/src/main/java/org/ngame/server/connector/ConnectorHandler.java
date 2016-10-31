/**
 * connector的用户处理逻辑
 */
package org.ngame.server.connector;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.UrlBase64;
import org.ngame.annotation.RequestMapping;
import org.ngame.app.Application;
import org.ngame.service.CardService;
import org.ngame.service.NotifyService;
import org.ngame.service.PlayerService;
import org.ngame.service.ThreadPoolService;
import org.ngame.domain.Player;
import org.ngame.protocol.OnlineRequest;
import org.ngame.protocol.OnlineResponse;
import org.ngame.protocol.auth.AuthRequest;
import org.ngame.protocol.auth.AuthResponse;
import org.ngame.protocol.notify.CardGroupNotify;
import org.ngame.protocol.ping.Pong;
import org.ngame.script.ScriptEngine;
import org.ngame.server.Context;
import org.ngame.server.Router;
import org.ngame.socket.NClient;
import org.ngame.util.DESMD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Beykery
 */
@Component
public class ConnectorHandler
{

  private static final Logger LOG = Logger.getLogger(ConnectorHandler.class);
  @Autowired
  private PlayerService playerService;
  @Autowired
  private ScriptEngine engine;
  @Autowired
  private NotifyService notifyService;
  @Autowired
  private CardService cardService;
  @Autowired
  private ThreadPoolService threadPoolService;

  /**
   * 验证
   *
   * @param c
   * @param au
   * @return
   */
  @RequestMapping(value = "auth", auth = false)
  public AuthResponse auth(Context c, AuthRequest au)
  {
    AuthResponse ar = new AuthResponse();
    if (c.getClient().getSessionId() != null)
    {
      ar.setInfo("不可重复auth");
    } else
    {
      int serverId = au.getServerid();
      List<Integer> service = (List<Integer>) engine.getProperty(Application.serverFile, "service");
      if (service.contains(serverId))//可为此连接服务
      {
        JSONObject json;
//TODO ..这个是因为没接用户中心导致没有loginid 临时改掉的，以后会改回来
//        String loginId = au.getLoginid();
//        long ucenterId = -1;
//        boolean testing = false;
//        String userCode = "";
        long ucenterId = au.getUcenterId();
        boolean testing = au.isTesting();
        String userCode = au.getUserCode();
        try
        {
//          json = JSON.parseObject(new String(DESMD5.decrypt(UrlBase64.decode(loginId.getBytes("UTF-8")), Application.DES_KEY)));
//          ucenterId = json.getLong("id");
//          testing = json.getBooleanValue("test");
//          userCode = json.getString("usercode");
        } catch (Exception e)
        {
        	e.printStackTrace();
        }
        if (ucenterId > 0)//从用户中心来的
        {
          if ((Application.isReady() && testing) || (!Application.isReady()))
          {
            c.getClient().setSession(Router.SERVER_ID_KEY, serverId);//记录下serverId
            c.getClient().setSession(Router.ROUTER_KEY, ucenterId);
            ar.setSuccess(true);
            Player p = this.playerService.getPlayer(ucenterId, serverId);
            if(p==null){
            	 p = playerService.createPlayer(serverId,ucenterId, userCode, testing, userCode);
            }
              NClient cli = c.getClient();
              cli.setSessionId(p.getId());
              ((ConnectorServer) c.getServer()).login(cli);
              ar.setPid(p.getId());
//            //上报最近登录,异步 TODO
//            JSONObject obj = new JSONObject();
//            obj.put("uid", ucenterId);
//            obj.put("gid", Application.gameId);
//            obj.put("sid", serverId);
//            LoginRunnable run = new LoginRunnable(ucenterId, Application.gameId, serverId, Application.recentUrl);
//            threadPoolService.put(run);
              
              //推送个人牌组Notify
              CardGroupNotify cgn=new CardGroupNotify();
              cgn.setPid(p.getId());
              cgn.setCardGroup(cardService.allCardGroupInfo(p.getId()));
              notifyService.notify(cgn, p.getId());
          } else
          {
            ar.setInfo("准备中");
          }
        }
      } else
      {
        ar.setInfo("无法提供特殊服务");
      }
    }
    if (!ar.isSuccess() && ar.getInfo() == null)
    {
      ar.setInfo("未知异常");
    }
    return ar;
  }

  /**
   * ping协议，保活
   *
   * @return
   */
  @RequestMapping(value = "ping", auth = false)
  public Pong ping()
  {
    Pong pong = new Pong();
    pong.setSuccess(true);
    pong.setTime(System.currentTimeMillis());
    return pong;
  }

  /**
   * 测试
   * @return
   */
  @RequestMapping(value = "online", auth = false)
  public OnlineResponse getOnline(Context c,OnlineRequest onlineRequest)
  {
    OnlineResponse op=new OnlineResponse();
	ConnectorServer cs= (ConnectorServer)c.getBaseServer();
	Set<String> pids=cs.connections.keySet();
	op.setPids(new ArrayList<>(pids));
    return op;
  }

}