/**
 * 测试
 */
package org.ocean.test;

import com.jcabi.ssh.SSHByPassword;
import com.jcabi.ssh.Shell;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.ocean.domain.GameServer;
import org.ocean.service.ServerService;
import org.ocean.Spring;
import org.ocean.dao.BaseDao;
import org.ocean.domain.User;
import java.util.Date;
/**
 *
 * @author beykery
 */
public class Test
{

  public static void main(String[] args) throws UnknownHostException, IOException
  {
    StringBuilder sb = new StringBuilder();
    Set<String> filter = new HashSet<>();
    List<GameServer> gs = Spring.bean(ServerService.class).getServerList(1L, 0, 100000);
    for (GameServer g : gs)
    {
      if (!filter.contains(g.getAddress()))
      {
        filter.add(g.getAddress());
        String add = g.getAddress();
        int index = add.indexOf(':');
        Shell shell = new SSHByPassword(add.substring(0, index), 22, "root", "5t9mhdwEQd3tQg");
        String out = new Shell.Plain(shell).exec("hi .'");
        sb.append(out).append("\n");
      }
    }
    System.out.println(sb);
    // return sb.toString();
    Session s = Spring.bean(BaseDao.class).getSession();
    Transaction tx=s.beginTransaction();
    User u=new User();
    u.setUsercode("jiayoumm888");
    u.setPassmd5("e10adc3949ba59abbe56e057f20f883e");
    u.setChannelId(-1);
    u.setRegTime(new Date());
    s.save(u);
    s.flush();
    tx.commit();
  }
}
