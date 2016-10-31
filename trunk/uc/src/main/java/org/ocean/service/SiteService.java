/**
 * 官网信息
 */
package org.ocean.service;

import org.ocean.dao.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author beykery
 */
@Component
public class SiteService
{

  @Autowired
  private BaseDao dao;
}
