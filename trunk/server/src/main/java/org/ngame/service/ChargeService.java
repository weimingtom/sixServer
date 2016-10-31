/**
 * 充值
 */
package org.ngame.service;

import org.ngame.domain.Player;
import org.ngame.script.ScriptEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author beykery
 */
@Component
public class ChargeService
{

  @Autowired
  private ScriptEngine engine;

  /**
   * 充值
   *
   * @param p
   * @param type
   * @param price
   */
  public void charge(Player p, int type, double price)
  {
    //todo:
  }

}
