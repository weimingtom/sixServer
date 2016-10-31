/**
 * 生成协议文档
 */
package org.ngame.protocol.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ngame.protocol.annotation.Proto;
import org.ngame.protocol.util.ClassUtil;
import org.ngame.protocol.util.FileUtil;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.utils.FieldUtils;

/**
 *
 * @author beykery
 */
public class Gen2
{

  private static final String summary = "        /// <summary>\n"
          + "        /// %s\n"
          + "        /// </summary>";
  private static final String summary1 = "    /// <summary>\n"
          + "    /// %s\n"
          + "    /// </summary>";
  private static final String enumDesc = "[Description(\"%s\")]";

  
  private static final Set<Class<?>> cachedTypes = new HashSet<>();
  private static final Set<Class<?>> cachedEnumTypes = new HashSet<>();
  /**
   * 扫描指定包下所有协议，生成协议文件
   *
   * @param args
   * @throws java.io.FileNotFoundException
   */
  public static void main(String[] args) throws FileNotFoundException, IOException, Exception
  {
	  genProto();
  }

  /**
   * 生成proto文件
   *
   * @return
   * @throws java.io.IOException
   */
  public static String genProto() throws IOException
  {
    StringBuilder sb = new StringBuilder();
    Set<Class<?>> cs = ClassUtil.scan("org.ngame.protocol");
    List<Class<?>> l = new ArrayList<>();
    Set<Integer> set = new HashSet<>();
    for (Class c : cs)
    {
      Proto p = (Proto) c.getAnnotation(Proto.class);
      if (p != null)
      {
        l.add(c);
        boolean r = set.add(p.value());
        if (!r)
        {
          throw new RuntimeException("重复的协议号: " + p.value() + " " + c.getName());
        }
      }
    }
    Collections.sort(l, new Comparator<Class<?>>()
    {

      @Override
      public int compare(Class<?> o1, Class<?> o2)
      {
        Proto p1 = o1.getAnnotation(Proto.class);
        Proto p2 = o2.getAnnotation(Proto.class);
        return Math.abs(p1.value()) - Math.abs(p2.value());
      }
    });
    sb.append("--公共的常用定义(该文件为客户端提供支持)\nmodule(...,package.seeall)\n");
    sb.append("----------------------------协议ID-----------------------\n");
    sb.append("protocolInfos={}\n");
    for (Class c : l)
    {
      sb.append(idl(c)).append("\n");
    }
    System.out.println(sb);
    File f = new File("./agreement.lua");
    if (!f.exists())
    {
      f.createNewFile();
    }
    try (FileOutputStream fos = new FileOutputStream(f))
    {
      fos.write(sb.toString().getBytes("utf-8"));
      fos.flush();
    }
    return sb.toString();
  }
  
  
  
  /**
   * 生成
   *
   * @param cls
   * @return
   */
  public static String idl(Class<?> cls)
  {
    //cachedTypes.add(cls);
    return getIDL(cls, cachedTypes, cachedEnumTypes, true);
  }

  public static String getIDL(final Class<?> cls, final Set<Class<?>> cachedTypes, final Set<Class<?>> cachedEnumTypes, boolean ignoreJava)
  {
    Set<Class<?>> types = cachedTypes;
    if (types == null)
    {
      types = new HashSet<>();
    }
    Set<Class<?>> enumTypes = cachedEnumTypes;
    if (enumTypes == null)
    {
      enumTypes = new HashSet<>();
    }
    if (types.contains(cls))
    {
      return null;
    }
    StringBuilder code = new StringBuilder();
    if (!ignoreJava)
    {
      // define package
      code.append("package ").append(cls.getPackage().getName()).append(";\n");
      code.append("option java_outer_classname = \"").append(cls.getSimpleName()).append("$$ByJProtobuf\";\n");
    }
    
    //TODO ..猴哥测试协议生成
    generateIDL(code, cls, cachedTypes, cachedEnumTypes);
    return code.toString();
  }	

  public static void generateIDL(StringBuilder code, Class<?> cls, Set<Class<?>> cachedTypes, Set<Class<?>> cachedEnumTypes)
  {
    List<Field> fields = FieldUtils.findMatchedFields(cls, Protobuf.class);
    Set<Class<?>> subTypes = new HashSet<>();
    Set<Class<Enum>> enumTypes = new HashSet<>();
    Proto p=cls.getAnnotation(Proto.class);
    if(p!=null){
    	 code.append(cls.getSimpleName()).append("Id = "+p.value()+"\n");
    	 code.append("protocolInfos["+p.value()+"] = "+"{strings.TestView.protoPath,\"protocol."+cls.getSimpleName()+"\"}");
    if (subTypes.isEmpty())
    {
      return;
    }
    for (Class<?> subType : subTypes)
    {
      generateIDL(code, subType, cachedTypes, cachedEnumTypes);
    }
  }
}
}
