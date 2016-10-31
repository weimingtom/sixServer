package org.ngame.protocol.battle;

import lombok.Data;
import org.ngame.protocol.BaseResponse;
import org.ngame.protocol.annotation.Proto;
import org.ngame.protocol.domain.RoundInfo;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 *
 * @author beykery
 */
@Data
@Proto(value = -25, description = "roundOver")
public class RoundResponse extends BaseResponse
{
   @Protobuf(description = "房间id")
   private String roomId;
   @Protobuf(description = "回合信息")
   private RoundInfo nri;
}
