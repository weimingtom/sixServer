/**
 * 用户
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
public class Account
{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(unique = true)
  private String email;
  @Column
  private String pass;
  @Temporal(TemporalType.TIMESTAMP)
  private Date createTime;//创建时间
  @Column
  private long role;//分配的角色

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public long getId()
  {
    return id;
  }

  public void setPass(String pass)
  {
    this.pass = pass;
  }

  public String getPass()
  {
    return pass;
  }

  public void setCreateTime(Date createTime)
  {
    this.createTime = createTime;
  }

  public Date getCreateTime()
  {
    return createTime;
  }

  public void setRole(long role)
  {
    this.role = role;
  }

  public long getRole()
  {
    return role;
  }

  @Override
  public String toString()
  {
    return email;
  }

  @Override
  public int hashCode()
  {
    int hash = 5;
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
    final Account other = (Account) obj;
    return this.id == other.id;
  }
}
