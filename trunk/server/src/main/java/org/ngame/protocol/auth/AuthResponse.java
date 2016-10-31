/**
 * 登陆返回
 */
package org.ngame.protocol.auth;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import lombok.Data;
import org.ngame.protocol.BaseResponse;
import org.ngame.protocol.annotation.Proto;

/**
 *
 * @author beykery
 */
@Data
@Proto(value = -6, description = "登陆返回")
public class AuthResponse extends BaseResponse
{

  @Protobuf(description = "用户的id==null")
  private String pid;
}
