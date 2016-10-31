package fix;

import org.ngame.app.Application;
import org.ngame.domain.Player;
import org.ngame.util.RedisUtil;
import org.rojo.repository.Rojo;
import org.ngame.service.*;
import org.ngame.*;
import org.ngame.protocol.notify.*;

String pid="4f1";
PartnerService ps=Spring.bean(PartnerService.class);
ps.addPartner(pid,"1");