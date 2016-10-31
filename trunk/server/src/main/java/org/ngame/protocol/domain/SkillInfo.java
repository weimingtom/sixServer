/**
 * 卡牌
 */
package org.ngame.protocol.domain;



import java.util.List;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

import lombok.Data;

@Data
public class SkillInfo
{
  @Protobuf(description = "技能id")
  private String uniqueId;
  @Protobuf(description = "技能id")
  private String id;
  @Protobuf(description = "受影响的卡牌id")
  private List<String> target;

}
