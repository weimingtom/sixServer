/**
 * 记录log
 */
package org.ngame.service;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author beykery
 */
@Component
public class LogService implements Runnable
{

  private static final Logger LOG = Logger.getLogger(LogService.class.getName());
  private final List<Object> buffer = new ArrayList<>();
  private final Object lock = new Object();
//  @Autowired
//  private HibernateDao dao;

  /**
   * 启动一个线程记录log
   */
  public LogService()
  {
    Thread t = new Thread(this);
    t.setName("db log");
    t.start();
  }

  /**
   * 记录到数据库
   *
   * @param log
   */
  public void log(Object log)
  {
    synchronized (lock)
    {
      buffer.add(log);
      lock.notify();
    }
  }

  @Override
  public void run()
  {
    while (true)
    {
      synchronized (lock)
      {
        if (buffer.size() >= 10)
        {
//          dao.saveOrUpdate(buffer);
          buffer.clear();
        } else
        {
          try
          {
            lock.wait();
          } catch (InterruptedException ex)
          {
            LOG.error("db log线程状态异常");
          }
        }
      }
    }
  }

}
