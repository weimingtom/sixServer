/**
 * ping协议，保活
 */
package org.ngame.protocol.ping;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import lombok.Data;
import org.ngame.protocol.annotation.Proto;

/**
 *
 * @author beykery
 */
@Data
@Proto(value = 7, description = "ping协议，保活")
public class Ping
{

  @Protobuf(description = "客户端的时间")
  private long time;
}
