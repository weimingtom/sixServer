/**
 * 客户端version等
 */
package org.ocean.controller.pf;

import com.alibaba.fastjson.JSONObject;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.ocean.domain.Channel;
import org.ocean.domain.Client;
import org.ocean.service.ChannelService;
import org.ocean.service.ClientService;
import org.ocean.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author beykery
 */
@Controller
@RequestMapping(value = "/")
public class ClientPlatform
{

  @Autowired
  private ChannelService channelService;
  @Autowired
  private ClientService clientService;

  /**
   * 查询客户端版本状态
   *
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "version.pf")
  public String version(WebRequest request, HttpServletResponse response)
  {
    JSONObject json = new JSONObject();
    Map<String, String[]> ps = request.getParameterMap();
    String ch = ps.get("channel") == null ? null : ps.get("channel")[0];
    String version = ps.get("version") == null ? null : ps.get("version")[0];
    int cid = -1;
    try
    {
      cid = Integer.parseInt(ch);
    } catch (Exception e)
    {
    }
    if (cid > 0)
    {
      Channel channel = channelService.getChannelByCid(cid);
      if (channel != null)
      {
        Client client = this.clientService.getClientById(channel.getClid());
        if (client != null)
        {
          String v = client.getVersion();
          if (v != null && !v.equals(version) && client.isOpen() && client.getUpdateTime().getTime() < System.currentTimeMillis())//更新
          {
            json.put("version", v);
            json.put("address", client.getAddress());
            json.put("forceUpdate", client.isForceUpdate());
          }
        }
      }
    }
    json.put("successFlag", true);
    HttpUtil.response(response, json.toString());
    return null;
  }
}
