package org.ngame.protocol.test;

import lombok.Data;

import org.ngame.protocol.annotation.Proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

@Data
//@Proto(value = 11010, description = "测试")
public class VersionRequest {
	  @Protobuf(required = true)
	  private String platform;
	  @Protobuf
	  private int channelId;
	  @Protobuf
	  private int vid;
}
