/**
 * 用于调试环境，列出当前连接的玩家
 */
import org.ngame.app.*;
import org.ngame.server.connector.*;
import org.ngame.server.*;
import org.ngame.socket.NClient;

List<Application> apps=Application.apps();
for(Application app:apps)
{
  BaseServer server=app.getServer();
  if(server instanceof ConnectorServer)
  {
    ConnectorServer c=(ConnectorServer)server;
    println("当前连接数 : "+c.getOuter().connections());
    Map<String, NClient> m=c.connections();
    for(NClient client:m.values())
    {
      print(client.getSessionId()+" : ");
      println(client);
    }
  }
}
