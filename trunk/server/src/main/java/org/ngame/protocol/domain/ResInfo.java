/**
 * 资源
 */
package org.ngame.protocol.domain;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import lombok.Data;
import org.ngame.domain.em.EResType;

/**
 *
 * @author beykery
 */
@Data
public class ResInfo
{

  @Protobuf(description = "资源类型", fieldType = FieldType.ENUM)
  private EResType type;
  @Protobuf(description = "资源数量")
  private long count;

  /**
   * 构造
   *
   * @param type
   * @param count
   */
  public ResInfo(EResType type, int count)
  {
    this.type = type;
    this.count = count;
  }

  public ResInfo()
  {
  }

}
