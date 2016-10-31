package org.ngame.protocol.battle;

import lombok.Data;
import org.ngame.protocol.annotation.Proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 *
 * @author beykery
 */
@Data
@Proto(value = 23, description = "卡牌行动")
public class ActionRequest
{
	@Protobuf(description = "房间id")
	private String roomId;
	@Protobuf(description = "出牌id")
	private String uniqueId;
	@Protobuf(description = "目标")
	private String targetId;
}
