/**
 * 官网信息
 */
package org.ocean.domain;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
public class SiteInfo
{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column
  private String title;
  @Column
  private String icon;
  @Column
  private int type;
  @Column
  private int level;
  @Column
  private String note;
  @Column
  private String addr;
  @Temporal(TemporalType.TIMESTAMP)
  private Date time;
  @Lob
  @Basic(fetch = FetchType.EAGER)
  private String content;

  public String getAddr()
  {
    return addr;
  }

  public String getContent()
  {
    return content;
  }

  public long getId()
  {
    return id;
  }

  public int getLevel()
  {
    return level;
  }

  public String getNote()
  {
    return note;
  }

  public Date getTime()
  {
    return time;
  }

  public String getTitle()
  {
    return title;
  }

  public int getType()
  {
    return type;
  }

  public void setAddr(String addr)
  {
    this.addr = addr;
  }

  public void setContent(String content)
  {
    this.content = content;
  }

  public void setLevel(int level)
  {
    this.level = level;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public void setTime(Date time)
  {
    this.time = time;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public void setType(int type)
  {
    this.type = type;
  }

  public void setIcon(String icon)
  {
    this.icon = icon;
  }

  public String getIcon()
  {
    return icon;
  }

}
