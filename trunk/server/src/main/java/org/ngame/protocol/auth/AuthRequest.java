/**
 * connector上的验证请求
 */
package org.ngame.protocol.auth;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.ngame.protocol.annotation.Proto;
import lombok.Data;

/**
 *
 * @author beykery
 */
@Data
@Proto(value = 6, description = "connector上的验证请求")
public class AuthRequest
{
//  @Protobuf(required = true, description = "用户中心的加密id")
//  private String loginid;
  @Protobuf(description = "唯一id")
  private int ucenterId;
  @Protobuf(description = "用户名")
  private String userCode;
  @Protobuf(description = "是否是测试号")
  private boolean testing;
  ////////////////////////////////
  @Protobuf(required = true, description = "登陆的服务器id")
  private int serverid;
}
