/**
 * shell测试
 */
package org.ngame.server.test;

import com.jcabi.ssh.SSHByPassword;
import com.jcabi.ssh.Shell;
import java.util.concurrent.TimeUnit;
import net.schmizz.keepalive.KeepAliveProvider;
import net.schmizz.sshj.DefaultConfig;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

/**
 *
 * @author beykery
 */
public class ShellTest
{

  public static void main(String[] args)
  {
    //jcabi();
    sshj();
   // scp();
  }

  private static void jcabi()
  {
    String ip = "119.29.153.92";
    int port = 22;
    String user = "root";
    String pwd = "5t9mhdwEQd3tQg";
    try
    {
      StringBuilder sb = new StringBuilder();
      sb.append("cd /data/release1\n");
      sb.append("./start.sh\n");
      Shell shell = new SSHByPassword(ip, port, user, pwd);

      String out = new Shell.Plain(shell).exec(sb.toString());
      //System.in.read();
      System.out.println(out);
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private static void sshj()
  {
    String ip = "119.29.153.92";
    int port = 22;
    String user = "root";
    String pwd = "5t9mhdwEQd3tQg";
    try
    {
      DefaultConfig defaultConfig = new DefaultConfig();
      defaultConfig.setKeepAliveProvider(KeepAliveProvider.KEEP_ALIVE);
      SSHClient ssh = new SSHClient(defaultConfig);
      ssh.addHostKeyVerifier(new PromiscuousVerifier());
      ssh.connect(ip, port);
      ssh.authPassword(user, pwd);
      Session s = ssh.startSession();
      StringBuilder sb = new StringBuilder();
      sb.append("cd /data/release1\n");
//      sb.append("nohup /data/jdk1.8.0_74/bin/java -jar ./interstellar-1.0.jar > /dev/null &\n");
//      sb.append("echo $! > main.pid");
     sb.append("./start.sh");
      Session.Command c = s.exec(sb.toString());
      c.join(3, TimeUnit.SECONDS);
      System.out.println(IOUtils.readFully(c.getInputStream()).toString());

      System.out.println("\n** exit status: " + c.getExitStatus());

      s.close();
      ssh.close();
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public static void scp()
  {
    String ip = "119.29.153.92";
    int port = 22;
    String user = "root";
    String pwd = "5t9mhdwEQd3tQg";
    String src = "./release";
    String dest = "/data/";
    try
    {
      DefaultConfig defaultConfig = new DefaultConfig();
      defaultConfig.setKeepAliveProvider(KeepAliveProvider.KEEP_ALIVE);
      SSHClient ssh = new SSHClient(defaultConfig);
      ssh.addHostKeyVerifier(new PromiscuousVerifier());
      ssh.connect(ip, port);
      ssh.authPassword(user, pwd);
      ssh.newSCPFileTransfer().upload(src, dest);
      System.out.println("ok.uploaded.");
      ssh.close();
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
