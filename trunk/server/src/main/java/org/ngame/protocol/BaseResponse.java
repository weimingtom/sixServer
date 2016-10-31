/**
 * 用来表达协议的返回
 */
package org.ngame.protocol;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 *
 * @author beykery
 */
public abstract class BaseResponse
{

  @Protobuf(order = 1, description = "是否成功")
  private boolean success=true;
  @Protobuf(order = 2, description = "错误消息")
  private String info;

  public boolean isSuccess()
  {
    return success;
  }

  public void setSuccess(boolean success)
  {
    this.success = success;
  }

  public void setInfo(String info)
  {
    this.info = info;
  }

  public String getInfo()
  {
    return info;
  }

}
