/**
 * ping协议的返回
 */
package org.ngame.protocol.ping;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import lombok.Data;
import org.ngame.protocol.BaseResponse;
import org.ngame.protocol.annotation.Proto;

/**
 *
 * @author beykery
 */
@Data
@Proto(value = -7,description = "ping协议的返回")
public class Pong extends BaseResponse
{

  @Protobuf(description = "服务器的时间")
  private long time;
}
