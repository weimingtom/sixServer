/**
 * 背包
 */
package org.ngame.domain;

import java.util.Map;
import org.rojo.annotations.Entity;
import org.rojo.annotations.Id;
import org.rojo.annotations.Value;
import lombok.Data;

/**
 * 消耗物品
 *
 * @author wangmanman
 */
@Data
@Entity(cache = true, idCache = false)
public class Bag
{

  @Id
  private String id;//id,pid
  /**
   * 所有物品
   */
  @Value
  private Map<String, Integer> items;

  /**
   * 添加物品
   *
   * @param item
   * @param count
   */
  public void addItem(String item, int count)
  {
    Map<String, Integer> map = this.items;
    if (map != null)
    {
      Integer old = map.get(item);
      if (old == null)
      {
        map.put(item, count < 0 ? 0 : count);
      } else
      {
        map.put(item, old + count < 0 ? 0 : old + count);
      }
    }
  }

  /**
   * 数量
   *
   * @param item
   * @return
   */
  public int count(String item)
  {
    Map<String, Integer> map = this.items;
    if (map != null)
    {
      Integer old = map.get(item);
      return old == null ? 0 : old;
    }
    return 0;
  }

}
