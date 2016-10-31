package org.ngame.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.ngame.data.DataBuilder;
import org.ngame.data.init.D_Card;
import org.ngame.domain.Card;
import org.ngame.domain.CardGroup;
import org.ngame.domain.Player;
import org.ngame.protocol.domain.CardGroupInfo;
import org.ngame.protocol.domain.CardInfo;
import org.ngame.protocol.notify.CardGroupNotify;
import org.ngame.util.RedisUtil;
import org.rojo.repository.Rojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CardService
{
  @Autowired
  private PlayerService playerService;
  @Autowired
  private NotifyService notifyService;
  
  public void initCardGroup(Player p,String name,String baseInitId,List<String> cardIds){
	  Rojo rojo=RedisUtil.rojo();
	  CardGroup cg=new CardGroup();
	  cg.setPid(p.getId());
	  cg.setName(name);
	  cg.setBaseInitId(baseInitId);
	  cg.setId(RedisUtil.id(rojo, CardGroup.class,p.getServerid()));
	  cg.setCardIds(cardIds);
	  rojo.saveAndFlush(cg);
  }
  
  /**
   * 添加卡牌
   * @param pid
   * @param initCardId
   */
  public void addCard(String pid,String initCardId){
	  Rojo rojo = RedisUtil.rojo();
	  Player p=playerService.getPlayerInfo(pid);
	  Card c=new Card();
	  D_Card dc=  DataBuilder.build(new D_Card(initCardId));
	  if(dc!=null){
		  c.setId(RedisUtil.id(rojo, Card.class, p.getServerid()));
		  c.setInitId(initCardId);
		  c.setPid(pid);
		  c.setQulity(1);
		  rojo.saveAndFlush(c);
	  }
  }
  
  public Card getCard(String cardId){
	  Rojo rojo = RedisUtil.rojo();
	  return rojo.get(Card.class, cardId);
  }
  
  public Set<CardGroup> allCardGroup(String pid){
	  Rojo rojo=RedisUtil.rojo();
	 return rojo.index(CardGroup.class, "pid", pid);
  }
  
  public List<CardGroupInfo> allCardGroupInfo(String pid){
	  List<CardGroupInfo> list=new ArrayList<>();
	  Set<CardGroup> set=allCardGroup(pid);
	  for(CardGroup cg : set){
		  list.add(cg.change());
	  }
	 return list;
  }
  public void addCardGroup(String pid,String id,String name,String baseInitId,List<String> cardIds){
	  Player p=playerService.getPlayerInfo(pid);
	  Rojo rojo = RedisUtil.rojo();
//	  if(isGet(pid, cardIds)){
		  CardGroup cg=rojo.get(CardGroup.class, id);
		  if(cg!=null){
			  cg.setName(name);
			  cg.setCardIds(cardIds);
			  cg.setBaseInitId(baseInitId);
			  rojo.updateAndFlush(cg);
		  }else{
			  cg=new CardGroup();
			  cg.setId(RedisUtil.id(rojo, CardGroup.class, p.getServerid()));
			  cg.setName(name);
			  cg.setPid(pid);
			  cg.setBaseInitId(baseInitId);
			  cg.setCardIds(cardIds);
			  rojo.saveAndFlush(cg);
		  }
		   //推送个人牌组Notify
          CardGroupNotify cgn=new CardGroupNotify();
          cgn.setPid(p.getId());
          cgn.setCardGroup(allCardGroupInfo(p.getId()));
          notifyService.notify(cgn, p.getId());
//	  }
  }
  
  /**
   * 已经拥有
   * @param pid
   * @param cardIds
   * @return
   */
  public boolean isGet(String pid,List<String> cardIds){
	  Rojo rojo = RedisUtil.rojo();
	  for(String id : cardIds){
		  if(rojo.get(Card.class, id)==null) return false;
	  }
	  return true;
  }
  
  /**
   * 随机从cardGroup中获取n张卡牌
   * @param index
   * @return
   */
  public List<CardInfo> randomCardN(String cardGroupId){
	  Rojo rojo = RedisUtil.rojo();
	  CardGroup cg=rojo.get(CardGroup.class,cardGroupId);
	  List<CardInfo> cardIds=new ArrayList<>();
	  for(String id : cg.getCardIds()){
//		  Card c=getCard(id);
//		  D_Card dc=DataBuilder.build(new D_Card(c.getInitId()));
		  D_Card dc=DataBuilder.build(new D_Card(id));
		  cardIds.add( dc.change());//clone data表数据
	  }
	  Collections.shuffle(cardIds);
	  return cardIds;
  }
  
  /**
   * 获得指定的卡组
   * @param cardGroupId
   * @return
   */
  public CardGroup getCardGroup(String cardGroupId){
	  Rojo rojo = RedisUtil.rojo();
	  return rojo.get(CardGroup.class,cardGroupId);
  }
  
  public String getBaseInitId(String cardGroupId){
	  Rojo rojo = RedisUtil.rojo();
	  CardGroup cg=rojo.get(CardGroup.class,cardGroupId);
	  return cg.getBaseInitId();
  }
}
