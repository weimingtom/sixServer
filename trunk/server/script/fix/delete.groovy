package fix;

import org.ngame.app.Application;
import org.ngame.domain.Player;
import org.ngame.util.RedisUtil;
import org.ngame.domain.*;
import org.rojo.repository.Rojo;
import java.util.*;

Rojo rojo = RedisUtil.rojo();
Set<Unit> items = rojo.index(Unit.class, "pid", "1f1");
for (Unit item : items)
{
rojo.deleteAndFlush(item);
Set<Equip> es = rojo.index(Equip.class, "uid", item.getId());
for(Equip e : es)
{
rojo.deleteAndFlush(e);
}
}