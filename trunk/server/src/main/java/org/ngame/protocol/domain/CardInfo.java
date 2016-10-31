/**
 * 卡牌
 */
package org.ngame.protocol.domain;



import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.ngame.data.init.D_Card;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

import lombok.Data;

@Data
public class CardInfo
{
  @Protobuf(description = "唯一id")
  private String uniqueId;
  @Protobuf(description = "id")
  private String id;
  @Protobuf(description = "名称")
  private String name;
  @Protobuf(description = "血量")
  private int blood;
  @Protobuf(description = "攻击")
  private int att;
  @Protobuf(description = "种族")
  private int race;
  @Protobuf(description = "类型")
  private int type;
  @Protobuf(description = "对应技能",fieldType=FieldType.STRING)
  private List<String> skill=new LinkedList<>();
  @Protobuf(description = "需要科技")
  private int science;
  @Protobuf(description = "消耗")
  private int cost;
  @Protobuf(description = "状态")
  private int status;
  @Protobuf(description = "行动力")
  private int action;
  @Protobuf(description = "buff",fieldType=FieldType.STRING)
  private List<String> buff=new LinkedList<>();
  @Protobuf(description = "美术资源")
  private String art;

  public CardInfo()
  {
  }

  public CardInfo(String id)
  {
    this.id = id;
  }
  
  public void addStatus(int i){
	  status+=i;
  }
  
  public boolean subBlood(int hurt){
	  blood=blood-hurt <0 ? 0 : blood-hurt;
	  return blood == 0;
  }
  public void addBlood(int i){
	  blood+=i;
  }
  public void addAtt(int i){
	  att+=i;
  }
  public void addAction(int i){
	  action+=i;
  }
  
  //this's place is card change , not need generate uuid
  public void change(D_Card dc){
	  this.id=dc.getId();
	  this.name=dc.getName();
	  this.blood=dc.getBlood();
	  this.att=dc.getAtt();
	  this.race=dc.getRace();
	  this.type=dc.getType();
	  if(Arrays.asList(dc.getSkill())!=null)
	  this.skill=dc.getSkill();
	  this.cost=dc.getCost();
	  this.science=dc.getScience();
	  this.status=dc.getStatus();
	  if(Arrays.asList(dc.getBuff())!=null)
	  this.buff=dc.getBuff();
	  this.art=dc.getArt();
  }
}
