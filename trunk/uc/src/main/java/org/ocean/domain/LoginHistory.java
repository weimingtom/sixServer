/**
 * 最近登录
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
public class LoginHistory
{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column
  private long uid;//用户id
  @Column
  private int sid;//服务器id
  @Column
  private int gid;//游戏id
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastTime;//最近登录时间

  public long getId()
  {
    return id;
  }

  public Date getLastTime()
  {
    return lastTime;
  }

  public int getSid()
  {
    return sid;
  }

  public long getUid()
  {
    return uid;
  }

  public void setLastTime(Date lastTime)
  {
    this.lastTime = lastTime;
  }

  public void setUid(long uid)
  {
    this.uid = uid;
  }

  public int getGid()
  {
    return gid;
  }

  public void setSid(int sid)
  {
    this.sid = sid;
  }

  public void setGid(int gid)
  {
    this.gid = gid;
  }
}
