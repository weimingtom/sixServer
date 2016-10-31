/**
 * 测试用
 */
package org.ngame.protocol.test;

import org.ngame.domain.em.EMsg;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

import lombok.Data;

/**
 *
 * @author beykery
 */
@Data
//@Proto(value = 900, description = "测试em")
public class TestRequest
{

  @Protobuf(description = "信息类型", fieldType = FieldType.ENUM)
  private EMsg c;
}
