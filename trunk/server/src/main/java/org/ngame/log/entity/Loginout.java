/**
 * 登入登出
 */
package org.ngame.log.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

/**
 *
 * @author beykery
 */
@Data
@Entity
public class Loginout
{

	public static final int IN = 1;
	public static final int OUT = 0;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Basic
	private Date createTime;
	@Basic
	private String pid;
	@Basic
	private boolean enter;
}
