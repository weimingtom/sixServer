/**
 * spring初始化容器完毕的监听
 */
package org.ocean;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.ocean.dao.BaseDao;
import org.ocean.domain.User;
import org.ocean.service.KeyWordsService;
import org.ocean.util.FileUtil;
import org.ocean.util.StringUtil;
import org.ocean.util.TimeUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 *
 * @author beykery
 */
public class BeansPostConstructorListerner implements ApplicationListener<ContextRefreshedEvent>
{

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event)
  {
    if (event.getApplicationContext().getParent() == null)
    {
      Spring.bean(KeyWordsService.class).init();//初始化脏词
    }
  }

}
