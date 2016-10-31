/**
 * 填充data属性
 */
package org.ngame.data;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.ngame.Spring;
import org.ngame.protocol.util.ClassUtil;
import org.ngame.script.ScriptEngine;
import org.ngame.util.ExceptionUtil;

/**
 *
 * @author beykery
 */
public class DataBuilder
{

  private static final Logger LOG = Logger.getLogger(DataBuilder.class);

  /**
   * 初始化
   */
  static
  {
    Set<Class<?>> cs = ClassUtil.scan("org.ngame.data.init");
    for (Class<?> c : cs)
    {
      DataRepresentation.forClass(c);
    }
  }

  /**
   * 构造
   *
   * @param <T>
   * @param c
   * @return
   */
  public static <T> T build(Class<T> c)
  {
    return build(c, true);
  }

  /**
   * 构造
   *
   * @param <T>
   * @param c
   * @param cache
   * @return
   */
  public static <T> T build(Class<T> c, boolean cache)
  {
    DataRepresentation dr = DataRepresentation.forClass(c);
    if (dr != null)
    {
      File root = dr.getRoot();
      if (root.isFile())
      {
        T data = Spring.bean(ScriptEngine.class).get(c, root, cache);
        return data;
      }
    }
    return null;
  }

  /**
   * 构造
   *
   * @param <T>
   * @param c
   * @param root
   * @param cache
   * @return
   */
  public static <T> List<T> build(Class<T> c, File root, boolean cache)
  {
    List<T> l = new ArrayList<>();
    buildAll(root, c, l, cache);
    return l;
  }

  /**
   * 指定目录
   *
   * @param <T>
   * @param c
   * @param root
   * @return
   */
  public static <T> List<T> build(Class<T> c, File root)
  {
    List<T> l = new ArrayList<>();
    buildAll(root, c, l, true);
    return l;
  }

  /**
   * 构造
   *
   * @param <T>
   * @param data
   * @return
   */
  public static <T> T build(T data)
  {
    return build(data, true);
  }

  /**
   * 构造填充属性
   *
   * @param <T>
   * @param data
   * @param cache
   * @return
   */
  public static <T> T build(T data, boolean cache)
  {
    DataRepresentation dr = DataRepresentation.forClass(data.getClass());
    if (dr != null)
    {
      File root = dr.getRoot();
      File dest = root;
      try
      {
        for (Field f : dr.getPaths())
        {
          Object v = f.get(data);
          dest = new File(dest, v.toString());
        }
        StringBuilder sb = new StringBuilder();
        for (Field f : dr.getNames())
        {
          Object v = f.get(data);
          sb.append(v).append("_");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(".groovy");
        dest = new File(dest, sb.toString());
        data = Spring.bean(ScriptEngine.class).get(data, dest, cache);
        return data;
      } catch (Exception e)
      {
        LOG.error("反射失败：" + ExceptionUtil.stackTrace(e));
      }
    }
    return null;
  }

  /**
   * 构造所有
   *
   * @param <T>
   * @param claz
   * @return
   */
  public static <T> List<T> buildAll(Class<T> claz)
  {
    return buildAll(claz, true);
  }

  /**
   * 构造所有对象
   *
   * @param <T>
   * @param claz
   * @param cache
   * @return
   */
  public static <T> List<T> buildAll(Class<T> claz, boolean cache)
  {
    DataRepresentation dr = DataRepresentation.forClass(claz);
    if (dr != null)
    {
      List<T> l = new ArrayList<>();
      buildAll(dr.getRoot(), claz, l, cache);
      return l;
    }
    return null;
  }

  /**
   * 遍历
   *
   * @param <T>
   * @param claz
   * @param list
   * @return
   */
  private static void buildAll(File root, Class claz, List list, boolean cache)
  {
    File[] fs = root.listFiles();
    for (File item : fs)
    {
      if (item.isFile())
      {
        Object t = Spring.bean(ScriptEngine.class).get(claz, item, cache);
        list.add(t);
      } else
      {
        buildAll(item, claz, list, cache);
      }
    }
  }
}
