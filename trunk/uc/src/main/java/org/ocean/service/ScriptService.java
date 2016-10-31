/**
 * 脚本service
 */
package org.ocean.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.Session;
import org.ocean.dao.BaseDao;
import org.ocean.domain.GameServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 操作脚本
 *
 * @author beykery
 */
@Component
public class ScriptService
{

  @Autowired
  private BaseDao dao;

  /**
   * 服务器
   *
   * @param gid
   * @param ss
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<GameServer> getServers(long gid, int[] ss)
  {
    Session s = dao.getSession();
    String query;
    if (ss.length == 1 && ss[0] == -1)//全部服务器
    {
      query = "from GameServer gs where gs.gameId=:gid";
      Query q = s.createQuery(query);
      q.setLong("gid", gid);
      q.setCacheable(true);
      return q.list();
    } else
    {
      query = "from GameServer gs where gs.gameId=:gid and gs.serverId=:sid";
      Map<String, GameServer> m = new HashMap<>();
      for (int i = 0; i < ss.length; i++)
      {
        Query q = s.createQuery(query);
        q.setLong("gid", gid);
        q.setInteger("sid", ss[i]);
        q.setCacheable(true);
        GameServer temp = (GameServer) q.uniqueResult();
        if (temp != null)
        {
          m.put(temp.getAddress(), temp);//地址相同的合服
        } else
        {
          return null;
        }
      }
      Collection<GameServer> temp = m.values();
      List<GameServer> list = new ArrayList<>();
      list.addAll(temp);
      return list;
    }
  }
}
