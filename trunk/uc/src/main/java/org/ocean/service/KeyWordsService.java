/**
 * 过滤关键词
 */
package org.ocean.service;

import java.util.Collection;
import java.util.List;
import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import org.hibernate.Query;
import org.hibernate.Session;
import org.ocean.dao.BaseDao;
import org.ocean.domain.DirtyWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author beykery
 */
@Component
public class KeyWordsService
{

  private Trie.TrieBuilder bu;
  private Trie searcher;
  @Autowired
  private BaseDao dao;

  /**
   * 初始化关键词（读取数据库，初始化之）
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public void init()
  {
    bu = Trie.builder().caseInsensitive();
    Session s = dao.getSession();
    Query q = s.createQuery("select dw from DirtyWord dw order by dw.id");
    List<DirtyWord> dws = q.list();
    for (DirtyWord dw : dws)
    {
      bu.addKeyword(dw.getWord());
    }
    searcher = bu.build();
  }

  /**
   * 增添关键词
   *
   * @param word
   * @param note
   * @return
   */
  public boolean addWord(String word, String note)
  {
    DirtyWord dw = new DirtyWord();
    dw.setWord(word);
    dw.setNote(note);
    dw = dao.saveOrUpdate(dw);
    if (dw != null)
    {
      this.bu.addKeyword(word);
      return true;
    }
    return false;
  }

  /**
   * 是否合法
   *
   * @param text
   * @return
   */
  public boolean validate(String text)
  {
    return searcher.parseText(text).isEmpty();
  }

  /**
   * 过滤脏词
   *
   * @param content
   * @return
   */
  public String replace(String content)
  {
    Collection<Emit> emits = searcher.parseText(content);
    if (!emits.isEmpty())
    {
      StringBuilder sb = new StringBuilder();
      int start = 0;
      for (Emit e : emits)
      {
        sb.append(content.substring(start, e.getStart()));
        sb.append("**");
        start = e.getEnd() + 1;
      }
      sb.append(content.substring(start));
      return sb.toString();
    }
    return content;
  }

  /**
   * 查找dirtyword
   *
   * @param id
   * @return
   */
  public DirtyWord getDirty(long id)
  {
    return dao.get(DirtyWord.class, id);
  }

  /**
   * 删掉脏词
   *
   * @param id
   * @return
   */
  @Transactional
  public boolean deleteDirty(long id)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("delete DirtyWord dw where dw.id=:id");
    q.setLong("id", id);
    boolean r = q.executeUpdate() > 0;
    if (r)
    {
      this.init();
    }
    return r;
  }

  @Transactional
  public boolean deleteDirty(String dirty)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("delete DirtyWord dw where dw.word=:dirty");
    q.setString("dirty", dirty);
    boolean r = q.executeUpdate() > 0;
    if (r)
    {
      this.init();
    }
    return r;
  }

  /**
   * dirty列表
   *
   * @param page
   * @param rows
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<DirtyWord> dirtyList(int page, int rows)
  {
    Session s = dao.getSession();
    Query q = s.createQuery("from DirtyWord as dw order by dw.id");
    q.setFirstResult((page - 1) * rows);
    q.setMaxResults(rows);
    q.setCacheable(true);
    return q.list();
  }

  /**
   * dirty总数量
   *
   * @return
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public long dirtyTotal()
  {
    Session s = dao.getSession();
    Query q = s.createQuery("select count(dw.id) from DirtyWord as dw");
    q.setCacheable(true);
    return (long) q.uniqueResult();
  }
}
