/**
 * spring初始化容器完毕的监听
 */
package org.ngame;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 *
 * @author beykery
 */
public class BeansPostConstructorListerner implements ApplicationListener<ContextRefreshedEvent>
{

  private static final Logger LOG = Logger.getLogger(BeansPostConstructorListerner.class);

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event)
  {
    if (event.getApplicationContext().getParent() == null)
    {
      LOG.info("spring容器初始化完毕");
    }
  }

}
