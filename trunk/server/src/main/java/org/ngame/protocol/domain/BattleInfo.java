package org.ngame.protocol.domain;

import java.util.ArrayList;
import java.util.List;


import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

import lombok.Data;

@Data
public class BattleInfo {
	public static int baseLvBase=300;//基地升级基数
	@Protobuf(description = "玩家id")
	private String pid;
	@Protobuf(description = "玩家昵称")
	private String name;
	@Protobuf(description = "玩家等级")
	private int lv;
	@Protobuf(description = "玩家头像")
	private String icon;
	@Protobuf(description = "基地信息")
	private BaseInfo baseInfo;
	@Protobuf(description = "当前回合数")
	private int round;//当前回合
	@Protobuf(description = "资源")
	private int resource;
	@Protobuf(description = "手牌")
	private List<CardInfo> handCard=new ArrayList<>();
	@Protobuf(description = "桌子上的牌")
	private List<CardInfo> tableCard=new ArrayList<>();
	@Protobuf(description = "牌堆里的牌")
	private List<CardInfo> heapCard=new ArrayList<>();
	@Protobuf(description = "已使用的牌")
	private List<CardInfo> outCard=new ArrayList<>();
	
	public void addRound(int round){
		this.round+=round;
	}
	
	public void addResource(){
		this.resource+=50*2+50*(round-this.baseInfo.getScience());
	}
	
	public void subResource(int resource){
		this.resource-=resource;
	}
	
	public void addBaseLv(int science){
		this.baseInfo.addScience(science);
	}
}
