/**
 * 飘字
 */
package org.ngame.protocol.notify;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import lombok.Data;
import org.ngame.domain.em.EMsg;
import org.ngame.protocol.BaseResponse;
import org.ngame.protocol.annotation.Proto;

/**
 *
 * @author beykery
 */
@Data
@Proto(value = -801, description = "飘字")
public class MsgNotify extends BaseResponse
{

  @Protobuf(description = "消息类型", fieldType = FieldType.ENUM)
  private EMsg type;
  @Protobuf(description = "消息")
  private String msg;
}
