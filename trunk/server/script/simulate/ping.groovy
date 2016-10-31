package simulate;

import org.ngame.protocol.ping.*;
/**
 * ping协议
 */
def ping()
{
  Ping ping=new Ping();
  ping.setTime(0);
  return ping;
}