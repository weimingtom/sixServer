package org.ngame.protocol.battle;

import lombok.Data;
import org.ngame.protocol.annotation.Proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 *
 * @author beykery
 */
@Data
@Proto(value = 22, description = "base lv")
public class BaseLvRequest
{
	@Protobuf(description = "roomId")
	private String roomId;
}
