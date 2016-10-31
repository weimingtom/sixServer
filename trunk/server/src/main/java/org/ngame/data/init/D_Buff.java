/**
 * buff
 */
package org.ngame.data.init;

import lombok.Data;


import org.ngame.annotation.Name;
import org.ngame.annotation.Root;

@Data
@Root(value = "./script/buff")
public class D_Buff
{

  @Name
  private String id;// id
  private int effect;// 效果
  private int effectRound;
  private int value;
  private int desc;
  public D_Buff()
  {
  }

  public D_Buff(String id)
  {
    this.id = id;
  }
}
