package org.ngame.script;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import org.codehaus.groovy.control.CompilationFailedException;
import org.ngame.util.FileUtil;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.ngame.util.ClassUtil;
import org.springframework.stereotype.Component;

/**
 * 脚本引擎
 */
@Component
public class ScriptEngine
{

  private static final Logger LOG = Logger.getLogger(ScriptEngine.class);
  private static final GroovyClassLoader gcLoader;
  //缓存编译的类文件
  private static final Map<String, GroovyObject> groovyObjectCache = new ConcurrentHashMap<>();
  private static final Map<String, Long> dateCheck = new ConcurrentHashMap<>();
  private static final Map<String, Boolean> processed = new ConcurrentHashMap<>();
  private static final Map<GroovyObject, Object> objCache = new ConcurrentHashMap<>();
  private static final Map<Class, Field[]> fields = new ConcurrentHashMap<>();

  static
  {
    CompilerConfiguration config = new CompilerConfiguration();
    config.setSourceEncoding("utf-8");
    gcLoader = new GroovyClassLoader(ScriptEngine.class.getClassLoader(), config);
  }

  /**
   * 清空缓存
   */
  public void clearCache()
  {
    groovyObjectCache.clear();
    dateCheck.clear();
    processed.clear();
    objCache.clear();
  }

  /**
   * 清空缓存
   *
   * @param file
   */
  public void clearCache(String file)
  {
    GroovyObject key = groovyObjectCache.remove(file);
    dateCheck.remove(file);
    processed.remove(file);
    if (key != null)
    {
      objCache.remove(key);
    }
  }

  /**
   * 执行脚本
   *
   * @param file
   * @param arg
   * @return
   */
  public Object executeFile(File file, Object... arg)
  {
    try
    {
      GroovyObject go = this.getGroovyObject(file);
      if (arg != null)
      {
        if (arg.length == 1)
        {
          go.setProperty("arg", arg[0]);//设定一个参数
        } else
        {
          go.setProperty("arg", arg);//设定一个参数
        }
      } else
      {
        go.setProperty("arg", arg);
      }
      Object o = go.invokeMethod("run", null);
      processed.put(file.getAbsolutePath(), Boolean.TRUE);
      return o;
    } catch (Exception ex)
    {
      ex.printStackTrace();
      LOG.error("游戏脚本运行异常:" + file.getAbsolutePath());
      throw new RuntimeException(ex.getMessage());
    }
  }

  /**
   * 执行脚本
   *
   * @param file
   * @param method
   * @param arg
   * @return
   */
  public Object executeMethod(File file, String method, Object... arg)
  {
    GroovyObject go = this.getGroovyObject(file);
    if (go != null)
    {
      try
      {
        if (!"run".equals(method))
        {
          if (!processed.get(file.getAbsolutePath()))
          {
            go.invokeMethod("run", null);
            processed.put(file.getAbsolutePath(), Boolean.TRUE);
          }
        }
        if (arg != null && arg.length == 1)
        {
          return go.invokeMethod(method, arg[0]);
        } else
        {
          return go.invokeMethod(method, arg);
        }
      } catch (Exception ex)
      {
        ex.printStackTrace();
        LOG.error("游戏脚本运行异常" + ex.getMessage());
        throw new RuntimeException(ex.getMessage());
      }
    } else
    {
      LOG.error("无法找到脚本文件或脚本文件无法编译：" + file.getAbsolutePath());
      return null;
    }
  }

  /**
   * 获取指定文件的groovy对象
   *
   * @param file
   * @return
   */
  private GroovyObject getGroovyObject(File file)
  {
    GroovyObject go = null;
    try
    {
      //获得缓存的Class
      go = groovyObjectCache.get(file.getAbsolutePath());
      if (go == null)
      {
        Class<?> clazz = gcLoader.parseClass(file);
        go = (GroovyObject) clazz.newInstance();
        gcLoader.clearCache();
        dateCheck.put(file.getAbsolutePath(), file.lastModified());
        groovyObjectCache.put(file.getAbsolutePath(), go);
        processed.put(file.getAbsolutePath(), Boolean.FALSE);
      } else
      {
        long l = dateCheck.get(file.getAbsolutePath());
        if (l != file.lastModified())//文件有改动
        {
          objCache.remove(go);
          gcLoader.clearCache();
          Class<?> clazz = gcLoader.parseClass(FileUtil.getFileContent(file, "utf-8"));
          go = (GroovyObject) clazz.newInstance();
          dateCheck.put(file.getAbsolutePath(), file.lastModified());
          groovyObjectCache.put(file.getAbsolutePath(), go);
          processed.put(file.getAbsolutePath(), Boolean.FALSE);
        }
      }
    } catch (CompilationFailedException | IOException | InstantiationException | IllegalAccessException e)
    {
      LOG.error("无法获取指定文件的groovy对象" + e.getMessage());
    }
    return go;
  }

  /**
   * 获取属性
   *
   * @param file
   * @param p
   * @return
   */
  public Object getProperty(File file, String p)
  {
    Object o = null;
    GroovyObject go = this.getGroovyObject(file);
    if (go != null)
    {
      if (!processed.get(file.getAbsolutePath()))
      {
        go.invokeMethod("run", null);
        processed.put(file.getAbsolutePath(), Boolean.TRUE);
      }
      try
      {
        o = go.getProperty(p);
      } catch (Exception e)
      {
      }
    }
    return o;
  }

  /**
   * 计算属性
   *
   * @param file
   * @param p
   * @param defal
   * @return
   */
  public Object getProperty(File file, String p, Object defal)
  {
    Object o = null;
    GroovyObject go = this.getGroovyObject(file);
    if (go != null)
    {
      if (!processed.get(file.getAbsolutePath()))
      {
        go.invokeMethod("run", null);
        processed.put(file.getAbsolutePath(), Boolean.TRUE);
      }
      try
      {
        o = go.getProperty(p);
      } catch (Exception e)
      {
      }
    }
    return o == null ? defal : o;
  }

  /**
   * 计算属性
   *
   * @param path
   * @param p
   * @return
   */
  public Object getProperty(String path, String p)
  {
    return this.getProperty(new File(path), p);
  }

  /**
   * 计算属性
   *
   * @param path
   * @param p
   * @param defal
   * @return
   */
  public Object getProperty(String path, String p, Object defal)
  {
    return this.getProperty(new File(path), p, defal);
  }

  /**
   * 读取int值
   *
   * @param file
   * @param p
   * @return
   */
  public int getInt(File file, String p)
  {
    Object o = this.getProperty(file, p);
    if (o != null)
    {
      return o instanceof Integer ? (Integer) o : Integer.parseInt(o.toString());
    }
    return 0;
  }

  /**
   * 读取float值
   *
   * @param file
   * @param p
   * @return
   */
  public float getFloat(File file, String p)
  {
    Object o = this.getProperty(file, p);
    if (o != null)
    {
      return o instanceof Float ? (Float) o : Float.parseFloat(o.toString());
    }
    return 0;
  }

  /**
   * 读取int值
   *
   * @param file
   * @param p
   * @param def
   * @return
   */
  public int getInt(File file, String p, int def)
  {
    Object o = this.getProperty(file, p);
    if (o != null)
    {
      return o instanceof Integer ? (Integer) o : Integer.parseInt(o.toString());
    }
    return def;
  }

  /**
   * string
   *
   * @param f
   * @param p
   * @return
   */
  public String getString(File f, String p)
  {
    Object o = this.getProperty(f, p);
    if (o != null)
    {
      return o instanceof String ? (String) o : o.toString();
    }
    return null;
  }

  /**
   * string
   *
   * @param f
   * @param p
   * @param def
   * @return
   */
  public String getString(File f, String p, String def)
  {
    Object o = this.getProperty(f, p);
    if (o != null)
    {
      return o instanceof String ? (String) o : o.toString();
    }
    return def;
  }

  /**
   * 读取float值
   *
   * @param file
   * @param p
   * @param def
   * @return
   */
  public float getFloat(File file, String p, float def)
  {
    Object o = this.getProperty(file, p);
    if (o != null)
    {
      return o instanceof Float ? (Float) o : Float.parseFloat(o.toString());
    }
    return def;
  }

  /**
   * 判断是否为原始数据类型
   *
   * @param src
   * @return
   */
  public boolean isPrimitive(Object src)
  {
    return src instanceof String || src instanceof Integer || src instanceof Float || src instanceof Double || src instanceof Long || src instanceof Boolean;
  }

  /**
   * 是否cache
   *
   * @param <T>
   * @param claz
   * @param file
   * @param cache
   * @return
   */
  public <T> T get(Class<T> claz, File file, boolean cache)
  {
    T o = null;
    GroovyObject go = this.getGroovyObject(file);
    if (go != null)
    {
      if (!processed.get(file.getAbsolutePath()))
      {
        go.invokeMethod("run", null);
        processed.put(file.getAbsolutePath(), Boolean.TRUE);
      }
      if (cache)
      {
        o = (T) objCache.get(go);
        if (o != null && o.getClass() == claz)
        {
          return o;
        }
      }
      try
      {
        o = claz.newInstance();
      } catch (Exception e)
      {
        LOG.error("初始化" + claz.getName() + "失败");
        return null;
      }
      Field[] fs = fields(claz);
      for (Field f : fs)
      {
        try
        {
          Object v = go.getProperty(f.getName());
          if (v != null)
          {
            f.setAccessible(true);
            f.set(o, v);
          }
        } catch (Exception e)
        {
          LOG.info(file.getAbsolutePath() + "中不存在属性：" + f.getName());
        }
      }
      objCache.put(go, o);
    }
    return o;
  }

  /**
   * 组装
   *
   * @param <T>
   * @param t
   * @param file
   * @param cache
   * @return
   */
  public <T> T get(T t, File file, boolean cache)
  {
    T o = null;
    GroovyObject go = this.getGroovyObject(file);
    if (go != null)
    {
      if (!processed.get(file.getAbsolutePath()))
      {
        go.invokeMethod("run", null);
        processed.put(file.getAbsolutePath(), Boolean.TRUE);
      }
      if (cache)
      {
        o = (T) objCache.get(go);
        if (o != null)
        {
          return o;
        }
      }
      o = t;
      Field[] fs = fields(o.getClass());
      for (Field f : fs)
      {
        try
        {
          Object v = go.getProperty(f.getName());
          if (v != null)
          {
            f.setAccessible(true);
            f.set(o, v);
          }
        } catch (Exception e)
        {
//          LOG.info(file.getAbsolutePath() + "中不存在属性：" + f.getName());
        }
      }
      objCache.put(go, o);
    }
    return o;
  }

  /**
   * 填充
   *
   * @param <T>
   * @param claz
   * @param file
   * @return
   */
  public <T> T get(Class<T> claz, File file)
  {
    return get(claz, file, false);
  }

  /**
   * 填充
   *
   * @param <T>
   * @param claz
   * @param file
   * @param cache
   * @param ex
   * @return
   */
  public <T> T get(Class<T> claz, File file, boolean cache, String... ex)
  {
    T o = null;
    GroovyObject go = this.getGroovyObject(file);
    if (go != null)
    {
      if (!processed.get(file.getAbsolutePath()))
      {
        go.invokeMethod("run", null);
        processed.put(file.getAbsolutePath(), Boolean.TRUE);
      }
      if (cache)
      {
        o = (T) objCache.get(go);
        if (o != null && o.getClass() == claz)
        {
          return o;
        }
      }
      Set<String> excludes = null;
      if (ex != null)
      {
        excludes = new HashSet<>();
        excludes.addAll(Arrays.asList(ex));
      }
      try
      {
        o = claz.newInstance();
      } catch (Exception e)
      {
        LOG.error("初始化" + claz.getName() + "失败");
        return null;
      }
      Field[] fs = fields(claz);
      for (Field f : fs)
      {
        if (excludes == null || !excludes.contains(f.getName()))
        {
          try
          {
            Object v = go.getProperty(f.getName());
            if (v != null)
            {
              f.setAccessible(true);
              f.set(o, v);
            }
          } catch (Exception e)
          {
            LOG.debug(file.getAbsolutePath() + "中不存在属性：" + f.getName());
          }
        }
      }
      objCache.put(go, o);
    }
    return o;
  }

  /**
   * 计算所有的field
   *
   * @param <T>
   * @param claz
   * @return
   */
  private static <T> Field[] fields(Class<T> claz)
  {
    Field[] fs = fields.get(claz);
    if (fs == null)
    {
      fs = ClassUtil.allField(claz);
      fields.put(claz, fs);
    }
    return fs;
  }
}
