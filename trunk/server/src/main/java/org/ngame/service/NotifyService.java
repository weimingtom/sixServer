/**
 * 通知
 */
package org.ngame.service;

import org.ngame.domain.em.EMsg;
import org.ngame.protocol.notify.MsgNotify;
import org.ngame.server.Context;
import org.springframework.stereotype.Component;

/**
 *
 * @author beykery
 */
@Component
public class NotifyService
{

  /**
   * 通知消息
   *
   * @param pid
   * @param type
   * @param message
   */
  public void msg(String pid, EMsg type, String message)
  {
    MsgNotify notify = new MsgNotify();
    notify.setSuccess(true);
    notify.setMsg(message);
    notify.setType(type);
    Context.last().send(notify, pid);
  }
  
  /**
   * 战斗推送
   * @param notify
   * @param pids
   */
  public void notify(Object notify,String... pids){
	  Context.last().send(notify, pids);
  }
}
