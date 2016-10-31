/**
 * 登录验证
 */
package org.ocean.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.ocean.domain.Account;
import org.ocean.domain.Role;
import org.ocean.service.AuthService;
import org.ocean.Spring;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author beykery
 */
public class LoginInterceptor extends HandlerInterceptorAdapter
{

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
  {
    String uri = request.getRequestURI();
    if (uri.endsWith("login.man") || uri.endsWith("sessionTimeout.man"))//如果访问登陆界面，则直接放行
    {
      return true;
    }
    HttpSession session = request.getSession();
    if (session != null)
    {
      Account acc = (Account) session.getAttribute("acc");
      Role role = (Role) session.getAttribute("role");
      if (acc == null)//session里面木有
      {
        response.sendRedirect(request.getContextPath() + "/login.man");
        return false;
      }
      String op = uri.substring(uri.lastIndexOf('/') + 1);
      boolean r = Spring.bean(AuthService.class).auth(role, op);//鉴权
      if (!r)
      {
        response.sendRedirect(request.getContextPath() + "/authError.man");
      }
      return r;
    }
    return false;
  }
}
