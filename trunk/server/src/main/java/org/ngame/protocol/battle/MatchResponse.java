package org.ngame.protocol.battle;

import lombok.Data;
import org.ngame.protocol.BaseResponse;
import org.ngame.protocol.annotation.Proto;
import org.ngame.protocol.domain.RoomInfo;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 *
 * @author beykery
 */
@Data
@Proto(value = -20, description = "匹配")
public class MatchResponse extends BaseResponse
{
   @Protobuf(description = "房间")
   private RoomInfo room;
}
