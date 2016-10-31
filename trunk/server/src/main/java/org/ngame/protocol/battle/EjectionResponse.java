package org.ngame.protocol.battle;

import lombok.Data;

import java.util.List;

import org.ngame.protocol.BaseResponse;
import org.ngame.protocol.annotation.Proto;
import org.ngame.protocol.domain.SkillInfo;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 *
 * @author beykery
 */
@Data
@Proto(value = -21, description = "出牌")
public class EjectionResponse extends BaseResponse
{
   @Protobuf(description = "用户id")
   private String pid;
   @Protobuf(description = "卡牌唯一id")
   private String uniqueId;
   @Protobuf(description = "卡牌id")
   private String cardId;
   @Protobuf(description = "摆放位置")
   private int position;
   @Protobuf(description = "目标")
   private int target;
   @Protobuf(description = "技能")
   private List<SkillInfo> skill;
}
