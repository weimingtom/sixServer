/**
 * 操作第三方调用
 */
package org.ngame.service;

import java.io.File;
import java.nio.file.Files;
import org.apache.log4j.Logger;
import org.ngame.script.ScriptEngine;
import org.ngame.util.ExceptionUtil;
import org.ngame.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author beykery
 */
@Component
public class ScriptService
{

  private static final Logger LOG = Logger.getLogger(ScriptService.class);
  @Autowired
  private ScriptEngine engine;

  /**
   * 删除脚本
   *
   * @param file
   * @return
   */
  public String deleteScript(String file)
  {
    boolean r;
    File f = new File(file);
    if (f.exists())
    {
      delete(f);
    }
    r = !f.exists();
    LOG.error("删除脚本文件 " + f + " " + r);
    return r ? null : "无法删除指定脚本";
  }

  /**
   * 删除文件或文件夹
   *
   * @param f
   * @return
   */
  private boolean delete(File f)
  {
    if (f.isFile())
    {
      try
      {
        Files.deleteIfExists(f.toPath());
        LOG.error("脚本文件 " + f + " 已被远程删除");
      } catch (Exception e)
      {
        LOG.error("无法删除文件：" + f);
        return false;
      }
      return true;
    } else
    {
      File[] fs = f.listFiles();
      for (File item : fs)
      {
        delete(item);
      }
      return f.delete();
    }
  }

  /**
   * 更新脚本文件
   *
   * @param file
   * @param content
   * @return
   */
  public String pushScript(String file, String content)
  {
    File f = new File(file);
    return FileUtil.write(f, content) ? null : "无法写入脚本文件:" + file;
  }

  /**
   * 执行脚本
   *
   * @param file
   * @param content
   * @return
   */
  public Object exeScript(String file, String content)
  {
    File f = new File(file);
    if (FileUtil.write(f, content))
    {
      try
      {
        return engine.executeFile(f);
      } catch (Exception e)
      {
        LOG.error("执行失败", e);
        return ExceptionUtil.stackTrace(e);
      }
    }
    return null;
  }
}
