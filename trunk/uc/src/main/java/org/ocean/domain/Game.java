/**
 * 游戏
 */
package org.ocean.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 *
 * @author beykery
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Game
{

  public static final int STATUS_DEV = -1;//开发
  public static final int STATUS_FIX = 0;//维护
  public static final int STATUS_TEST = 1;//测试
  public static final int STATUS_NORMAL = 2;//运营
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(unique = true)
  private int gameId;//内部代号
  @Column(unique = true)
  private String name;//名字,唯一
  @Column
  private String bid;//提供给苹果的bid
  @Column
  private int status;//状态
  @Column
  private String note;//描述
  @Temporal(TemporalType.TIMESTAMP)
  private Date createTime;//创建时间
  @Column
  private String desKey;//des秘钥
  @Column
  private String anyPrivateKey;//anysdk的privatekey

  /**
   * 默认给hibernate使用的，必须有
   */
  public Game()
  {
  }

  /**
   * 构造
   *
   * @param name
   * @param id
   * @param bid
   * @param status
   * @param note
   * @param desKey
   * @param anykey
   */
  public Game(String name, int id, String bid, int status, String note, String desKey, String anykey)
  {
    this.name = name;
    this.gameId = id;
    this.bid = bid;
    this.status = status;
    this.note = note;
    this.desKey = desKey;
    this.anyPrivateKey = anykey;
  }

  public long getId()
  {
    return id;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public String getNote()
  {
    return note;
  }

  public void setBid(String bid)
  {
    this.bid = bid;
  }

  public void setGameId(int gameId)
  {
    this.gameId = gameId;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void setStatus(int status)
  {
    this.status = status;
  }

  public String getBid()
  {
    return bid;
  }

  public int getGameId()
  {
    return gameId;
  }

  public String getName()
  {
    return name;
  }

  public int getStatus()
  {
    return status;
  }

  public Date getCreateTime()
  {
    return createTime;
  }

  public void setCreateTime(Date createTime)
  {
    this.createTime = createTime;
  }

  public void setDesKey(String desKey)
  {
    this.desKey = desKey;
  }

  public String getDesKey()
  {
    return desKey;
  }

  public String getAnyPrivateKey()
  {
    return anyPrivateKey;
  }

  public void setAnyPrivateKey(String anyPrivateKey)
  {
    this.anyPrivateKey = anyPrivateKey;
  }

  /**
   * 状态字符串
   *
   * @return
   */
  public String getStatusString()
  {
    switch (this.status)
    {
      case STATUS_DEV:
        return "开发";
      case STATUS_FIX:
        return "维护";
      case STATUS_NORMAL:
        return "运营";
      case STATUS_TEST:
        return "测试";
    }
    return null;
  }

}
