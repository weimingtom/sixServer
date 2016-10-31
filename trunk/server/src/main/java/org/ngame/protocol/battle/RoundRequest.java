package org.ngame.protocol.battle;

import lombok.Data;
import org.ngame.protocol.annotation.Proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 *
 * @author beykery
 */
@Data
@Proto(value = 25, description = "roundOver")
public class RoundRequest
{
	@Protobuf(description = "roomId")
	private String roomId;
}
