package org.ngame.protocol.battle;

import lombok.Data;

import java.util.List;

import org.ngame.protocol.annotation.Proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;


@Data
@Proto(value = 26, description = "回合开始选择随机手牌")
public class RandomAgainRequest
{
	@Protobuf(description = "房间id")
	private String roomId;
	@Protobuf(description = "要随机掉的牌")
	private List<Integer> indexs;
}
