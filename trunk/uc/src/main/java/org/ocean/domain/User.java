/**
 * 注册账号
 */
package org.ocean.domain;

import com.alibaba.fastjson.JSONObject;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.bouncycastle.util.encoders.UrlBase64;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.ocean.util.DESMD5;

/**
 *
 * @author beykery
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User
{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(unique = true)
  private String usercode;
  @Column
  private String password;
  @Column
  private String passmd5;
  @Temporal(TemporalType.TIMESTAMP)
  private Date regTime;
  @Column
  private int channelId;//注册的时候写入的
  @Column
  private String phoneMsg;
  @Column
  private String params;
  @Column
  private int flag;
  @Column
  private int lockFlag;
  @Column
  private boolean testing;//测试账号
  @Temporal(TemporalType.TIMESTAMP)
  private Date unlockTime;//解禁日期
  @Column
  private long originGame;//注册时的游戏id
  @Column
  private String anyChannel;//any的渠道号
  @Column
  private String anyUid;//any的用户id

  public User()
  {
  }

  public User(String userCode, String password, int channelId, String phoneMsg, String params)
  {
    this.usercode = userCode;
    this.password = password;
    this.channelId = channelId;
    this.phoneMsg = phoneMsg;
    this.params = params;
    this.regTime = new Date();
  }

  public long getId()
  {
    return id;
  }

  public void setTesting(boolean testing)
  {
    this.testing = testing;
  }

  public boolean isTesting()
  {
    return testing;
  }

  public void setUsercode(String usercode)
  {
    this.usercode = usercode;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  public String getUsercode()
  {
    return usercode;
  }

  public String getPassword()
  {
    return password;
  }

  public int getChannelId()
  {
    return channelId;
  }

  public int getFlag()
  {
    return flag;
  }

  public int getLockFlag()
  {
    return lockFlag;
  }

  public String getParams()
  {
    return params;
  }

  public String getPhoneMsg()
  {
    return phoneMsg;
  }

  public Date getRegTime()
  {
    return regTime;
  }

  public Date getUnlockTime()
  {
    return unlockTime;
  }

  public void setChannelId(int channelId)
  {
    this.channelId = channelId;
  }

  public void setFlag(int flag)
  {
    this.flag = flag;
  }

  public void setLockFlag(int lockFlag)
  {
    this.lockFlag = lockFlag;
  }

  public void setParams(String params)
  {
    this.params = params;
  }

  public void setPhoneMsg(String phoneMsg)
  {
    this.phoneMsg = phoneMsg;
  }

  public void setRegTime(Date regTime)
  {
    this.regTime = regTime;
  }

  public void setUnlockTime(Date unlockTime)
  {
    this.unlockTime = unlockTime;
  }

  public void setOriginGame(long originGame)
  {
    this.originGame = originGame;
  }

  public long getOriginGame()
  {
    return originGame;
  }

  public void setAnyUid(String anyUid)
  {
    this.anyUid = anyUid;
  }

  public String getAnyUid()
  {
    return anyUid;
  }

  public void setAnyChannel(String anyChannel)
  {
    this.anyChannel = anyChannel;
  }

  public String getAnyChannel()
  {
    return anyChannel;
  }

  public void setPassmd5(String passmd5)
  {
    this.passmd5 = passmd5;
  }

  public String getPassmd5()
  {
    return passmd5;
  }

  /**
   * 加密过的loginId
   *
   * @param desKey
   * @return
   */
  public String getLoginId(String desKey)
  {
    try
    {
      JSONObject json = new JSONObject();
      json.put("id", id);
      json.put("usercode", usercode);
      json.put("testing", testing);
      String s = new String(UrlBase64.encode(DESMD5.encrypt(json.toString().getBytes("UTF-8"), desKey)), "UTF-8");
      return s;
    } catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }
}
