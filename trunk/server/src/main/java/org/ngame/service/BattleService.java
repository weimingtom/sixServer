package org.ngame.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.ngame.data.DataBuilder;
import org.ngame.data.init.D_Base;
import org.ngame.data.init.D_Buff;
import org.ngame.data.init.D_Card;
import org.ngame.data.init.D_Skill;
import org.ngame.domain.Player;
import org.ngame.domain.em.EBuff;
import org.ngame.domain.em.ESkill;
import org.ngame.domain.em.EStatus;
import org.ngame.domain.em.ETarget;
import org.ngame.domain.em.ETrigger;
import org.ngame.protocol.battle.ActionResponse;
import org.ngame.protocol.battle.BaseActionResponse;
import org.ngame.protocol.battle.BaseLvResponse;
import org.ngame.protocol.battle.EjectionResponse;
import org.ngame.protocol.battle.MatchResponse;
import org.ngame.protocol.battle.RoundResponse;
import org.ngame.protocol.battle.RandomAgainResponse;
import org.ngame.protocol.domain.BaseInfo;
import org.ngame.protocol.domain.BattleInfo;
import org.ngame.protocol.domain.CardInfo;
import org.ngame.protocol.domain.RoundInfo;
import org.ngame.protocol.domain.RoomInfo;
import org.ngame.protocol.domain.SkillInfo;
import org.ngame.protocol.notify.BattleOverNotify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class BattleService
{
  private static final Logger LOG = Logger.getLogger(BattleService.class.getName());
  private static Map<String,RoomInfo> rooms=new ConcurrentHashMap<>();
  //record user request time userid:time
//  private static Map<String,Long> online=new ConcurrentHashMap<>();
  private static Map<String,String> online=new ConcurrentHashMap<>();
  /**max hand card count**/
  private final static int maxHandCard=10; 
  /**max table card count**/
  private final static int maxTableCard=12;
  @Autowired
  private PlayerService playerService;
  @Autowired
  private CardService cardService;
  @Autowired
  private NotifyService notifyService;
  private BattleRemoveThread brt;
  //TODO ..test
  @Autowired
  private ThreadPoolService threadPoolService;

  private Lock lock=new ReentrantLock();
  /**
   * battle
   * @param pid
   * @param index select the card group
   */
  public void match(String pid,String p1cardGroupId){
	  //TODO ..this logic is updating....
	  if(brt==null) brt=new BattleRemoveThread();
	  if (!brt.running())   brt.start();
	  brt.add(pid,System.currentTimeMillis());
//	  brt.add(pid, now);
//	  threadPoolService.put(brt);
	  Player p=playerService.getPlayerInfo(pid);
	  if(p!=null && online.size()>0){
		  String bpid=online.keySet().iterator().next();//battle specify //battle succeed
		  if(!pid.equals(bpid)){//battle obj can't youself
			  initRoom(pid,bpid , p1cardGroupId,online.get(bpid));
			  online.remove(bpid);
		  }
	  }else {
		  online.put(pid,p1cardGroupId);
	  }
	  Long now=System.currentTimeMillis();
  }
  
  public void remove(String pid){
	  online.remove(pid);
  }
  public void initRoom(String p1,String p2,String p1cardGroupId,String p2cardGroupId){
	  RoomInfo room=new RoomInfo();
	  room.setId(UUID.randomUUID().toString().replaceAll("-", ""));
	  room.setBackground("100010");//random a background icon
	  room.setAttInfo(initBattle(p1, p1cardGroupId));
	  room.setDefInfo(initBattle(p2, p2cardGroupId));
	  //添加索引,客户端将根据这个索引生成卡牌
	  room.addIndex(cardService.getCardGroup(p1cardGroupId).getCardIds().size()*2);
	  rooms.put(room.getId(), room);
	  //Notify
	  MatchResponse mr=new MatchResponse();
	  mr.setRoom(room);
	  //random spacify rule  TODO ......这个地方会修改通知
	  roundStart(room.getId(),null);
	  notifyService.notify(mr,p1,p2);
  }
  
  public BattleInfo initBattle(String pid,String cardGroupId){
	  BattleInfo bi=new BattleInfo();
	  Player p=playerService.getPlayerInfo(pid);
	  bi.setPid(pid);
	  bi.setName(p.getNickname());
	  bi.setLv(p.getLevel());
	  bi.setIcon(p.getIcon());
	  D_Base db=DataBuilder.build(new D_Base(cardService.getBaseInitId(cardGroupId)));
	  bi.setBaseInfo(db.change());
	  List<CardInfo> list=cardService.randomCardN(cardGroupId);
	  bi.setHandCard(new LinkedList<>(list.subList(0, 4)));
	  bi.setHeapCard(new LinkedList<>(list.subList(4, list.size())));
	  return bi;
  }
  
  /**
   *  出牌
   * */
  public void ejection(String roomId,String uniqueId,int position,String targetId){
	  RoomInfo room=rooms.get(roomId);
	  BattleInfo att=room.getAttInfo();
	  System.out.println("1");
	  CardInfo dc=getHandCardInfo(roomId, uniqueId);
	  //判断有没超过牌数量
	  if(dc.getType()==1 && att.getTableCard()!=null && att.getTableCard().size()>=maxTableCard) return;
	  //判断阶数是否满足
//	  if(att.getBaseInfo().getScience()<dc.getScience()) return;
	  //消耗资源
//	  if(att.getResource()-dc.getCost()<0)  return;
//	  att.subResource(dc.getCost());
	//随从牌
	  if(dc.getType()==1){
		  att.getTableCard().add(dc);//出牌
	  } else{
		  att.getOutCard().add(dc);//法术牌
	  } 
	  List<SkillInfo> skillList=new ArrayList<>();
	 if(dc.getSkill()!=null){
		 for(String skillId : dc.getSkill()){
			  List<SkillInfo> list=computeSkill(roomId, ETrigger.ENTER, position,dc.getUniqueId(), skillId,targetId);
			  if(list!=null && list.size()>0){
				 skillList.addAll(list);
			  }
		  }
	 }
	  //Notify
//	  compareNotify(before);
	  EjectionResponse er=new EjectionResponse();
	  er.setPid(att.getPid());
	  er.setUniqueId(uniqueId);
	  er.setCardId(dc.getId());
	  er.setPosition(position);
	  er.setSkill(skillList);
	  notifyService.notify(er, room.getAttInfo().getPid(),room.getDefInfo().getPid());
	  
	  
	  att.getHandCard().remove(dc);//hand card remove
  }
  
  public void action(String roomId,String uniqueId,String targetId){
	  RoomInfo room=rooms.get(roomId);
	  BattleInfo att=room.getAttInfo();
	  List<SkillInfo> skillList=new LinkedList<>();
	  List<SkillInfo> vaild=computerAttack(roomId, uniqueId, -1, targetId);
	  System.out.println("");
	 if(vaild!=null && vaild.size()>0){
		 skillList.addAll(vaild);
	 }
	 CardInfo card=getTableCardInfo(roomId, uniqueId);
	 if(card!=null){//我攻击别人 && 没有把自己打死了
	 	  for(String skillId : card.getSkill()){
			  List<SkillInfo> list=computeSkill(roomId, ETrigger.ATTACK, -1,card.getUniqueId(),skillId,targetId);
			 if(list!=null && list.size()>0){
				 skillList.addAll(list);
			 }
		  }
	 }

 	  
	  //Notify
//	  compareNotify(before);
	  ActionResponse ar=new ActionResponse();
	  ar.setPid(att.getPid());
	  ar.setUniqueId(uniqueId);
	  ar.setTargetId(targetId);
	  ar.setSkill(skillList);//valid skill and target
	  notifyService.notify(ar, room.getAttInfo().getPid(),room.getDefInfo().getPid());
  }
  

  /**
   * base attack
   */
  public void race(String roomId,String targetId){
	  RoomInfo room=rooms.get(roomId);
	  BaseInfo db=room.getAttInfo().getBaseInfo();
	  //cost resouce
	  if(room.getAttInfo().getResource()-db.getCost()<0) return;
	  room.getAttInfo().subResource(db.getCost());
	  List<SkillInfo> list=computeSkill(roomId, ETrigger.RACEACTION,-1, null,db.getSkill(),targetId);//如果launch,position为0代表基地 似乎可以跟attack公用
	  if(list==null){
			list=new ArrayList<>();
	  }
	  //Notify
//	  compareNotify(before);
	  BaseActionResponse bar=new BaseActionResponse();
	  bar.setPid(room.getAttInfo().getPid());
	  bar.setSkill(list);
	  notifyService.notify(bar, room.getAttInfo().getPid(),room.getDefInfo().getPid());

  }
  
  
  public void surrender(String roomId){
	  RoomInfo room=rooms.get(roomId);
	  BattleInfo att=room.getAttInfo();
	  BattleInfo def=room.getDefInfo();
	  room.setAttInfo(def);
	  room.setDefInfo(att);//att and def change
	  battleOver(roomId);
  }

  public void battleOver(String roomId){
	  RoomInfo room=rooms.get(roomId);
	  //reward 
	
	  //Notify
	  BattleOverNotify attBon=new BattleOverNotify();
	  BattleOverNotify defBon=new BattleOverNotify();
	  attBon.setRoomId(roomId);
	  defBon.setRoomId(roomId);
	  attBon.setFlag(true);
	  defBon.setFlag(false);
	  notifyService.notify(attBon, room.getAttInfo().getPid());
	  notifyService.notify(defBon,room.getDefInfo().getPid());
	  rooms.remove(roomId);//move romm
  }
  /**
   * lv base
   */
  public void baseLv(String roomId){
	  RoomInfo room=rooms.get(roomId);
	  int cost=BattleInfo.baseLvBase*room.getAttInfo().getBaseInfo().getScience();//lv base need cost resource
	  if(room.getAttInfo().getResource()-cost<0) return;
	  room.getAttInfo().subResource(cost);
	  room.getAttInfo().addBaseLv(1);
	  //Notify
//	  compareNotify(before);
	  BaseLvResponse lv=new BaseLvResponse();
	  lv.setPid(room.getAttInfo().getPid());
	  lv.setResource(room.getAttInfo().getResource());
	  lv.setBaseLv(room.getAttInfo().getBaseInfo().getScience());
	  notifyService.notify(lv, room.getAttInfo().getPid(),room.getDefInfo().getPid());
  }
  /**
   *  anew roll specify hand card
   * @param roomId
   * @param pid
   * @param indexs card
   */
  public void randomAgain(String roomId,String pid,List<Integer> indexs){
	  RoomInfo room=rooms.get(roomId);
	  BattleInfo bi=room.getBattleInfo(pid);
	  for(Integer index : indexs){
		  CardInfo dc=bi.getHandCard().get(index);
		  bi.getHandCard().remove(dc);
		  bi.getHeapCard().add(dc);
		  bi.getHandCard().add(bi.getHeapCard().get(index));
	  }
	  Collections.shuffle(bi.getHeapCard());
	  //Notify
//	  compareNotify(before);
	  RandomAgainResponse rar=new RandomAgainResponse();
	  rar.setRoomId(roomId);
	  rar.setPid(pid);
	  rar.setHand(bi.getHandCard());
	  notifyService.notify(rar, room.getAttInfo().getPid(),room.getDefInfo().getPid());
  }
  /**
   * 
   * @param roomId
   * @param isstart  if one round:true else:false
   */
  public void roundStart(String roomId,RoundInfo nri){
	  RoomInfo room=rooms.get(roomId);
	  //then round over,enemy roll two card
	  List<CardInfo> list=new LinkedList<>(room.getAttInfo().getHeapCard().subList(0, 2));
	  for(CardInfo ci : list){
		  if(room.getAttInfo().getHandCard().size()<maxHandCard){
			  room.getAttInfo().getHandCard().add(ci);
			  if(nri!=null) nri.getAdd().add(ci);
		  }
	  }
	  room.getAttInfo().getHeapCard().removeAll(list);
	  room.getAttInfo().addRound(1);//add round
	  room.getAttInfo().addResource();//every round add resource
	  
	  
	  //Notify
	  //compareNotify(before);
	  //TODO ..this notify not mind
	  if(nri!=null){
		  nri.setAttId(room.getAttInfo().getPid());
		  nri.setResouce(room.getAttInfo().getResource());
		  nri.setRound(room.getAttInfo().getRound());
		  RoundResponse nrr=new RoundResponse();
		  nrr.setRoomId(roomId);
		  nrr.setNri(nri);
		  notifyService.notify(nrr, room.getAttInfo().getPid(),room.getDefInfo().getPid());
	  }
  }
  
  public void roundEnd(String roomId){
	  RoomInfo room=rooms.get(roomId);
	  BattleInfo att=room.getAttInfo();
	  BattleInfo def=room.getDefInfo();
	  RoundInfo nri=new RoundInfo();
	  for(CardInfo ci : att.getTableCard()){
		  ci.setAction(0);//sub action
		  if(ci.getStatus()==EStatus.FROZEN.getValue() || ci.getStatus()==EStatus.IMPRISONMENT.getValue())  ci.setStatus(0);
			 for(String buffId : ci.getBuff()){
				  D_Buff db=DataBuilder.build(new D_Buff(buffId));
				  if(db.getEffectRound()!=-1){//at round over , cancel one round buff
					  ci.getBuff().remove(buffId);
					  nri.getRemove().add(ci.getId());//this defence , because this's here none att change def
				  }
			 }
	  }
	  for(CardInfo ci : def.getTableCard()){//add action
		  //round start compute skill
		  for(String skillId : ci.getSkill()){
			  computeSkill(roomId, ETrigger.ROUNDBEGIN, def.getTableCard().indexOf(ci), ci.getUniqueId(), skillId, null);
		  }
		  ci.addAction(1);
		  nri.getAction().add(ci.getId());
	  }
	  
	  room.setAttInfo(def);
	  room.setDefInfo(att);//att and def change
	  roundStart(roomId,nri);
  }
  
  public List<SkillInfo> computerAttack(String roomId,String uniqueId,int position,String targetId){
	  List<SkillInfo> vaild=new LinkedList<>();
	  RoomInfo room=rooms.get(roomId);
	  CardInfo attCard=getTableCardInfo(roomId, uniqueId);
	  CardInfo defCard=getTableCardInfo(roomId, targetId);
	  // if attack base will bear all follower attacked
	  int hurt=0; boolean defCardFlag=false; boolean attCardFlag=false;
	  if(defCard==null){//-0 express attack base
		  for(CardInfo ci :room.getDefInfo().getTableCard()){
			  hurt+=ci.getAtt();
		  }
		  boolean flag=room.getDefInfo().getBaseInfo().subBlood(hurt);
		  if(flag){//isOver
			  battleOver(roomId);
		  }
	  }else{
		  defCardFlag=defCard.subBlood(attCard.getAtt());
	  }
	  attCardFlag=attCard.subBlood(hurt>0 ? hurt : defCard.getAtt());

	  if(attCardFlag){
		  for(String skillId : attCard.getSkill()){
			  List<SkillInfo> list=computeSkill(roomId, ETrigger.DEATH,-1, attCard.getUniqueId(),skillId,null);
			  if(list!=null && list.size()>0){
				  vaild.addAll(list);
			  }
		  }
		  room.getAttInfo().getOutCard().add(attCard);
		  room.getAttInfo().getTableCard().remove(attCard);
	  }
	  if(defCardFlag){
		  for(String skillId : defCard.getSkill()){
			  List<SkillInfo> list=computeSkill(roomId, ETrigger.DEATH,-1,defCard.getUniqueId(),skillId,null);
			  if(list!=null && list.size()>0){
				  vaild.addAll(list);
			  }
		  }
		  room.getDefInfo().getOutCard().add(defCard);
		  room.getDefInfo().getTableCard().remove(defCard);
	  }
	  return vaild;
  }
  
  /**
   * get att and def
   * @param roomId
   * @param uniqueId
   * @return
   */
  public BattleInfo[] attAndDef(String roomId,String uniqueId){
	  BattleInfo[] attAndDef=new BattleInfo[2];
	  RoomInfo room=rooms.get(roomId);
	  attAndDef[0]=room.getAttInfo();
	  attAndDef[1]=room.getDefInfo();
	  for(CardInfo info :room.getDefInfo().getTableCard()){
		  if(info.getUniqueId().equals(uniqueId)){
			  attAndDef[0]=room.getAttInfo();
			  attAndDef[1]=room.getDefInfo();
			  break;
		  }
	  }
	  return attAndDef;
  }
  
  public CardInfo getHandCardInfo(String roomId,String uniqueId){
	  RoomInfo room=rooms.get(roomId);
	  for(CardInfo info : room.getAttInfo().getHandCard()){
		  if(info.getUniqueId().equals(uniqueId)){
			  return info;
		  }
	  }
	  for(CardInfo info : room.getDefInfo().getHandCard()){
		  if(info.getUniqueId().equals(uniqueId)){
			  return info;
		  }
	  }
	  return null;
  }
  public CardInfo getTableCardInfo(String roomId,String uniqueId){
	  RoomInfo room=rooms.get(roomId);
	  for(CardInfo info : room.getAttInfo().getTableCard()){
		  if(info.getUniqueId().equals(uniqueId)){
			  return info;
		  }
	  }
	  for(CardInfo info : room.getDefInfo().getTableCard()){
		  if(info.getUniqueId().equals(uniqueId)){
			  return info;
		  }
	  }
	  return null;
  }
  
  public int attOrDef(String roomId,String uniqueId){
	  RoomInfo room=rooms.get(roomId);
	  for(CardInfo info : room.getDefInfo().getTableCard()){
		  if(info.getUniqueId().equals(uniqueId)){
			  return 1;
		  }
	  }
	  return 0;
  }
  /**
   * 
   * @param roomId
   * @param trigger
   * @param launch
   * @param position
   * @param skillId
   * @param target
   * @return List<SkillInfo> vaild skill and it's target
   */
  public List<SkillInfo> computeSkill(String roomId,ETrigger trigger,int position,String uniqueId,String skillId,String targetId){
	  /** vaild skill and it's target**/
	  List<SkillInfo> list=new LinkedList<>();
	  RoomInfo room=rooms.get(roomId);
	  BattleInfo att=attAndDef(roomId, uniqueId)[0];
	  CardInfo attCardInfo=att.getTableCard().get(position);
	  D_Skill ds=DataBuilder.build(new D_Skill(skillId));
	  List<CardInfo> tList=computeTarget(roomId,position,uniqueId,skillId,targetId);
	  boolean validSkillId=true;
	  if(ETrigger.parse(ds.getTrigger())!=trigger){//is right trigger
		  //compute skill
		  switch (ESkill.parse(ds.getSkillType())) {
			case SUMMON:
				for(String summonId : ds.getSummon()){
					if(att.getTableCard().size()<maxTableCard){
						att.getTableCard().add(DataBuilder.build(new D_Card(summonId)).change());
						room.addIndex(1);
					}
				}
				break;
			case CURE:
				for(CardInfo dc : tList){
					if(ds.getValue()==0)  dc.setBlood(DataBuilder.build(new D_Card(dc.getId()).getBlood()));
					else dc.addBlood(ds.getValue());
				}
			case MULTIPLE:
				for(CardInfo dc : tList){
					dc.addAction(1);
				}
				break;
			case HURT:
				for(CardInfo dc : tList){
					boolean die=false;
					if(ds.getValue()==0) die=dc.subBlood(attCardInfo.getAtt());
					else die=dc.subBlood(ds.getValue());
					if(die) {
						for(String sid : dc.getSkill()){
							List<SkillInfo> l=computeSkill(roomId, ETrigger.DEATH, -1, dc.getUniqueId(),sid,null);
							if(l!=null && l.size()>0){
								list.addAll(l);
							}
						}
					}
				}
				break;
			case CHANGE:
				for(CardInfo dc : tList){
					for(String summonId : ds.getSummon()){
						dc.change(DataBuilder.build(new D_Card(summonId)));
					}
				}
				break;
			case SUICIDE:
				for(CardInfo dc : tList){
					att.getTableCard().remove(dc);
					att.getOutCard().add(dc);
				}
				break;
			case FROZEN:
				for(CardInfo dc : tList){
					dc.setStatus(EStatus.FROZEN.getValue());
				}
				break;
			case IMPRISONMENT:
				for(CardInfo dc : tList){
					dc.setStatus(EStatus.IMPRISONMENT.getValue());
				}
				break;
			case STEALTH:
				for(CardInfo dc : tList){
					dc.setStatus(EStatus.STEALTH.getValue());
				}
				break;
			case PROTECT:
				for(CardInfo dc : tList){
					dc.setStatus(EStatus.PROTECT.getValue());
				}
				break;
			case UNSTEALTH:
			case UNPROTECT:
				for(CardInfo dc : tList){
					dc.setStatus(0);
				}
				break;
			default:
				validSkillId=false;//if skill not valid ,return false; default true
				break;
		  }
	  }
	  //buff
	  computeBuff(roomId, skillId, tList);
	  
	  if(validSkillId){
		  SkillInfo info=new SkillInfo();
		  info.setUniqueId(uniqueId);
		  info.setId(skillId);
		  info.setTarget(computeTargetIds(roomId, skillId, tList));
		  list.add(0,info);
	  }
	  return list;
  }	
  
  public void computeBuff(String roomId,String skillId,List<CardInfo> target){
	  D_Skill ds=DataBuilder.build(new D_Skill(skillId));
	  for(String buffId : ds.getBuff()){
		  D_Buff db=DataBuilder.build(new D_Buff(buffId));
		  for(CardInfo dc : target){
			  switch (EBuff.parse(db.getEffect())) {
				  case ADDATT:
						dc.addAtt(db.getValue());
				  break;
				  case ADDBLOOD:
					 	dc.addAtt(db.getValue());
				  break;
				  case ASSIGNBOOLD:
						 dc.setBlood(db.getValue());
				  break;
				  case ASSIGNATT:
					   dc.setAtt(db.getValue());
				  break;
				  default:
				  break;
			  }
			  dc.getBuff().add(buffId);
		  }
	  }
  }
  public List<String> computeTargetIds(String roomId,String skillId,List<CardInfo> target){
	  List<String> ids=new ArrayList<>();
	  for(CardInfo card : target){
		  ids.add(card.getUniqueId());
	  }
	  return ids;
  }
  
  public List<CardInfo> computeTarget(String roomId,int position,String uniqueId,String skillId,String targetId){
	  BattleInfo att=attAndDef(roomId, uniqueId)[0];
	  BattleInfo def=attAndDef(roomId, uniqueId)[1];
	  List<CardInfo> list=new ArrayList<>();
	  D_Skill ds=DataBuilder.build(new D_Skill(skillId));
	  switch (ETarget.parse(ds.getTarget())) {
		  case NONE:
			  break;
		  case ENEMYASSIGN:
			  list.add(getTableCardInfo(roomId, targetId));
		  break;
		  case OURASSIGN:
		  	  list.add(getTableCardInfo(roomId, targetId));
		  case ENEMYALL:
			  list.addAll(def.getTableCard());
		  break;
		  case OURALL:
			  list.addAll(att.getTableCard());
		  break;
		  case ALL:
			  list.addAll(att.getTableCard());
			  list.addAll(def.getTableCard());
		  break;
		  case OURSIDE:
			  if(position-1>=0){//left
				  list.add(att.getTableCard().get(position-1));
			  }
			  if(position<att.getTableCard().size()-1){//right
				  list.add(att.getTableCard().get(position+1));
			  }
		  break;
		  case ENEMYSIDE:
			  int target=def.getTableCard().indexOf(getTableCardInfo(roomId, targetId));
			  if(target-1>=0){//left
				  list.add(def.getTableCard().get(target-1));
			  }
			  if(target<att.getTableCard().size()-1){//right
				  list.add(def.getTableCard().get(target+1));
			  }
		  break;
		  case OURRANDOMSIDE:
			  List<CardInfo> our=new ArrayList<>();
			  target=att.getTableCard().indexOf(getTableCardInfo(roomId, targetId));
			  if(position-1>=0){//left
				  our.add(att.getTableCard().get(position-1));
			  }
			  if(position<att.getTableCard().size()-1){//right
				  our.add(att.getTableCard().get(position+1));
			  }
			  Collections.shuffle(our);
			  list.add(our.get(0));
			  break;
		  case ENEMYRANDOMSIDE:
			  target=def.getTableCard().indexOf(getTableCardInfo(roomId, targetId));
			  List<CardInfo> roll=new ArrayList<>();
			  if(target-1>=0){//left
				  roll.add(def.getTableCard().get(target-1));
			  }
			  if(target<def.getTableCard().size()-1){//right
				  roll.add(def.getTableCard().get(target+1));
			  }
			  Collections.shuffle(roll);
			  list.add(roll.get(0));
			  break;
		  case OUR:
			  list.add(att.getTableCard().get(position-1));
			  break;
		  case RANDOMTWO:
			  Random random=new Random();
			  List<CardInfo> listDef= def.getTableCard();
			  list.add(listDef.get(random.nextInt(listDef.size())));
			  CardInfo info=null;
			  while(info==null || list.contains(info)){
				 info=listDef.get(random.nextInt(listDef.size()));
			  }
			  list.add(info);
			  break;
		  case ASSIGN:
			  target=def.getTableCard().indexOf(getTableCardInfo(roomId, targetId));
			  if(attOrDef(roomId, uniqueId)==1){//enemy 
				  list.add(att.getTableCard().get(target));
			  }else{//youself
				  list.add(def.getTableCard().get(target));
			  }
			  break;
		  default:
		  break;
	}
	  return list;
  }
  
  /**
   * notify compare.
   * @param before
   */
  public void compareNotify(RoomInfo before){
//	  RoomInfo curr=(RoomInfo) rooms.get(before.getId()).clone();//now room
//	  side(before.getAttInfo(), curr.getAttInfo());
//	  side(before.getDefInfo(), curr.getDefInfo());
//	  BattleNotify rn=new BattleNotify();
//	  rn.setRoom(curr);
//	  notifyService.notify(rn, curr.getAttInfo().getPid(),curr.getDefInfo().getPid());
  }
  
  public void side(BattleInfo before,BattleInfo curr){
	  if(curr.getBaseInfo().equals(before.getBaseInfo())) curr.setBaseInfo(null);
	  curr.getHandCard().removeAll(before.getHandCard());
	  curr.getHeapCard().removeAll(before.getHeapCard());
	  curr.getOutCard().removeAll(before.getOutCard());
	  curr.getTableCard().removeAll(before.getTableCard());
  }
  
  
  
  
  private class BattleRemoveThread implements Runnable{
	private final Map<String,Long> online=new ConcurrentHashMap<>();
	private final Long removeTime=10000L;//10 s timeout
	private boolean running;// is run
    /**
     *start thread
     */
    private void start()
    {
      this.running = true;
      Thread t = new Thread(this);
      t.setName("battle remove");
      t.start();
    }
    
    private void add(String pid,Long time)
    {
    	this.online.put(pid, time);
    }
    
	@Override
	public void run() {//timeout:remove battle user 
		for(String pid : online.keySet()){
			if(System.currentTimeMillis()-online.get(pid)>removeTime){
				online.remove(pid);
				remove(pid);
				LOG.error("remove :"+ pid);
			}
		}
	}
	
	/**
     * 是否正在运行
     *
     * @return
     */
    private boolean running()
    {
      return running;
    }

  } 
  
}
