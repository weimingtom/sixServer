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
import java.util.ArrayList;
import java.util.List;

//PlayerService ps=Spring.getBean(PlayerService.class);
/**
 * 初始化套牌
 */
def initCardGroup(Player p){
	CardService cs=Spring.bean(CardService.class);
	String baseInitId=1;
	String name="卡组1";
	List<String> cardIds=new ArrayList<>();
	cardIds.add("10100");cardIds.add("10100");
	cardIds.add("10101");cardIds.add("10101");
	cardIds.add("10102");cardIds.add("10102");
	cardIds.add("10103");cardIds.add("10103");
	cardIds.add("10104");cardIds.add("10104");
	cardIds.add("10105");cardIds.add("10105");
	cardIds.add("10106");cardIds.add("10106");
	cardIds.add("10107");cardIds.add("10107");
	cardIds.add("10108");cardIds.add("10108");
	cardIds.add("10109");cardIds.add("10109");
	cardIds.add("10150");cardIds.add("10150");
	cardIds.add("10151");cardIds.add("10151");
	cardIds.add("10152");cardIds.add("10152");
	cardIds.add("10153");cardIds.add("10153");
	cardIds.add("10154");cardIds.add("10154");
	cs.initCardGroup(p,name,baseInitId,cardIds);
}