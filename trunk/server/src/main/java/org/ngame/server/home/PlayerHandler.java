/**
 * player的操作
 */
package org.ngame.server.home;

import java.util.List;
import java.util.logging.Logger;

import org.ngame.annotation.RequestMapping;
import org.ngame.protocol.battle.BaseActionRequest;
import org.ngame.protocol.player.CardGroupRequest;
import org.ngame.server.Context;
import org.ngame.service.CardService;
import org.ngame.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * player操作
 *
 * @author beykery
 */
@Component
public class PlayerHandler
{

  private static final Logger LOG = Logger.getLogger(PlayerHandler.class.getName());
  @Autowired
  private PlayerService playerService;
  @Autowired
  private CardService cardService;
  
  /**
   * 添加套牌
   * @param c
   * @param request
   */
  @RequestMapping(value = "addCardGroup")
  public void addCardGroup(Context c, CardGroupRequest request)
  {
	  String id=request.getId();
	  String name=request.getName();
	  String baseInitId=request.getBaseInitId();
	  List<String> cardIds=request.getCardIds();
	  cardService.addCardGroup(c.getSessionId(), id,name, baseInitId, cardIds);
  }
  
}
