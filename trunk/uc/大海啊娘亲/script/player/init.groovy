/**
 * 初始化玩家数据
 */
import org.ngame.*;
import org.ngame.service.*;
import org.ngame.domain.*;
import org.rojo.repository.*;
import org.ngame.util.*;
import org.ngame.service.*;
import org.ngame.*;


//PlayerService ps=Spring.getBean(PlayerService.class);
/**
 * 初始化player
 */
def initSupport(Support su)
{
  su.setGold(10000);
  su.setDiamond(1000);
}
