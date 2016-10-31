package org.ngame.protocol.battle;

import lombok.Data;
import org.ngame.protocol.annotation.Proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;


@Data
@Proto(value = 27, description = "我选择死亡")
public class SurrenderRequest
{
	@Protobuf(description = "roomId")
	private String roomId;
}
