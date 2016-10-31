/**
 * 资源更新
 */
package org.ngame.protocol.test;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import java.util.List;
import lombok.Data;
import org.ngame.protocol.BaseResponse;
import org.ngame.protocol.annotation.Proto;
import org.ngame.protocol.domain.ResInfo;

/**
 *
 * @author beykery
 */
@Data
//@Proto(value = -1000, description = "资源更新")
public class ResNotify extends BaseResponse
{

  @Protobuf(description = "资源列表")
  List<ResInfo> res;
}
