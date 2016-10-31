/**
 * 测试
 */
package org.ngame.server.home;

import org.ngame.annotation.RequestMapping;
import org.ngame.protocol.auth.AuthRequest;
import org.ngame.protocol.test.TestRequest;
import org.ngame.protocol.test.TestResponse;
import org.ngame.server.Context;
import org.springframework.stereotype.Component;

/**
 *
 * @author beykery
 */
@Component
public class TestHandler
{

  @RequestMapping(value = "testEM", auth = false)
  public TestResponse testEM(Context c, TestRequest request)
  {
    TestResponse response = new TestResponse();
    response.setC(request.getC());
    response.setSuccess(true);
    return response;
  }
  
  
  @RequestMapping(value = "version", auth = false)
  public AuthRequest version(Context c, AuthRequest request)
  {
//	  System.out.println(request.getPlatform()+"\t"+request.getChannelId()+"\t"+request.getVid());
//	  System.out.println(request.getLoginid()+"\t"+request.getServerid());
	  return request;
  }
}
