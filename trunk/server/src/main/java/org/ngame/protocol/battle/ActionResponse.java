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
@Proto(value = -23, description = "卡牌行动")
public class ActionResponse extends BaseResponse
{
	   @Protobuf(description = "用户id")
	   private String pid;
	   @Protobuf(description = "卡牌id")
	   private String uniqueId;
	   @Protobuf(description = "目标")
	   private String targetId;
	   @Protobuf(description = "技能")
	   private List<SkillInfo> skill;
}
