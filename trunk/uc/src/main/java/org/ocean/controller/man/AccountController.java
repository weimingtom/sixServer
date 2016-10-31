/**
 * account控制器
 */
package org.ocean.controller.man;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.ocean.annotation.Auth;
import org.ocean.dao.BaseDao;
import org.ocean.domain.Account;
import org.ocean.domain.Role;
import org.ocean.service.AccountService;
import org.ocean.util.HttpUtil;
import org.ocean.Spring;
import org.ocean.domain.Game;
import org.ocean.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(value = "/")
@SessionAttributes("acc")//account是作用域为session
public class AccountController
{

  static final Logger logger = Logger.getLogger(AccountController.class.getName());
  public static final Map<Account, HttpSession> sessions = new ConcurrentHashMap<>();
  public static final Map<String, Account> accounts = new ConcurrentHashMap<>();

  @Autowired
  private BaseDao dao;
  @Autowired
  private AccountService accountService;

  /**
   * 登陆页面
   *
   * @return
   */
  @RequestMapping(value = "login.man", method = RequestMethod.GET)
  public String index()
  {
    return "login";
  }

  /**
   * 登陆页面
   *
   * @param session
   * @return
   */
  @RequestMapping(value = "logout.man")
  public String logout(HttpSession session)
  {
    session.invalidate();
    return "login";
  }

  /**
   * 进入管理页面
   *
   * @param request
   * @return
   */
  @RequestMapping(value = "admin.man", method = RequestMethod.GET)
  public String admin(HttpServletRequest request)
  {
    HttpSession session = request.getSession();
    if (session != null)
    {
      Account acc = (Account) session.getAttribute("acc");
      if (acc != null)
      {
        return "admin";
      }
    }
    return "login";
  }

  /**
   * 鉴权失败页面
   *
   * @return
   */
  @RequestMapping(value = "authError.man")
  public String authError()
  {
    return "authError";
  }

  @RequestMapping(value = "login.man", method = RequestMethod.POST)
  public String login(@RequestParam(value = "email") String email, @RequestParam(value = "pass") String pass, HttpSession session, ModelMap model)
  {
    Account acc = this.accountService.getAccount(email, pass);
    if (acc != null)
    {
      session.setAttribute("acc", acc);
      Role role = dao.get(Role.class, acc.getRole());
      session.setAttribute("role", role);
      HttpSession old = sessions.put(acc, session);
      accounts.put(session.getId(), acc);
      Game g = Spring.bean(GameService.class).getGameByMaxId();
      if (g != null)
      {
        session.setAttribute("curGame", g.getId());
      }
      if (old != null && old.getAttribute("acc") != null)
      {
        old.invalidate();
      }
      return "redirect:/admin.man";
    }
    model.addAttribute("error", Spring.message("account_login_error"));
    return "login";
  }

  /**
   * 重新登录
   *
   * @return
   */
  @RequestMapping(value = "sessionTimeout.man")
  public String sessionTimeout()
  {
    return "sessionTimeout";
  }

  @RequestMapping(value = "relogin.man")
  public String relogin(HttpServletResponse response, @RequestParam(value = "email") String email, @RequestParam(value = "pwd") String pass, HttpSession session, ModelMap model)
  {
    Account acc = this.accountService.getAccount(email, pass);
    if (acc != null)
    {
      session.setAttribute("acc", acc);
      Role role = dao.get(Role.class, acc.getRole());
      session.setAttribute("role", role);
      sessions.put(acc, session);
      accounts.put(session.getId(), acc);
      HttpUtil.response(response, "1");
    } else
    {
      HttpUtil.response(response, Spring.message("account_login_error"));
    }
    return null;
  }

  @Auth(note = "账号列表")
  @RequestMapping(value = "accountList.man")
  public String accountList()
  {
    return "accountList";
  }

  /**
   * 角色box
   *
   * @param aid
   * @param response
   * @return
   * @throws java.io.UnsupportedEncodingException
   */
  @RequestMapping(value = "rolesBox.man")
  public String rolesBox(@RequestParam(value = "aid") long aid, HttpServletResponse response) throws UnsupportedEncodingException
  {
    JSONArray arr = new JSONArray();
    Account acc = dao.get(Account.class, aid);
    Role role = acc == null ? null : dao.get(Role.class, acc.getRole());
    List<Role> rs = accountService.getRoles();
    for (Role r : rs)
    {
      JSONObject item = new JSONObject();
      item.put("rid", r.getId());
      item.put("role", r.getName());
      if (role != null && role.getId() == r.getId())
      {
        item.put("selected", true);
      }
      arr.add(item);
    }
    HttpUtil.response(response, arr.toString().getBytes("utf-8"));
    return null;
  }

  @RequestMapping(value = "accountJsonList.man")
  public String accountJsonList(HttpServletResponse response, @RequestParam(value = "page") int page, @RequestParam(value = "rows") int rows) throws UnsupportedEncodingException
  {
    List<Account> list = accountService.accountList(page, rows);
    JSONObject json = new JSONObject();
    json.put("total", accountService.accountTotal());
    JSONArray arr = new JSONArray();
    json.put("rows", arr);
    for (Account u : list)
    {
      Role r = dao.get(Role.class, u.getRole());
      JSONObject item = new JSONObject();
      item.put("email", u.getEmail());
      item.put("pwd", u.getPass());
      item.put("roleName", r == null ? null : r.getName());
      item.put("id", u.getId());
      arr.add(item);
    }
    HttpUtil.response(response, json.toString().getBytes("UTF-8"));
    return null;
  }

  @Auth(note = "账号删除")
  @RequestMapping(value = "deleteAccount.man")
  public String deleteAccount(HttpServletResponse response, @RequestParam(value = "id") long aid) throws UnsupportedEncodingException
  {
    boolean r = false;
    try
    {
      r = accountService.deleteAccount(aid);
    } catch (Exception e)
    {
    }
    HttpUtil.response(response, (r ? "1" : "无法删除指定账号").getBytes("utf-8"));
    return null;
  }

  /**
   * 添加账号
   *
   * @param response
   * @param request
   * @return
   * @throws java.io.UnsupportedEncodingException
   */
  @Auth(note = "账号添加")
  @RequestMapping(value = "addAccount.man")
  public String addAccount(HttpServletResponse response, WebRequest request) throws UnsupportedEncodingException
  {
    Map<String, String[]> ps = request.getParameterMap();
    String email = ps.get("email")[0];
    String pwd = ps.get("pwd")[0];
    long role = Long.parseLong(ps.get("role")[0]);
    Account a = new Account();
    a.setEmail(email);
    a.setCreateTime(new Date());
    a.setPass(pwd);
    a.setRole(role);
    a = dao.saveOrUpdate(a);
    HttpUtil.response(response, (a == null ? "添加账号操作失败" : "1").getBytes("utf-8"));
    return null;
  }

  /**
   * 修改account
   *
   * @param response
   * @param request
   * @return
   * @throws java.io.UnsupportedEncodingException
   */
  @Auth(note = "账号修改")
  @RequestMapping(value = "editAccount.man")
  public String editAccount(HttpServletResponse response, WebRequest request) throws UnsupportedEncodingException
  {
    Map<String, String[]> ps = request.getParameterMap();
    String email = ps.get("email")[0];
    String pwd = ps.get("pwd")[0];
    long role = ps.get("role") == null ? -1 : Long.parseLong(ps.get("role")[0]);
    long id = Long.parseLong(ps.get("id")[0]);
    Account a = dao.get(Account.class, id);
    if (a != null)
    {
      a.setEmail(email);
      a.setPass(pwd);
      a.setRole(role);
      a = dao.saveOrUpdate(a);
    }
    HttpUtil.response(response, (a == null ? "修改账号失败" : "1"));
    return null;
  }

  /**
   * session销毁
   *
   * @param sid
   */
  public static void sessionDestroyed(String sid)
  {
    Account acc = accounts.remove(sid);
    if (acc != null)
    {
      sessions.remove(acc);
    }
  }
}
