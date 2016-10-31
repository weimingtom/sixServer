import org.ngame.*;
import org.ngame.service.*;
import org.ngame.domain.*;
import org.ngame.util.*;
import org.rojo.repository.Rojo;
import org.rojo.util.*;

Cache cache=Rojo.getCache();
cache.evict(Player.class,"4f1");
println("ok.");
	