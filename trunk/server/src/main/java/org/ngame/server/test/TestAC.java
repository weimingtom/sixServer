/**
 * ac算法测试
 */
package org.ngame.server.test;

import com.jcabi.ssh.SSHByPassword;
import com.jcabi.ssh.Shell;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 *
 * @author beykery
 */
public class TestAC
{

  public static void main(String[] args) throws UnknownHostException, IOException
  {
   
    Shell shell = new SSHByPassword("119.29.153.92", 22, "root", "5t9mhdwEQd3tQg");
    String out = new Shell.Plain(shell).exec("echo 'hi .'");
    System.out.println(out);
  }
}
