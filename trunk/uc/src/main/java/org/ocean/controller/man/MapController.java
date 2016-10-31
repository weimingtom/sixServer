/**
 * 地图
 */
package org.ocean.controller.man;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ARXrqdGSOy34yEvuzIDPd2Yi
 *
 * @author beykery
 */
@Controller
@RequestMapping(value = "/")
public class MapController
{

  @RequestMapping(value = "map.man")
  public String logout()
  {
    return "map";
  }
}
