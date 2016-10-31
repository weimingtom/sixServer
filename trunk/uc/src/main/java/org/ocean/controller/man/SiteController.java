/**
 * 官网信息管理
 */
package org.ocean.controller.man;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.ocean.annotation.Auth;
import org.ocean.dao.BaseDao;
import org.ocean.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author beykery
 */
@Controller
@RequestMapping(value = "/")
public class SiteController
{

  @Autowired
  private BaseDao dao;
  @Autowired
  private SiteService siteSerivce;

  @Auth(note = "管网管理")
  @RequestMapping(value = "siteInfoList.man", method = RequestMethod.POST)
  public String siteInfoList(WebRequest request, HttpServletResponse response, HttpSession session)
  {
    return "siteInfoList";
  }
}
