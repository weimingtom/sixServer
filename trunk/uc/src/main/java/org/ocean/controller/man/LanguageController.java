/**
 * 切换语言
 */
package org.ocean.controller.man;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

/**
 *
 * @author beykery
 */
@Controller
@RequestMapping(value = "/")
public class LanguageController
{

  @Autowired
  private CookieLocaleResolver resolver;

  @RequestMapping(value = "language.man")
  public String language(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "language") String language)
  {
    language = language.toLowerCase();
    if (language == null || language.isEmpty())
    {
      return "redirect:/admin.man";
    } else if (language.equals("zh_cn"))
    {
      resolver.setLocale(request, response, Locale.CHINA);
    } else if (language.equals("en"))
    {
      resolver.setLocale(request, response, Locale.ENGLISH);
    } else if (language.equals("zh_tw"))
    {
      resolver.setLocale(request, response, Locale.TRADITIONAL_CHINESE);
    }
    return "redirect:/admin.man";
  }
}
