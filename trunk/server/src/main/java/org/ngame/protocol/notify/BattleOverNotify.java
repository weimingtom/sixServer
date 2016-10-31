/**
 * 资源更新
 */
package org.ngame.protocol.notify;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import lombok.Data;

import org.ngame.protocol.BaseResponse;
import org.ngame.protocol.annotation.Proto;

/**
 *
 * @author csf
 */
@Data
@Proto(value = -32, description = "战斗结果")
public class BattleOverNotify extends BaseResponse
{
   @Protobuf(description = "房间id")
   private String roomId;
   @Protobuf(description = "胜利true 失败false")
   private boolean flag;
}
