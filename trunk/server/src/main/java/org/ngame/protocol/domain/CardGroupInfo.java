/**
 * 卡牌
 */
package org.ngame.protocol.domain;



import java.util.List;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

import lombok.Data;

@Data
public class CardGroupInfo
{

  @Protobuf(description = "id")
  private String id;
  @Protobuf(description = "属于哪个玩家")
  private String pid;
  @Protobuf(description = "卡组名称")
  private String name;
  @Protobuf(description = "基地")
  private String baseInitId;
  @Protobuf(description = "卡牌组",fieldType=FieldType.STRING)
  private List<String> cardIds;
  
}
