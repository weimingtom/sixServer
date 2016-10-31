package simulate;

import org.ngame.protocol.partner.*;

/**
 * 装备列表
 */
def list()
{
  PartnerListRequest request=new PartnerListRequest();
  request.setPid("6f1");
  return request;
}
