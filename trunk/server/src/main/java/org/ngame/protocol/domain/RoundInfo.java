package org.ngame.protocol.domain;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

@Data
public class RoundInfo
{
	@Protobuf(description = "攻击方id")
	private String attId;
	@Protobuf(description = "回合数")
	private int round;
	@Protobuf(description = "资源数")
	private int resouce;
	@Protobuf(description = "新抓到的卡牌")
	private List<CardInfo> add=new LinkedList<>();
	@Protobuf(description = "需要加行动力的卡牌id,攻击方",fieldType=FieldType.STRING)
	private List<String> action=new LinkedList<>();
	@Protobuf(description = "需要移除buff的卡牌id,防守方",fieldType=FieldType.STRING)
	private List<String> remove=new LinkedList<>();
}
