/**
 * 系统设置
 */
package org.ngame.data.init;

import lombok.Data;
import org.ngame.annotation.Root;

/**
 *
 * @author chensf
 */
@Data
@Root(value = "./script/system/system.groovy")
public class D_System
{

  private int cost_rename = 100;//重命名花费钻石数量
  private int request_process_threshold = 100;//处理时间
  private int max_energy = 120;//最大体力
  private int pkg_size = 300;//背包大小
}
