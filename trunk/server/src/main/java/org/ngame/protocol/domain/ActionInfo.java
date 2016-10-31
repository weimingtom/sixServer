/**
 * 行动信息，只用于推送给前端做转发
 */
package org.ngame.protocol.domain;

import lombok.Data;





import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

@Data
public class ActionInfo
{
	@Protobuf(description = "行动的协议号")
	private int actionCmd;
	@Protobuf(description = "房间id")
	private String roomId;
	@Protobuf(description = "出牌索引")
	private int launch;
	@Protobuf(description = "出牌id")
	private String cardId;
	@Protobuf(description = "摆放位置")
	private int position;
	@Protobuf(description = "目标")
	private int target;
}
