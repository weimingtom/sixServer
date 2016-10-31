/**
 * 停止服务器运行
 */
import org.ngame.app.*;
import org.ngame.server.connector.*;
import org.ngame.server.*;
import org.ngame.socket.NClient;

List<Application> apps=Application.apps();
for(Application app:apps)
{
  BaseServer server=app.getServer();
  server.stop();
}

println("已经停止服务器");
System.exit(0);