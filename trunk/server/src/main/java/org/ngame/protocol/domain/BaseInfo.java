/**
 * 基地
 */
package org.ngame.protocol.domain;

import lombok.Data;





import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

@Data
public class BaseInfo
{
  @Protobuf(description = "基地id")
  private String id;// id
  @Protobuf(description = "名称")
  private String name;// 名称
  @Protobuf(description = "血量")
  private int blood;
  @Protobuf(description = "消耗")
  private int cost;
  @Protobuf(description = "科技等级")
  private int science;
  @Protobuf(description = "技能")
  private String skill;
  @Protobuf(description = "美术资源")
  private String art;
  @Protobuf(description = "描述")
  private String desc;
  public BaseInfo()
  {
  }

  public BaseInfo(String id)
  {
    this.id = id;
  }
  
  public void addScience(int science){
	  this.science+=science;
  }
  
  public boolean subBlood(int hurt){
	  blood=blood-hurt <0 ? 0 : blood-hurt;
	  return blood == 0;
  }
  
}
