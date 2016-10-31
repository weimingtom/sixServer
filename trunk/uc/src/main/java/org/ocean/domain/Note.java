/**
 * 公告
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
public class Note
{

  public static final int STATUS_OPEN = 0;
  public static final int STATUS_CLOSED = 1;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column
  private long gid;
  @Column(length = 8192)
  private String tags;//json字符串，表达开启的tag集合
  @Temporal(TemporalType.TIMESTAMP)
  private Date ctime;
  @Temporal(TemporalType.TIMESTAMP)
  private Date ftime;
  @Column(length = 1024)
  private String content;
  @Column
  private String title;
  @Column
  private int status;

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getContent()
  {
    return content;
  }

  public void setContent(String content)
  {
    this.content = content;
  }

  public Date getCtime()
  {
    return ctime;
  }

  public Date getFtime()
  {
    return ftime;
  }

  public void setCtime(Date ctime)
  {
    this.ctime = ctime;
  }

  public void setFtime(Date ftime)
  {
    this.ftime = ftime;
  }

  public int getStatus()
  {
    return status;
  }

  public void setStatus(int status)
  {
    this.status = status;
  }

  public long getGid()
  {
    return gid;
  }

  public void setGid(long gid)
  {
    this.gid = gid;
  }

  public long getId()
  {
    return id;
  }

  public void setTags(String tags)
  {
    this.tags = tags;
  }

  public String getTags()
  {
    return tags;
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
      case STATUS_CLOSED:
        return "关闭";
      case STATUS_OPEN:
        return "开启";
    }
    return null;
  }
}
