/**
 * 文件操作
 */
package org.ngame.protocol.util;

import java.io.File;

/**
 *
 * @author beykery
 */
public class FileUtil
{

  /**
   * 删除
   *
   * @param f
   */
  public static void delete(File f)
  {
    delete0(f);
    f.delete();
  }

  /**
   * 删除
   *
   * @param f
   */
  private static void delete0(File f)
  {
    if (f.isFile())
    {
      f.delete();
    } else
    {
      File[] fs = f.listFiles();
      for (File item : fs)
      {
        delete(item);
      }
    }
  }
}
