package simulate;

import org.ngame.protocol.player.*;
import org.ngame.protocol.support.*;
import com.alibaba.fastjson.JSONObject;
import org.bouncycastle.util.encoders.UrlBase64;
import org.ngame.util.DESMD5;


/**
 * 创建角色
 */
def create()
{
	CreatePlayerRequest request=new CreatePlayerRequest();
  request.setNickyName("beykery");
  request.setCamp(1);
  request.setIcon("icon");
  request.setSex(1);
  request.setLoginid(getLoginId());
  return request;
}
/**
 * 生成一个loginid
 */ 
def getLoginId()
{
  JSONObject json = new JSONObject();
  json.put("id", 1);
  json.put("usercode", "beykery");
  json.put("testing", true);
  String s = new String(UrlBase64.encode(DESMD5.encrypt(json.toString().getBytes("UTF-8"), "421w6tW1ivg=")), "UTF-8");
  return s;
}
/**
 * 资源协议
 */
def res()
{
  ResRequest request=new ResRequest();
  request.setPid("1f1");
  return request;
}
/**
 * 随机一个昵称
 */
def randomName()
{
  RandomNickyNameRequest request=new RandomNickyNameRequest();
  request.setSex(0);
  return request;
}
/**
 * 重命名
 */
def rename()
{
  RenameAndIconRequest request=new RenameAndIconRequest();
  request.setNickyName("niha...");
  return request;
}