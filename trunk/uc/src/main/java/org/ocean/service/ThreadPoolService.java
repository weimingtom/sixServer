/**
 * 线程池
 */
package org.ocean.service;

import org.ocean.util.WorkerPool;
import org.springframework.stereotype.Component;

/**
 *
 * @author Admin
 */
@Component
public class ThreadPoolService
{

  private final WorkerPool pool;

  public ThreadPoolService()
  {
    pool = new WorkerPool(3);
  }

  public void put(Runnable run)
  {
    pool.execute(run);
  }
}
