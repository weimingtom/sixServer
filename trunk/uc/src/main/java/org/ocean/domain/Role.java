/**
 * 角色
 */
package org.ocean.domain;

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
public class Role
{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(unique = true)
  private String name;//角色名
  @Column
  private String note;//描述
  @Column(length = 8192)
  private String ops;//对应的操作

  public long getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }

  public String getNote()
  {
    return note;
  }

  public String getOps()
  {
    return ops;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public void setOps(String ops)
  {
    this.ops = ops;
  }

}
