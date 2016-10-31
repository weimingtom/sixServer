package org.ngame.protocol.battle;

import lombok.Data;
import org.ngame.protocol.annotation.Proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 *
 * @author beykery
 */
@Data
@Proto(value = 21, description = "出牌")
public class EjectionRequest
{
	@Protobuf(description = "房间id")
	private String roomId;
	@Protobuf(description = "出牌id")
	private String uniqueId;
	@Protobuf(description = "摆放位置")
	private int position;
	@Protobuf(description = "目标")
	private String targetId;
}
