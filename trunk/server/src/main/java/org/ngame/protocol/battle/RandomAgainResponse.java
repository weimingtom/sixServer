package org.ngame.protocol.battle;

import lombok.Data;

import java.util.List;

import org.ngame.protocol.BaseResponse;
import org.ngame.protocol.annotation.Proto;
import org.ngame.protocol.domain.CardInfo;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 *
 * @author beykery
 */
@Data
@Proto(value = -26, description = "回合开始选择随机手牌")
public class RandomAgainResponse extends BaseResponse
{
	@Protobuf(description = "roomId")
	private String roomId;
	@Protobuf(description = "pid")
	private String pid;
	@Protobuf(description = "手牌信息")
	private List<CardInfo> hand;
}
