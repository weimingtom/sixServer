package fix;

import org.ngame.app.Application;
import org.ngame.domain.Player;
import org.ngame.util.RedisUtil;
import org.rojo.repository.Rojo;
import org.ngame.service.*;
import org.ngame.*;
import org.ngame.protocol.notify.*;

String pid="4f1";
Player p=RedisUtil.rojo().get(Player.class,pid);
EquipService es=Spring.bean(EquipService.class);
es.addEquip(p, "1");
es.addEquip(p, "2");
es.addEquip(p, "3");
es.addEquip(p, "4");
es.addEquip(p, "5");
es.addEquip(p, "6");
es.addEquip(p, "7");
es.addEquip(p, "8");
es.addEquip(p, "9");
es.addEquip(p, "10");
es.addEquip(p, "11");
es.addEquip(p, "12");
es.addEquip(p, "13");