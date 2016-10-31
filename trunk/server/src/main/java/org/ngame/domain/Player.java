/**
 * player
 */
package org.ngame.domain;

import org.ngame.domain.em.EResType;
import lombok.Data;
import org.rojo.annotations.Entity;
import org.rojo.annotations.Id;
import org.rojo.annotations.Index;
import org.rojo.annotations.Value;

/**
 *
 * @author Beykery
 */
@Data
@Entity(cache = true)
public class Player
{

  @Id
  private String id;
  @Value
  private int serverid;// 服务器id
  @Value
  @Index
  private long ucenterId;//uc 上的user id
  @Value
  private String usercode;//uc上的usercode
  @Value
  private boolean testing;//测试账号

  @Value(unique = true)
  private String nickname;//昵称
  @Value
  private String icon;//头像
  @Value
  private int level;//等级
  @Value(sort = true, bigFirst = false, size = 0)
  private long exp;//经验值  

  @Value
  private long diamond;//钻石
  @Value
  private long gold;//黄金
  @Value
  private int energy;//体力
  @Value
  private int vip;//vip等级
  @Value
  private long totalDiamond;//（充入的总钻石数量,可以计算vip等级）
  @Value
  private int sum;//总场次
  @Value
  private int win;//赢的场次
  
  public long getRes(int type)
  {
    EResType etype = EResType.parse(type);
    switch (etype)
    {
      case DIAMOND:
        return this.diamond;
      case GOLD:
        return this.gold;
      case ENERGY:
        return this.energy;
      case EXP:
        return this.exp;
    }
    return 0L;
  }

  /**
   * 字段
   *
   * @param type
   * @return
   */
  public static String getField(int type)
  {
    EResType etype = EResType.parse(type);
    return getField(etype);
  }

  /**
   * field
   *
   * @param etype
   * @return
   */
  public static String getField(EResType etype)
  {
    switch (etype)
    {
      case DIAMOND:
        return "diamond";
      case GOLD:
        return "gold";
      case ENERGY:
        return "energy";
      case EXP:
        return "exp";
    }
    return null;
  }

  /**
   * 添加资源数量
   *
   * @param type
   * @param count
   * @return
   */
  public boolean addRes(int type, long count)
  {
    EResType etype = EResType.parse(type);
    return this.addRes(etype, count);
  }

  /**
   * 添加资源
   *
   * @param etype
   * @param count
   * @return
   */
  public boolean addRes(EResType etype, long count)
  {
    switch (etype)
    {
      case DIAMOND:
        this.diamond += count;
        this.diamond = diamond < 0 ? 0 : diamond;
        return true;
      case GOLD:
        this.gold += count;
        this.gold = gold < 0 ? 0 : gold;
        return true;
      case ENERGY:
        this.energy += count;
        this.energy = energy < 0 ? 0 : energy;
        return true;
      case EXP:
        this.exp += count;
        this.exp = exp < 0 ? 0 : exp;
        return true;
    }
    return false;
  }

  /**
   * 减资源
   *
   * @param type
   * @param count
   * @return
   */
  public boolean minusRes(int type, int count)
  {
    if (count <= 0)
    {
      return false;
    }

    EResType etype = EResType.parse(type);
    switch (etype)
    {
      case DIAMOND:
        if (this.diamond >= count)
        {
          this.diamond -= count;
          return true;
        }
        break;
      case GOLD:
        if (this.gold >= count)
        {
          this.gold -= count;
          return true;
        }
        break;
      case ENERGY:
        if (this.energy >= count)
        {
          this.energy -= count;
          return true;
        }
        break;
      case EXP:
        if (this.exp >= count)
        {
          this.exp -= count;
          return true;
        }
        break;
    }
    return false;
  }

  public boolean checkRes(int type, int count)
  {
    if (count <= 0)
    {
      return false;
    }

    EResType etype = EResType.parse(type);
    switch (etype)
    {
      case DIAMOND:
        return this.diamond >= count;
      case GOLD:
        return this.gold >= count;
      case ENERGY:
        return this.energy >= count;
      case EXP:
        return this.exp >= count;
    }
    return false;
  }

}
