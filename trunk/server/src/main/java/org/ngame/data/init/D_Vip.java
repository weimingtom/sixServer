/**
 * vip配置
 */
package org.ngame.data.init;

import lombok.Data;
import org.ngame.annotation.Name;
import org.ngame.annotation.Root;

/**
 *
 * @author beykery
 */
@Data
@Root(value = "./script/vip")
public class D_Vip
{

  @Name
  private int level;// 等级
  private int exp;// 经验

  public D_Vip(int level)
  {
    this.level = level;
  }

  public D_Vip()
  {
  }

}
