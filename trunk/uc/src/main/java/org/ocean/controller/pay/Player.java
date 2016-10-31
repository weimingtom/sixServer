/**
 * 游戏玩家
 */
package org.ocean.controller.pay;

/**
 *
 * @author beykery
 */
public class Player
{

  private String id;
  private int serverid;// 服务器id
  private long ucenterId;//ocean 上的user id
  private String usercode;//ocean上的usercode
  private boolean testing;//测试账号
  private String icon;//头像
  private int vip;//vip等级
  private int level;//等级
  private int sex;//性别
  private String nickyName;//昵称
  private int exp;//经验值  

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public int getServerid()
  {
    return serverid;
  }

  public void setServerid(int serverid)
  {
    this.serverid = serverid;
  }

  public long getUcenterId()
  {
    return ucenterId;
  }

  public void setUcenterId(long ucenterId)
  {
    this.ucenterId = ucenterId;
  }

  public String getUsercode()
  {
    return usercode;
  }

  public void setUsercode(String usercode)
  {
    this.usercode = usercode;
  }

  public boolean isTesting()
  {
    return testing;
  }

  public void setTesting(boolean testing)
  {
    this.testing = testing;
  }

  public String getIcon()
  {
    return icon;
  }

  public void setIcon(String icon)
  {
    this.icon = icon;
  }

  public int getVip()
  {
    return vip;
  }

  public void setVip(int vip)
  {
    this.vip = vip;
  }

  public int getLevel()
  {
    return level;
  }

  public void setLevel(int level)
  {
    this.level = level;
  }

  public int getSex()
  {
    return sex;
  }

  public void setSex(int sex)
  {
    this.sex = sex;
  }

  public String getNickyName()
  {
    return nickyName;
  }

  public void setNickyName(String nickyName)
  {
    this.nickyName = nickyName;
  }

  public int getExp()
  {
    return exp;
  }

  public void setExp(int exp)
  {
    this.exp = exp;
  }

}
