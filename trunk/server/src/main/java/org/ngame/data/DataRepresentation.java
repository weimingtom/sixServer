/**
 * data类的描述
 */
package org.ngame.data;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ngame.annotation.*;

/**
 *
 * @author beykery
 */
public class DataRepresentation
{

  private static final Map<Class<? extends Object>, DataRepresentation> datas;

  static
  {
    datas = new HashMap<>();
  }
  /**
   * 路径属性
   */
  private final List<Field> paths;
  /**
   * 文件名属性
   */
  private final List<Field> names;
  /**
   * 根目录
   */
  private File root;

  /**
   * 构造
   */
  public DataRepresentation()
  {
    paths = new ArrayList<>(3);
    names = new ArrayList<>(3);
  }

  /**
   *
   * @param claz
   * @return
   */
  public static DataRepresentation forClass(Class<? extends Object> claz)
  {
    DataRepresentation dr = datas.get(claz);
    if (dr != null)
    {
      return dr;
    }
    if (claz.isAnnotationPresent(Root.class))
    {
      Root r = claz.getAnnotation(Root.class);
      dr = new DataRepresentation();
      dr.root = new File(r.value());
      Field[] fs = claz.getDeclaredFields();
      for (Field f : fs)
      {
        f.setAccessible(true);
        if (f.isAnnotationPresent(Path.class))
        {
          dr.paths.add(f);
        } else if (f.isAnnotationPresent(Name.class))
        {
          dr.names.add(f);
        }
      }
      //排序
      if (!dr.paths.isEmpty())
      {
        Field[] ps = new Field[dr.paths.size()];
        dr.paths.toArray(ps);
        Arrays.sort(ps, new Comparator<Field>()
        {
          @Override
          public int compare(Field o1, Field o2)
          {
            Path p1 = o1.getAnnotation(Path.class);
            Path p2 = o2.getAnnotation(Path.class);
            return p1.value() - p2.value();
          }
        });
        dr.paths.clear();
        dr.paths.addAll(Arrays.asList(ps));
      }
      if (!dr.names.isEmpty())
      {
        Field[] ps = new Field[dr.names.size()];
        dr.names.toArray(ps);
        Arrays.sort(ps, new Comparator<Field>()
        {
          @Override
          public int compare(Field o1, Field o2)
          {
            Name n1 = o1.getAnnotation(Name.class);
            Name n2 = o2.getAnnotation(Name.class);
            return n1.value() - n2.value();
          }
        });
        dr.names.clear();
        dr.names.addAll(Arrays.asList(ps));
      }
      datas.put(claz, dr);
      return dr;
    }
    return null;
  }

  public File getRoot()
  {
    return root;
  }

  public List<Field> getPaths()
  {
    return paths;
  }

  public List<Field> getNames()
  {
    return names;
  }

}
