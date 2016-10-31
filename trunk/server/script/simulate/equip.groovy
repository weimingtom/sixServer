package simulate;

import org.ngame.protocol.equip.*;

/**
 * 装备列表
 */
def list()
{
  EquipListRequest request=new EquipListRequest();
  request.setHid("6f1");
  return request;
}

def puton(){
	EquipPutOnRequest request=new EquipPutOnRequest();
	request.setEid("6f1");
	return request;
}