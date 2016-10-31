package simulate;

import org.ngame.protocol.player.*;
/**
 * 创建角色
 */
def create()
{
	CreatePlayerRequest request=new CreatePlayerRequest();
  request.setNickyName("beykery");
  request.setCamp(1);
  request.setIcon("icon");
  return request;
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