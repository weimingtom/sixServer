/**
 * room
 */
package org.ngame.protocol.domain;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

import lombok.Data;


/**
 *
 * @author csf
 */
@Data
public class RoomInfo implements Cloneable
{

    @Protobuf(description = "房间id")
	private String id;
    @Protobuf(description = "场景icon")
    private String background;
    @Protobuf(description = "我方信息")
	private BattleInfo attInfo;
    @Protobuf(description = "敌方信息")
	private BattleInfo defInfo;
    @Protobuf(description = "随机的索引")
    private int index;
	
	public BattleInfo getBattleInfo(String pid){
		if(attInfo.getPid().equals(pid)) return attInfo;
		else if(defInfo.getPid().equals(pid)) return defInfo;
		else return null;
	}
	
	@Override
	public Object clone() {  
		try {
			 return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
    }  
	
	public void addIndex(int i){
		index+=i;
	}
}
