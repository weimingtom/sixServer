package fix;

import org.ngame.app.Application;
import org.ngame.domain.Player;
import org.ngame.util.RedisUtil;
import org.ngame.domain.*;
import org.rojo.repository.Rojo;
import java.util.*;

Rojo rojo = RedisUtil.rojo();
Set<Item> items = rojo.index(Item.class, "pid", "1f1");
for (Item item : items)
{
rojo.deleteAndFlush(item);
}