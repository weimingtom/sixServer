package org.ngame.protocol.battle;

import lombok.Data;
import org.ngame.protocol.annotation.Proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 *
 * @author beykery
 */
@Data
@Proto(value = 24, description = "base action")
public class BaseActionRequest
{
	@Protobuf(description = "roomId")
	private String roomId;
	@Protobuf(description = "target")
	private String targetId;
}
