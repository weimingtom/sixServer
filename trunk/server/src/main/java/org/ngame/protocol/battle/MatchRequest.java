package org.ngame.protocol.battle;

import lombok.Data;
import org.ngame.protocol.annotation.Proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

@Data
@Proto(value = 20, description = "match")
public class MatchRequest
{
	@Protobuf(description = "select the card group")
	private String cardGroupId;
}
