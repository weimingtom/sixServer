/**
 * 资源更新
 */
package org.ngame.protocol.notify;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import lombok.Data;

import java.util.List;

import org.ngame.protocol.BaseResponse;
import org.ngame.protocol.annotation.Proto;
import org.ngame.protocol.domain.CardGroupInfo;

/**
 *
 * @author csf
 */
@Data
@Proto(value = -30, description = "套牌推送")
public class CardGroupNotify extends BaseResponse
{
   @Protobuf(description = "玩家id")
   private String pid;
   @Protobuf(description = "套牌组")
   private List<CardGroupInfo> cardGroup;
}
