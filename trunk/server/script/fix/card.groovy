package fix;

import org.ngame.app.Application;
import org.ngame.domain.Player;
import org.ngame.util.RedisUtil;
import org.rojo.repository.Rojo;
import org.ngame.service.*;
import org.ngame.*;
import org.ngame.protocol.notify.*;

/**
 * 初始化套牌
 */
def initCardGroup(){
	CardService cs=Spring.bean(CardService.class);
	int serverId=1;
	String pid="1f1"
	String baseInitId="1";
	List<String> cardIds=new ArrayList<>();
	cs.initCardGroup(serverId,pid,baseInitId,cardIds);
}
initCardGroup();