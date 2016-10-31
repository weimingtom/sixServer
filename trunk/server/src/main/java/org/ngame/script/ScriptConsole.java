/**
 * 脚本控制台
 */
package org.ngame.script;

import java.io.File;
import java.util.logging.Logger;
import org.ngame.Spring;
import org.ngame.util.Console;
import org.ngame.util.FileUtil;

/**
 * 用来输入和执行脚本
 *
 * @author beykery
 */
public class ScriptConsole
{

  private static final Logger LOG = Logger.getLogger(ScriptConsole.class.getName());

  /**
   * 从consle读入脚本并执行
   */
  public void inputScript()
  {
    ScriptEngine engine = Spring.bean(ScriptEngine.class);
    Console sc = new Console();
    final String path = "./script/";
    while (true)
    {
      try
      {
        System.out.println("\n---请输入待执行脚本路径---\n");
        String s;
        String f = null;
        f = sc.readLine();
        if (f == null || f.length() <= 0)
        {
          continue;
        } else if (!f.endsWith(".groovy"))
        {
          s = path + f + ".groovy";
        } else
        {
          s = f;
        }
        if (!new File(s).exists())
        {
          System.out.println("---找不到该文件，请重新输入-----");
          continue;
        }
        System.out.println("---请输入脚本参数---\n");
        String farg = sc.readLine().trim();
        Object[] fargs = null;
        if (farg != null && farg.length() > 0)
        {
          fargs = FileUtil.format(FileUtil.split(farg, ' '));
        }
        System.out.println("---请输入脚本函数名---\n");
        String m = sc.readLine().trim();
        if (m != null && m.length() > 0)
        {
          System.out.println("---请输入脚本函数参数（空格分隔多个参数）---\n");
          String arg = sc.readLine().trim();
          if (arg == null || arg.length() <= 0)
          {
            engine.executeMethod(new File(s), m, (Object[]) null);
          } else
          {
            Object[] args = FileUtil.format(FileUtil.split(arg, ' '));
            engine.executeMethod(new File(s), m, args);
          }
        } else
        {
          engine.executeFile(new File(s), fargs);
        }
        System.out.println("-------------------脚本执行完毕-------------------");
      } catch (Exception e)
      {
        e.printStackTrace();
        System.out.println("bug");
//				sc.close();
//				sc = new Scanner(System.in, System.getProperty("sun.jnu.encoding"));
        //e.printStackTrace();
        //LOG.log(Level.WARNING, "脚本执行失败");
      }
    }
  }
}
