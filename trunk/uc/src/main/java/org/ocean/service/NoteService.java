/**
 * 公告服务
 */
package org.ocean.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.Query;
import org.hibernate.Session;
import org.ocean.dao.BaseDao;
import org.ocean.domain.Channel;
import org.ocean.domain.Game;
import org.ocean.domain.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author beykery
 */
@Component
public class NoteService
{

  @Autowired
  private BaseDao dao;

  /**
   * 公告列表
   *
   * @param gameId
   * @param page
   * @param rows
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<Note> noteList(Long gameId, int page, int rows)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("select n from Note as n where n.gid=:gid");
    q.setLong("gid", gameId);
    q.setFirstResult((page - 1) * rows);
    q.setMaxResults(rows);
    return q.list();
  }

  /**
   * 公告数量
   *
   * @param gameId
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public long noteTotal(Long gameId)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("select count(n.id) from Note n where n.gid=:gid");
    q.setLong("gid", gameId);
    long c = (Long) q.uniqueResult();
    return c;
  }

  /**
   * 渠道信息
   *
   * @param nid
   * @param gid
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public JSONArray getTags(long nid, Long gid)
  {
    JSONArray arr = new JSONArray();
    if (gid != null)
    {
      Session s = dao.getSession();
      Query q = s.createQuery("select c.tag from Channel c where c.gid=:gid");
      q.setLong("gid", gid);
      q.setCacheable(true);
      List l = q.list();
      Set<String> set = new HashSet<>(l);
      Note gs = s.get(Note.class, nid);
      JSONArray cur = null;
      String ts = gs == null ? null : gs.getTags();
      if (ts != null)
      {
        cur = JSONArray.parseArray(ts);
      }
      for (String t : set)
      {
        JSONObject item = new JSONObject();
        item.put("t", t);
        if (cur != null && cur.contains(t))
        {
          item.put("selected", true);
        }
        arr.add(item);
      }
    }
    return arr;
  }

  /**
   * 编辑note
   *
   * @param id
   * @param title
   * @param content
   * @param status
   * @param cd
   * @param fd
   * @param tags
   * @return
   */
  @Transactional
  public boolean editNote(long id, String title, String content, int status, Date cd, Date fd, String[] tags)
  {
    Session s = dao.getSession();
    Note note = s.get(Note.class, id);
    note.setTitle(title);
    note.setContent(content);
    note.setStatus(status);
    note.setCtime(cd);
    note.setFtime(fd);
    JSONArray arr = new JSONArray();
    if (tags != null && tags.length > 0)
    {
      arr.addAll(Arrays.asList(tags));
    }
    note.setTags(arr.toString());
    s.update(note);
    return true;
  }

  /**
   * 添加一个公告
   *
   * @param gid
   * @param title
   * @param content
   * @param status
   * @param cd
   * @param fd
   * @param tags
   * @return
   */
  public boolean addNote(long gid, String title, String content, int status, Date cd, Date fd, String[] tags)
  {
    Note note = new Note();
    note.setTitle(title);
    note.setContent(content);
    note.setStatus(status);
    note.setCtime(cd);
    note.setFtime(fd);
    note.setGid(gid);
    JSONArray arr = new JSONArray();
    if (tags != null && tags.length > 0)
    {
      arr.addAll(Arrays.asList(tags));
    }
    note.setTags(arr.toString());
    note = dao.saveOrUpdate(note);
    return note != null;
  }

  /**
   * 删除note
   *
   * @param nid
   * @return
   */
  @Transactional
  public boolean deleteNote(long nid)
  {
    Session s = dao.getSession();
    Note note = s.get(Note.class, nid);
    if (note != null)
    {
      s.delete(note);
      return true;
    }
    return false;
  }

  /**
   * 计算一个合适的note
   *
   * @param gid
   * @param t
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public Note getNote(long gid, String t)
  {
    long cur = System.currentTimeMillis();
    Session s = dao.getSession();
    Query q = s.createQuery("select n from Note as n where n.gid=:gid");
    q.setLong("gid", gid);
    List<Note> notes = q.list();
    for (Note n : notes)
    {
      if (n.getStatus() == Note.STATUS_OPEN && n.getTags() != null)
      {
        JSONArray arr = JSONArray.parseArray(n.getTags());
        if (arr.contains(t))
        {
          if (n.getCtime().getTime() < cur && n.getFtime().getTime() > cur)
          {
            return n;
          }
        }
      }
    }
    return null;
  }

  /**
   * 获取一个note
   *
   * @param game
   * @param ch
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public Note getNoteByGame(Game game, String ch)
  {
    long cur = System.currentTimeMillis();
    Session s = dao.getSession();
    Query q = s.createQuery("select c from Channel as c where c.name=:name and c.gid=:gid");
    q.setString("name", ch);
    q.setLong("gid", game.getId());
    q.setCacheable(true);
    Channel cn = (Channel) q.uniqueResult();

    if (cn != null)
    {
      q = s.createQuery("select n from Note as n where n.gid=:gid");
      q.setLong("gid", game.getId());
      q.setCacheable(true);
      List<Note> notes = q.list();
      for (Note n : notes)
      {
        if (n.getStatus() == Note.STATUS_OPEN && n.getTags() != null)
        {
          JSONArray arr = JSONArray.parseArray(n.getTags());
          if (arr.contains(cn.getTag()))
          {
            if (n.getCtime().getTime() < cur && n.getFtime().getTime() > cur)
            {
              return n;
            }
          }
        }
      }
    }
    return null;
  }

}
