/**
 * 渠道
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
    "gid", "name"
  })
})
public class Channel
{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(nullable = false)
  private long gid;//游戏id
  @Column
  private long clid;//版本信息
  @Column(unique = true)
  private int cid;//id(业务id)
  @Column(nullable = false)
  private String name;//名称
  @Column(nullable = false)
  private String tag;//分类
  @Column
  private String note;//描述
  @Temporal(TemporalType.TIMESTAMP)
  private Date createTime;//创建时间
  @Column
  private String bid;

  public long getGid()
  {
    return gid;
  }

  public void setGid(long gid)
  {
    this.gid = gid;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public int getCid()
  {
    return cid;
  }

  public long getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }

  public String getTag()
  {
    return tag;
  }

  public void setCid(int cid)
  {
    this.cid = cid;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void setTag(String tag)
  {
    this.tag = tag;
  }

  public void setCreateTime(Date createTime)
  {
    this.createTime = createTime;
  }

  public Date getCreateTime()
  {
    return createTime;
  }

  public void setClid(long clid)
  {
    this.clid = clid;
  }

  public long getClid()
  {
    return clid;
  }

  public void setBid(String bid)
  {
    this.bid = bid;
  }

  public String getBid()
  {
    return bid;
  }

  @Override
  public int hashCode()
  {
    int hash = 3;
    hash = 59 * hash + (int) (this.id ^ (this.id >>> 32));
    return hash;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj == null)
    {
      return false;
    }
    if (getClass() != obj.getClass())
    {
      return false;
    }
    final Channel other = (Channel) obj;
    return this.id == other.id;
  }
}
