/**
 * 游戏服务器
 */
package org.ocean.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 *
 * @author beykery
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(uniqueConstraints =
{
  @UniqueConstraint(columnNames =
  {
    "gameId", "serverId"
  })
})
public class GameServer
{
//-1 维护中  1 畅通 2 火爆  5 新服

  public static final int STATUS_FIX = -1;
  public static final int STATUS_NORMAL = 1;
  public static final int STATUS_FIRE = 2;
  public static final int STATUS_NEW = 5;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column
  private long gameId;
  @Column
  private int serverId;
  @Column
  private String name;
  @Column
  private int status;
  @Column
  private String address;
  @Column
  private int riverPort;
  @Column
  private String innerIp;
  @Column
  private int innerPort = 22;
  @Column
  private String muser;
  @Column
  private String mpwd;
  @Column
  private String lastRelease;//上一个提交
  @Column
  private String redisIp;
  @Column
  private int redisPort;
  @Column
  private String redisPwd;
  @Column
  private int redisDb;
  @Column
  private String logUrl;
  @Column
  private String logUser;
  @Column
  private String logPwd;
  @Column
  private String note;
  @Column(length = 8192)
  private String tags;//json字符串，表达开启的tag集合
  @Temporal(TemporalType.TIMESTAMP)
  private Date createTime;//创建时间

  public String getTags()
  {
    return tags;
  }

  public void setTags(String tags)
  {
    this.tags = tags;
  }

  public void setGameId(long gameId)
  {
    this.gameId = gameId;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  public long getGameId()
  {
    return gameId;
  }

  public String getAddress()
  {
    return address;
  }

  public long getId()
  {
    return id;
  }

  public int getServerId()
  {
    return serverId;
  }

  public int getStatus()
  {
    return status;
  }

  public void setAddress(String address)
  {
    this.address = address;
  }

  public void setServerId(int serverId)
  {
    this.serverId = serverId;
  }

  public void setStatus(int status)
  {
    this.status = status;
  }

  public Date getCreateTime()
  {
    return createTime;
  }

  public void setCreateTime(Date createTime)
  {
    this.createTime = createTime;
  }

  public int getRiverPort()
  {
    return riverPort;
  }

  public void setRiverPort(int riverPort)
  {
    this.riverPort = riverPort;
  }

  public void setInnerPort(int innerPort)
  {
    this.innerPort = innerPort;
  }

  public int getInnerPort()
  {
    return innerPort;
  }

  public void setInnerIp(String innerIp)
  {
    this.innerIp = innerIp;
  }

  public String getInnerIp()
  {
    return innerIp;
  }

  public void setLastRelease(String lastRelease)
  {
    this.lastRelease = lastRelease;
  }

  public String getLastRelease()
  {
    return lastRelease;
  }

  public void setRedisPwd(String redisPwd)
  {
    this.redisPwd = redisPwd;
  }

  public void setRedisPort(int redisPort)
  {
    this.redisPort = redisPort;
  }

  public void setRedisIp(String redisIp)
  {
    this.redisIp = redisIp;
  }

  public void setRedisDb(int redisDb)
  {
    this.redisDb = redisDb;
  }

  public void setMuser(String muser)
  {
    this.muser = muser;
  }

  public void setMpwd(String mpwd)
  {
    this.mpwd = mpwd;
  }

  public void setLogUser(String logUser)
  {
    this.logUser = logUser;
  }

  public void setLogUrl(String logUrl)
  {
    this.logUrl = logUrl;
  }

  public void setLogPwd(String logPwd)
  {
    this.logPwd = logPwd;
  }

  public String getRedisPwd()
  {
    return redisPwd;
  }

  public int getRedisPort()
  {
    return redisPort;
  }

  public String getRedisIp()
  {
    return redisIp;
  }

  public int getRedisDb()
  {
    return redisDb;
  }

  public String getMuser()
  {
    return muser;
  }

  public String getMpwd()
  {
    return mpwd;
  }

  public String getLogUser()
  {
    return logUser;
  }

  public String getLogUrl()
  {
    return logUrl;
  }

  public String getLogPwd()
  {
    return logPwd;
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
      case STATUS_FIRE:
        return "火爆";
      case STATUS_FIX:
        return "维护";
      case STATUS_NEW:
        return "新服";
      case STATUS_NORMAL:
        return "畅通";
    }
    return null;
  }

}
