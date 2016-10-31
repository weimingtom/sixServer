/**
 * 初始化玩家数据
 */
import org.ngame.*;
import org.ngame.service.*;
import org.ngame.domain.*;

//PlayerService ps=Spring.getBean(PlayerService.class);
/**
 * 初始化player
 */
def initSupport(Support su)
{
  su.setOil(120);//体力补满
  su.setIronWorker(4);//四个钢铁工人
  su.setSupplyWorker(4);//四个军需工人
}