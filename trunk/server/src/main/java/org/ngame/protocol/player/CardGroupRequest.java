package org.ngame.protocol.player;

import lombok.Data;

import java.util.List;

import org.ngame.protocol.annotation.Proto;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 *
 * @author beykery
 */
@Data
@Proto(value = 8, description = "添加套牌")
public class CardGroupRequest
{
	@Protobuf(description = "套牌id")
	private String id;
	@Protobuf(description = "name")
	private String name;
	@Protobuf(description = "基地id")
	private String baseInitId;
	@Protobuf(description = "卡牌ids",fieldType=FieldType.STRING)
	private List<String> cardIds;
}
