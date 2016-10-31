package simulate;

import org.ngame.protocol.player.*;
import org.ngame.protocol.support.*;
import org.ngame.protocol.hero.*;
import com.alibaba.fastjson.JSONObject;
import org.bouncycastle.util.encoders.UrlBase64;
import org.ngame.util.DESMD5;


/**
 * 英雄列表
 */
def list()
{
	HeroListRequest request=new HeroListRequest();
  return request;
}