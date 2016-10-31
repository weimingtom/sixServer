/**
 * 支付服务
 */
package org.ocean.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.Date;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.ocean.controller.man.Result;
import org.ocean.dao.BaseDao;
import org.ocean.domain.Game;
import org.ocean.domain.GameServer;
import org.ocean.domain.Orderform;
import org.ocean.util.ExceptionUtil;
import org.ocean.util.HttpUtil;
import org.ocean.Spring;
import org.ocean.domain.Channel;
import org.ocean.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Decoder;

/**
 *
 * @author beykery
 */
@Component
public class PayService
{

  private static final Logger LOG = Logger.getLogger(PayService.class);
  public static final String verifyURL = "https://buy.itunes.apple.com/verifyReceipt";
  public static final String sandBoxVerifyUrl = "https://sandbox.itunes.apple.com/verifyReceipt";
  @Autowired
  private BaseDao dao;

  /**
   * 支付
   *
   * @param ucenterId
   * @param amount
   * @param gameId
   * @param serverId
   * @param chargeType
   * @param anyOrder any的订单号
   * @param orderId 订单id
   * @param anyChannel
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public void pay(long ucenterId, double amount, int gameId, int serverId, int chargeType, String anyOrder, String orderId, String anyChannel)
  {
    Orderform of = this.getOrderform(orderId);
    User u = Spring.bean(UserService.class).getUser(ucenterId);
    if (of == null)
    {
      of = new Orderform();
      of.setAnyChannel(anyChannel);
      of.setPrice(amount);
      of.setAnyOid(anyOrder);
      of.setCtime(new Date());
      of.setStatus(Orderform.STATUS_OVER);
      of.setGameId(gameId);
      of.setServerId(serverId);
      of.setOid(orderId);
      of.setFtime(new Date());
      of.setChargeType(chargeType);
      of.setUsercode(u == null ? null : u.getUsercode());
      dao.saveOrUpdate(of);
    } else
    {
      of.setStatus(Orderform.STATUS_OVER);
      of.setAnyChannel(anyChannel);
      of.setAnyOid(anyOrder);
      of.setFtime(new Date());
      dao.update(of);
    }
    boolean r = notifyPayOver(of);
    if (!r)
    {
      LOG.error("订单 " + of.getId() + " 通知失败");
    }
  }

  /**
   * orderform
   *
   * @param orderId
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public Orderform getOrderform(String orderId)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("select o from Orderform o where o.oid=:oid");
    q.setString("oid", orderId);
    q.setCacheable(true);
    return (Orderform) q.uniqueResult();
  }

  /**
   * 通知服务器订单已经支付
   *
   * @param of
   * @return
   */
  public boolean notifyPayOver(Orderform of)
  {
    if (of.getStatus() == Orderform.STATUS_OVER)
    {
      int gid = of.getGameId();
      int sid = of.getServerId();
      String usercode = of.getUsercode();
      Game g = Spring.bean(GameService.class).getGame(gid);
      GameServer gs = Spring.bean(ServerService.class).getServer(g.getId(), sid);
      String addr = gs.getAddress();
      int index = addr.indexOf(":");
      String ip = addr.substring(0, index);
      String url = "http://" + ip + ":" + gs.getRiverPort() + "/river/pay/pay.pf";//玩家基本数据
      StringBuilder sb = new StringBuilder();
      sb.append("serverid=").append(sid).append("&");
      sb.append("usercode=").append(usercode).append("&");
      sb.append("oid=").append(of.getOid()).append("&");
      sb.append("type=").append(of.getChargeType()).append("&");
      sb.append("price=").append(of.getPrice());
      try
      {
        byte[] r = HttpUtil.post(url, sb.toString().getBytes("UTF-8"), g.getDesKey());
        Result re = JSON.parseObject(new String(r, "utf-8"), Result.class);
        return re.isSuccess();
      } catch (Exception e)
      {
        LOG.error(of.getOid() + " 充值通知游戏服务器失败:" + ExceptionUtil.stackTrace(e));
      }
    }
    return false;
  }

  /**
   * 是否苹果充值有效
   *
   * @param orderId
   * @param receiptStr
   * @param cname
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public boolean ipayValide(String orderId, String receiptStr, String cname)
  {
    String url = verifyURL;
    Orderform orderform = this.getOrderform(orderId);
    Game game = Spring.bean(GameService.class).getGame(orderform.getGameId());
    if (orderform == null || game == null)
    {
      return false;
    }
    Channel c = cname == null ? null : Spring.bean(ChannelService.class).getChannel(game.getId(), cname);
    JSONObject result = this.iverify(url, receiptStr);
    if (result != null && result.getInteger("status") == 21007)//沙盒环境
    {
      url = sandBoxVerifyUrl;
      result = iverify(url, receiptStr);
    }
//    LOG.error("send url:" + url + "\tresponse data:" + result);
    if (result != null)
    {
      int status = result.getInteger("status");
      JSONObject receiptObj = (JSONObject) result.get("receipt");
      if (receiptObj != null)
      {
        String bid = receiptObj.getString("bid");
        if (c != null)
        {
          if (!c.getBid().equals(bid))
          {
            LOG.error("bid is Invalid!:\t" + bid);
            return false;
          }
        } else if (!game.getBid().equals(bid))
        {
          LOG.error("bid is Invalid!:\t" + bid);
          return false;
        }
      } else
      {
        LOG.error("receipt is null!");
        return false;
      }
      return status == 0;
    }
    return false;
  }

  /**
   * 验证
   *
   * @param url
   * @param receipt
   * @return
   */
  private JSONObject iverify(String url, String receipt)
  {
    try
    {
      String data = "{\"receipt-data\": \"" + receipt + "\"}";
      byte[] content = HttpUtil.post(url, data.getBytes(), null);
      String resultStr = new String(content);
      JSONObject result = JSONObject.parseObject(resultStr);
      return result;
    } catch (Exception e)
    {
      LOG.error("苹果验证异常：" + receipt);
    }
    return null;
  }
}
