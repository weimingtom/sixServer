/**
 * 订单
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
public class Orderform
{

  public static final int STATUS_CREATED = 0;//已创建
  public static final int STATUS_OVER = 1;//已处理完毕
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(unique = true)
  private String oid;//订单号
  @Column
  private String anyOid;//any的订单号
  @Column
  private int gameId;
  @Column
  private int serverId;
  @Column
  private double price;//订单价格
  @Column
  private int chargeType;//订单类型
  @Column
  private int status;//状态
  @Column
  private String anyChannel;//渠道
  @Temporal(TemporalType.TIMESTAMP)
  private Date ctime;//创建时间
  @Temporal(TemporalType.TIMESTAMP)
  private Date ftime;//结束时间
  @Column
  private boolean manual;//手动操作完成订单
  @Column
  private String usercode;

  public String getAnyChannel()
  {
    return anyChannel;
  }

  public void setAnyChannel(String anyChannel)
  {
    this.anyChannel = anyChannel;
  }

  public String getAnyOid()
  {
    return anyOid;
  }

  public void setAnyOid(String anyOid)
  {
    this.anyOid = anyOid;
  }

  public Date getCtime()
  {
    return ctime;
  }

  public void setCtime(Date ctime)
  {
    this.ctime = ctime;
  }

  public Date getFtime()
  {
    return ftime;
  }

  public void setFtime(Date ftime)
  {
    this.ftime = ftime;
  }

  public int getGameId()
  {
    return gameId;
  }

  public void setGameId(int gameId)
  {
    this.gameId = gameId;
  }

  public long getId()
  {
    return id;
  }

  public void setOid(String oid)
  {
    this.oid = oid;
  }

  public String getOid()
  {
    return oid;
  }

  public void setId(long id)
  {
    this.id = id;
  }

  public void setPrice(double price)
  {
    this.price = price;
  }

  public double getPrice()
  {
    return price;
  }

  public void setServerId(int serverId)
  {
    this.serverId = serverId;
  }

  public void setStatus(int status)
  {
    this.status = status;
  }

  public int getStatus()
  {
    return status;
  }

  public int getServerId()
  {
    return serverId;
  }

  public int getChargeType()
  {
    return chargeType;
  }

  public void setChargeType(int chargeType)
  {
    this.chargeType = chargeType;
  }

  public void setManual(boolean manual)
  {
    this.manual = manual;
  }

  public boolean isManual()
  {
    return manual;
  }

  public void setUsercode(String usercode)
  {
    this.usercode = usercode;
  }

  public String getUsercode()
  {
    return usercode;
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
      case STATUS_CREATED:
        return "创建";
      case STATUS_OVER:
        return "完成";
    }
    return null;
  }

}
