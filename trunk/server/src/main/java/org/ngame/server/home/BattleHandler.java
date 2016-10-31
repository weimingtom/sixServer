package org.ngame.server.home;

import java.util.List;
import java.util.logging.Logger;

import org.ngame.annotation.RequestMapping;
import org.ngame.protocol.battle.ActionRequest;
import org.ngame.protocol.battle.BaseActionRequest;
import org.ngame.protocol.battle.BaseLvRequest;
import org.ngame.protocol.battle.EjectionRequest;
import org.ngame.protocol.battle.MatchRequest;
import org.ngame.protocol.battle.RoundRequest;
import org.ngame.protocol.battle.RandomAgainRequest;
import org.ngame.server.Context;
import org.ngame.service.BattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *battle
 *
 */
@Component
public class BattleHandler
{

  private static final Logger LOG = Logger.getLogger(BattleHandler.class.getName());
  @Autowired
  private BattleService battleService;
  
  /**
   * match
   * @param c
   * @param request
   */
  @RequestMapping(value = "match")
  public void match(Context c, MatchRequest request)
  {
	  String pid=c.getSessionId();
	  String cardGroupId=request.getCardGroupId();
	  battleService.match(pid, cardGroupId);
  }
  
  /**
   * knockout a card
   * @param c context
   * @param request
   */
  @RequestMapping(value = "ejection",auth=false)
  public void ejection(Context c, EjectionRequest request)
  {
	  String uniqueId=request.getUniqueId();
	  String roomId=request.getRoomId();
	  int position=request.getPosition();
	  String targetId=request.getTargetId();
	  battleService.ejection(roomId, uniqueId, position,targetId);
  }
  /**
   * base lv
   * @param c
   * @param request
   */
  @RequestMapping(value = "baseLv")
  public void baseLv(Context c, BaseLvRequest request)
  {
	  String roomId=request.getRoomId();
	  battleService.baseLv(roomId);
  }
  
  /**
   * race action.refer rece skill
   * @param c
   * @param request
   */
  @RequestMapping(value = "baseAction")
  public void baseAction(Context c, BaseActionRequest request)
  {
	  String roomId=request.getRoomId();
	  String targetId=request.getTargetId();
	  battleService.race(roomId,targetId);
  }
  
  /**
   * this means is on table card action
   * @param c
   * @param request
   */
  @RequestMapping(value = "action")
  public void action(Context c, ActionRequest request)
  {
	  String roomId=request.getRoomId();
	  String uniqueId=request.getUniqueId();
	  String targetId=request.getTargetId();
	  battleService.action(roomId,uniqueId,targetId);
  }

  
  @RequestMapping(value = "roundOver",auth=false)
  public void roundOver(Context c, RoundRequest request)
  {
	  String roomId=request.getRoomId();
	  battleService.roundEnd(roomId);
  }
  
  @RequestMapping(value = "randomAgain")
  public void randomAgain(Context c, RandomAgainRequest request)
  {
	  String roomId=request.getRoomId();
	  List<Integer> indexs=request.getIndexs();
	  battleService.randomAgain(roomId, c.getSessionId(), indexs);
  }
  
 
  @RequestMapping(value = "surrender")
  public void surrender(Context c, RandomAgainRequest request)
  {
	  String roomId=request.getRoomId();
	  battleService.surrender(roomId);
  }
}
