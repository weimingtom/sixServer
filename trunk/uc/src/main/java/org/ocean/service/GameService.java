/**
 * game服务
 */
package org.ocean.service;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.ocean.dao.BaseDao;
import org.ocean.domain.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author beykery
 */
@Component
public class GameService
{

  @Autowired
  private BaseDao dao;

  /**
   * game列表
   *
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<Game> getGames()
  {
    Session s = dao.getSession();
    Query q = s.createQuery("from Game");
    q.setCacheable(true);
    return q.list();
  }

  /**
   * 更新game
   *
   * @param id
   * @param gameId
   * @param gameName
   * @param gameBid
   * @param gameStatus
   * @param note
   * @param desKey
   * @param anykey
   * @return
   */
  public boolean updateGame(long id, int gameId, String gameName, String gameBid, int gameStatus, String note, String desKey, String anykey)
  {
    Game g = dao.get(Game.class, id);
    if (g != null)
    {
      g.setGameId(gameId);
      g.setBid(gameBid);
      g.setName(gameName);
      g.setStatus(gameStatus);
      g.setNote(note);
      g.setDesKey(desKey);
      g.setAnyPrivateKey(anykey);
      g = dao.saveOrUpdate(g);
    }
    return g != null;
  }

  /**
   * 删掉一个游戏
   *
   * @param gameId
   * @return
   */
  @Transactional
  public boolean deleteGame(long gameId)
  {
    try
    {
      Session s = dao.getSession();
      Game g = (Game) s.get(Game.class, gameId);
      if (g != null)
      {
        s.delete(g);
//        Query q = s.createQuery("delete from GameServer gs where gs.gameId=:gameId");
//        q.setLong("gameId", g.getId());
//        int n = q.executeUpdate();
        return true;
      }
    } catch (Exception e)
    {
    }
    return false;
  }

  /**
   * 根据gameId字段选择游戏
   *
   * @param gameId
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public Game getGame(int gameId)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("from Game g where g.gameId=:gameId");
    q.setInteger("gameId", gameId);
    q.setCacheable(true);
    Game g = (Game) q.uniqueResult();
    return g;
  }

  /**
   * 分页查看游戏
   *
   * @param page
   * @param rows
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<Game> getGames(int page, int rows)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("from Game g order by g.id desc");
    q.setMaxResults(rows);
    q.setFirstResult((page - 1) * rows);
    q.setCacheable(true);
    return q.list();
  }

  /**
   * game总数
   *
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public long totalGames()
  {
    Session s = dao.getSession();
    Query q = s.createQuery("select count(s.id) from Game s");
    q.setCacheable(true);
    long c = (Long) q.uniqueResult();
    return c;
  }

  /**
   * game
   *
   * @param id
   * @return
   */
  public Game getGameById(long id)
  {
    return dao.get(Game.class, id);
  }

  /**
   * 通过cid查找游戏
   *
   * @param cid
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public Game getGameByChannelid(int cid)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("select g from Game g,Channel c where c.cid=:cid and c.gid=g.id");
    q.setInteger("cid", cid);
    q.setCacheable(true);
    Game g = (Game) q.uniqueResult();
    return g;
  }

  /**
   * id最大的game
   *
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public Game getGameByMaxId()
  {
    Session s = dao.getSession();
    Query q = s.createQuery("from Game g order by g.id desc");
    q.setMaxResults(1);
    q.setFirstResult(0);
    q.setCacheable(true);
    return (Game) q.uniqueResult();
  }
}
