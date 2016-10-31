/**
 * 文件工具
 */
package org.ngame.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author beykery
 */
public class FileUtil
{

  private static final Logger LOG = Logger.getLogger(FileUtil.class.getName());

  /**
   * 获取文件内容
   *
   * @param file
   * @return
   */
  public static String getFileContent(File file)
  {
    InputStream is = null;
    if (file.exists())
    {
      try
      {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        is = new FileInputStream(file);
        int n;
        while ((n = is.read(buffer)) > 0)
        {
          baos.write(buffer, 0, n);
        }
        return baos.toString();
      } catch (Exception e)
      {
        LOG.log(Level.SEVERE, "读取文件内容失败");
      } finally
      {
        if (is != null)
        {
          try
          {
            is.close();
          } catch (IOException ex)
          {
          }
        }
      }
    }
    return null;
  }

  /**
   * 获取文件内容
   *
   * @param file
   * @param encoding
   * @return
   */
  public static String getFileContent(File file, String encoding)
  {
    InputStream is = null;
    if (file.exists())
    {
      try
      {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        is = new FileInputStream(file);
        int n;
        while ((n = is.read(buffer)) > 0)
        {
          baos.write(buffer, 0, n);
        }
        return baos.toString(encoding);
      } catch (Exception e)
      {
        LOG.log(Level.SEVERE, "读取文件内容失败");
      } finally
      {
        if (is != null)
        {
          try
          {
            is.close();
          } catch (IOException ex)
          {
          }
        }
      }
    }
    return null;
  }

  /**
   * 获取文件内容，分行
   *
   * @param file
   * @return
   */
  public static String[] getLines(File file)
  {
    BufferedReader br = null;
    try
    {
      br = new BufferedReader(new FileReader(file));
      List<String> lines = new ArrayList<>();
      String line;
      while ((line = br.readLine()) != null)
      {
        lines.add(line);
      }
      String[] r = new String[lines.size()];
      for (int i = 0; i < r.length; i++)
      {
        r[i] = lines.get(i);
      }
      return r;
    } catch (Exception e)
    {
      LOG.log(Level.SEVERE, "读取文件内容失败");
    } finally
    {
      if (br != null)
      {
        try
        {
          br.close();
        } catch (IOException ex)
        {
        }
      }
    }
    return null;
  }

  /**
   * 按照指定的分割符分隔为json数组
   *
   * @param s
   * @param c
   * @return
   */
  public static String toJSONArray(String s, char c)
  {
    String[] r = split(s, c);
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (int i = 0; i < r.length; i++)
    {
      r[i] = r[i].replaceAll("\"", "\\\\\"");
      sb.append("\"" + r[i] + "\"");
      if (i < r.length - 1)
      {
        sb.append(",");
      }
    }
    sb.append("]");
    return sb.toString();
  }

  /**
   * 写入文件
   *
   * @param file
   * @param c
   */
  public static void write(File file, String[] c)
  {
    file.delete();
    file.getParentFile().mkdirs();
    try (FileWriter fw = new FileWriter(file))
    {
      for (String item : c)
      {
        fw.write(item);
        fw.write("\n");
      }
      fw.flush();
      fw.close();
    } catch (Exception e)
    {
      LOG.log(Level.SEVERE, "写入文件失败");
    }
  }

  /**
   * 续写文件
   *
   * @param file
   * @param s
   * @return
   */
  public static boolean append(File file, String s)
  {
    FileOutputStream fos = null;
    try
    {
      fos = new FileOutputStream(file, true);
      fos.write(s.getBytes("UTF-8"));
      fos.flush();
    } catch (Exception e)
    {
      return false;
    } finally
    {
      if (fos != null)
      {
        try
        {
          fos.close();
        } catch (IOException ex)
        {
        }
      }
    }
    return true;
  }

  /**
   * 分割
   *
   * @param s
   * @param c
   * @return
   */
  public static String[] split(String s, char c)
  {
    s = s.trim();
    List<String> r = new ArrayList<>();
    int i = 0, j;
    while (i < s.length())
    {
      j = s.indexOf(c, i);
      if (j > 0)
      {
        String temp = s.substring(i, j).trim();
        if (temp.length() > 0)
        {
          r.add(temp);
        }
        i = j + 1;
      } else
      {
        break;
      }
    }
    if (i > 0 && i < s.length())
    {
      String temp = s.substring(i).trim();
      if (temp.length() > 0)
      {
        r.add(s.substring(i));
      }
    } else
    {
      r.add(s.trim());
    }
    String[] ss = new String[r.size()];
    for (i = 0; i < ss.length; i++)
    {
      ss[i] = r.get(i);
    }
    return ss;
  }

  /**
   * 写入
   *
   * @param file
   * @param s
   * @return
   */
  public static boolean write(File file, String s)
  {
    file.getParentFile().mkdirs();
    FileOutputStream fos = null;
    try
    {
      fos = new FileOutputStream(file);
      fos.write(s.getBytes("UTF-8"));
      fos.flush();
    } catch (Exception e)
    {
      return false;
    } finally
    {
      if (fos != null)
      {
        try
        {
          fos.close();
        } catch (IOException ex)
        {
        }
      }
    }
    return true;
  }

  /**
   * 格式化为字符串、boolean、整数格式
   *
   * @param split
   * @return
   */
  public static Object[] format(String[] split)
  {
    Object[] os = new Object[split.length];
    for (int i = 0; i < os.length; i++)
    {
      try
      {
        Integer v = Integer.parseInt(split[i]);
        os[i] = v;
        continue;
      } catch (Exception e)
      {
      }
      if (split[i].equalsIgnoreCase("true"))
      {
        os[i] = Boolean.TRUE;
        continue;
      }
      if (split[i].equalsIgnoreCase("false"))
      {
        os[i] = Boolean.FALSE;
        continue;
      }
      os[i] = split[i];
    }
    return os;
  }

  /**
   * 获取输入流的内容
   *
   * @param in
   * @return
   */
  public static String getContent(InputStream in)
  {
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    StringBuilder sb = new StringBuilder();
    String line;
    try
    {
      while ((line = br.readLine()) != null)
      {
        sb.append(line);
      }
    } catch (Exception e)
    {
      LOG.log(Level.WARNING, "无法读取流中的数据", e);
    }
    return sb.toString();
  }

  /**
   * 最近一次的修改时间
   *
   * @param f
   * @return
   */
  public static long lastModified(File f)
  {
    return f.lastModified();
  }

  private FileUtil()
  {
  }
}
