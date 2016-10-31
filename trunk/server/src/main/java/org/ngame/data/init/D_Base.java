/**
 * 基地
 */
package org.ngame.data.init;

import lombok.Data;




import org.ngame.annotation.Name;
import org.ngame.annotation.Root;
import org.ngame.protocol.domain.BaseInfo;

@Data
@Root(value = "./script/base")
public class D_Base
{

  @Name
  private String id;// id
  private String name;// 名称
  private int blood;
  private int cost;
  private int science;
  private String skill;
  private String art;
  private String desc;
  public D_Base()
  {
  }

  public D_Base(String id)
  {
    this.id = id;
  }
  
  public void addScience(int science){
	  this.science+=science;
  }
  
  public BaseInfo change(){
	  BaseInfo bi=new BaseInfo();
	  bi.setArt(this.art);
	  bi.setBlood(this.blood);
	  bi.setCost(this.cost);
	  bi.setDesc(this.desc);
	  bi.setId(this.id);
	  bi.setName(this.name);
	  bi.setScience(this.science);
	  bi.setSkill(this.skill);
	  return bi;
  }
}
