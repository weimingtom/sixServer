/**
 * 监听session
 */
package org.ocean.session;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.ocean.controller.man.AccountController;

/**
 *
 * @author beykery
 */
public class SessionListener implements HttpSessionListener
{

  @Override
  public void sessionCreated(HttpSessionEvent se)
  {

  }

  @Override
  public void sessionDestroyed(HttpSessionEvent se)
  {
    AccountController.sessionDestroyed(se.getSession().getId());
  }

}
