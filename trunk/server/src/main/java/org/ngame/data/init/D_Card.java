/**
 * 卡牌
 */
package org.ngame.data.init;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.ngame.annotation.Name;
import org.ngame.annotation.Root;
import org.ngame.protocol.domain.CardInfo;

import lombok.Data;

@Data
@Root(value = "./script/card")
public class D_Card
{

  @Name
  private String id;// id
  private String name;// 名称
  private int blood;
  private int att;
  private int race;
  private int type;
  private List<String> skill;//对应被动技能
  private int science;
  private int cost;
  private int status;//卡牌状态,暂定用于行动
  private List<String> buff;//
  private String art;

  public D_Card()
  {
  }

  public D_Card(String id)
  {
    this.id = id;
  }
  
  public void addStatus(int i){
	  status+=i;
  }
  
  public boolean subBlood(int i){
	  blood=blood-i <0 ? 0 : blood-i;
	  return blood == 0;
  }
  public void addBlood(int i){
	  blood+=i;
  }
  public void addAtt(int i){
	  att+=i;
  }
  
  public CardInfo change(){
	  CardInfo ci=new CardInfo();
	  ci.setUniqueId(UUID.randomUUID().toString().replaceAll("-", ""));
	  ci.setId(this.id);
	  ci.setName(this.name);
	  ci.setBlood(this.blood);
	  ci.setAtt(this.att);
	  ci.setRace(this.race);
	  ci.setType(this.type);
	  ci.setSkill(this.skill!=null && this.skill.size()>0 ? this.skill :  new ArrayList<String>());
	  ci.setCost(this.cost);
	  ci.setScience(this.science);
	  ci.setStatus(this.status);
	  ci.setBuff(this.buff!=null && this.buff.size()>0 ? this.buff : new ArrayList<String>());
	  ci.setArt(this.art);
	 
	  return ci;
  }
  
}
