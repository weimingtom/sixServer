/**
 * 卡牌
 */
package org.ngame.data.init;

import lombok.Data;


import org.ngame.annotation.Name;
import org.ngame.annotation.Root;

@Data
@Root(value = "./script/skill")
public class D_Skill
{

  @Name
  private String id;// id
  private String name;// 名称	
  private int trigger;
  private int target;
  private int skillType;
  private int value;
  private String[] summon;//召唤物id
  private String[] buff;
  private String desc;
  public D_Skill()
  {
  }

  public D_Skill(String id)
  {
    this.id = id;
  }
}
