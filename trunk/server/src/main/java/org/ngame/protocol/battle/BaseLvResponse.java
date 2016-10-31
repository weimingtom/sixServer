package org.ngame.protocol.battle;

import lombok.Data;
import org.ngame.protocol.BaseResponse;
import org.ngame.protocol.annotation.Proto;
import org.ngame.protocol.domain.BaseInfo;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 *
 * @author beykery
 */
@Data
@Proto(value = -22, description = "基地升级")
public class BaseLvResponse extends BaseResponse
{
	@Protobuf(description = "玩家id")
	private String pid;
	@Protobuf(description = "资源数")
	private int resource;
	@Protobuf(description = "base lv")
	private int baseLv;
}
