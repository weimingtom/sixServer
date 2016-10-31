/**
 * player
 */
package org.ngame.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.ngame.Spring;
import org.ngame.domain.Bag;
import org.ngame.domain.em.EResType;
import org.ngame.domain.Player;
import org.ngame.script.ScriptEngine;
import org.ngame.util.MathUtil;
import org.ngame.util.RedisUtil;
import org.rojo.repository.Rojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author beykery
 */
@Component
public class PlayerService
{

  private static final File systemFile = new File("./script/system/system.groovy");
  //      int cost = engine.getInt(systemFile, "cost_rename");
  private static final File initFile = new File("./script/player/init.groovy");

  @Autowired
  private ScriptEngine engine;
  @Autowired
  private NotifyService notify;

  public PlayerService()
  {
  }

  /**
   * 获取palyer详情
   *
   * @param id
   * @return
   */
  public Player getPlayerInfo(String id)
  {
    Rojo rojo = RedisUtil.rojo();
    Player p = rojo.get(Player.class, id);
    return p;
  }

  /**
   * 昵称获取player
   *
   * @param name
   * @return
   */
  public Player getPlayerByNickyName(String name)
  {
    return RedisUtil.rojo().unique(Player.class, name);
  }

  /**
   * 随机角色名
   *
   * @param sex
   * @return
   */
  public String getRandomName(int sex)
  {
    File f = new File("./script/player/names.groovy");
    ScriptEngine se = this.engine;
    StringBuilder sb = new StringBuilder();
    String[] first = (String[]) se.getProperty(f, "firstname");
    String[] second = (String[]) se.getProperty(f, sex == 1 ? "man" : "woman");
    sb.append(first[(int) (Math.random() * first.length)]);
    sb.append(second[(int) (Math.random() * second.length)]);
    String name = sb.toString();
    Rojo rojo = RedisUtil.rojo();
    int index = 1;
    while (rojo.unique(Player.class, name) != null)
    {
      name = sb.toString() + index;
      index++;
    }
    return name;
  }

  /**
   * 随机名字
   *
   * @return
   */
  public String getRandomName()
  {
    return this.getRandomName(MathUtil.nextInt(2));
  }

  /**
   * 根据用户中心id和服务器id计算player
   *
   * @param ucenterId
   * @param serverid
   * @return
   */
  public Player getPlayer(long ucenterId, int serverid)
  {
    Rojo rojo = RedisUtil.rojo();
    Set<Player> ps = rojo.index(Player.class, "ucenterId", ucenterId);
    for (Player p : ps)
    {
      if (p.getServerid() == serverid)
      {
        return p;
      }
    }
    return null;
  }

  /**
   * 创建角色
   *
   * @param serverid
   * @param ucenterId
   * @param usercode
   * @param testing
   * @param nickyName
   * @param suit
   * @return
   */
  public Player createPlayer(int serverid, long ucenterId, String usercode, boolean testing, String nickyName)
  {
    if (Spring.bean(DirtyService.class).contains(nickyName))
    {
      return null;
    }
    Rojo rojo = RedisUtil.rojo();
    Player p = new Player();
    p.setId(RedisUtil.id(rojo, Player.class, serverid));
    p.setNickname(nickyName);
    p.setServerid(serverid);
    p.setUcenterId(ucenterId);
    p.setLevel(1);
    p.setUsercode(usercode);
    p.setTesting(testing);
    String pid = rojo.saveAndFlush(p);
    if (pid != null)
    {
      //初始化英雄配置
//      HeroConfig config = new HeroConfig();
//      config.setId(pid);
//      rojo.saveAndFlush(config);
//      engine.executeMethod(initFile, "initHero", p);
//      engine.executeMethod(initFile, "initEquip", p);
//      Bag bag = new Bag();
//      bag.setId(pid);
//      rojo.saveAndFlush(bag);
    	engine.executeMethod(initFile, "initCardGroup",p);
    }
    return pid == null ? null : p;
  }

  /**
   * 添加资源
   *
   * @param playerId
   * @param type
   * @param count
   * @return
   */
  public boolean addRes(String playerId, int type, long count)
  {
    Rojo rojo = RedisUtil.rojo();
    Player p = rojo.get(Player.class, playerId);
    return this.addRes(p, EResType.parse(type), count);
  }

  /**
   * 减少资源
   *
   * @param playerId
   * @param type
   * @param count
   * @return
   */
  public boolean minusResource(String playerId, int type, int count)
  {
    Rojo rojo = RedisUtil.rojo();
    Player p = rojo.get(Player.class, playerId);
    boolean flag = p.minusRes(type, count);
    if (flag)
    {
      rojo.updateAndFlush(p);
    }
    return flag;
  }

  /**
   * 检查资源是否足够
   *
   * @param playerId
   * @param type
   * @param count
   * @return
   */
  public boolean checkResource(String playerId, int type, int count)
  {
    Rojo rojo = RedisUtil.rojo();
    Player p = rojo.get(Player.class, playerId);
    return p.checkRes(type, count);
  }

  /**
   * 加资源（未计算exp导致的level升级）
   *
   * @param p
   * @param t
   * @param count
   * @return
   */
  public boolean addRes(Player p, EResType t, long count)
  {
    Rojo rojo = RedisUtil.rojo();
    boolean r = p.addRes(t.getValue(), count);
    if (r)
    {
      rojo.updateAndFlush(p, Player.getField(t.getValue()));
//      notify.notifyRes(p.getId(), t.getValue(), p.getRes(t.getValue()));
    }
    return r;
  }

//  /**
//   * 计算随机库对应的奖励(所有可能的奖励都算进来)
//   *
//   * @param rds
//   * @return
//   */
//  public RewardInfo getRewardInfo(String... rds)
//  {
//    RewardInfo ri = new RewardInfo();
//    for (String item : rds)
//    {
//      D_Group dg = DataBuilder.build(new D_Group(item));
//      if (dg.getRes_type() != null)
//      {
//        for (int i = 0; i < dg.getRes_type().length; i++)
//        {
//          ri.addRes(EResType.parse(dg.getRes_type()[i]), dg.getRes_count()[i]);
//        }
//      }
//      if (dg.getItems() != null)
//      {
//        for (int i = 0; i < dg.getItems().length; i++)
//        {
//          ri.addItem(notify.itemInfo(dg.getItems()[i], dg.getItem_count()[i]));
//        }
//      }
//      if (dg.getEquips() != null)
//      {
//        for (int i = 0; i < dg.getEquips().length; i++)
//        {
//          ri.addEquip(notify.equipInfo(dg.getEquips()[i]), dg.getEquip_count()[i]);
//        }
//      }
//    }
//    return ri;
//  }
  	
  
  /**
   * 返回当前匹配
   * @return
   */
  public Player playerStatus(){
	  Rojo rojo = RedisUtil.rojo();
	  Set<Player> all=rojo.all(Player.class, 0, -1);
	  for(Player p : all){
//		  if(p.getStatus()==1){
//			  return p;
//		  }
	  }
	  return null;
  }
}