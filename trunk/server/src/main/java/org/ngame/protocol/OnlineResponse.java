/**
 * 用来表达协议的返回
 */
package org.ngame.protocol;

import java.util.List;

import org.ngame.protocol.annotation.Proto;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

import lombok.Data;

/**
 *
 * @author beykery
 */
@Data
@Proto(value=-9999,description = "这是一条服务器的转发")
public class OnlineResponse
{
	  @Protobuf(description = "online pids",fieldType=FieldType.STRING)
	  private List<String> pids;
}
