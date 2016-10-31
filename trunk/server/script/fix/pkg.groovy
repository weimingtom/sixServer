package fix;

import org.ngame.app.Application;
import org.ngame.domain.Player;
import org.ngame.util.RedisUtil;
import org.rojo.repository.Rojo;
import org.ngame.service.*;
import org.ngame.*;
import org.ngame.protocol.notify.*;

String pid="4f1";
BagService ps=Spring.bean(BagService.class);

ps.addItem(pid,"11_1",500);
ps.addItem(pid,"11_2",500);
ps.addItem(pid,"11_3",500);
ps.addItem(pid,"8_1",50);
ps.addItem(pid,"8_2",500);
ps.addItem(pid,"9_1",50);
ps.addItem(pid,"9_2",50);
ps.addItem(pid,"9_3",500);
ps.addItem(pid,"2_1",500);
ps.addItem(pid,"2_2",500);
ps.addItem(pid,"2_3",500);
ps.addItem(pid,"3_1",500);
ps.addItem(pid,"3_2",500);
ps.addItem(pid,"3_3",500);
ps.addItem(pid,"4_1",500);
ps.addItem(pid,"5_1",500);
ps.addItem(pid,"6_1",500);
ps.addItem(pid,"6_2",800);
ps.addItem(pid,"7_1",500);
ps.addItem(pid,"8_1",500);

ps.addGroup(pid,"1");