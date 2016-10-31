/**
 * 客户端
 */
package org.ocean.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 *
 * @author beykery
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Client
{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column
  private long gid;//游戏id
  @Column
  private String version;//版本号
  @Column
  private Date updateTime;//更新时间
  @Column
  private String address;//更新地址
  @Column
  private boolean forceUpdate;//是否强制更新
  @Column
  private boolean open;//开启状态

  public String getAddress()
  {
    return address;
  }

  public long getGid()
  {
    return gid;
  }

  public long getId()
  {
    return id;
  }

  public Date getUpdateTime()
  {
    return updateTime;
  }

  public String getVersion()
  {
    return version;
  }

  public void setAddress(String address)
  {
    this.address = address;
  }

  public void setForceUpdate(boolean forceUpdate)
  {
    this.forceUpdate = forceUpdate;
  }

  public void setGid(long gid)
  {
    this.gid = gid;
  }

  public void setOpen(boolean open)
  {
    this.open = open;
  }

  public void setUpdateTime(Date updateTime)
  {
    this.updateTime = updateTime;
  }

  public void setVersion(String version)
  {
    this.version = version;
  }

  public boolean isOpen()
  {
    return open;
  }

  public boolean isForceUpdate()
  {
    return forceUpdate;
  }

}
