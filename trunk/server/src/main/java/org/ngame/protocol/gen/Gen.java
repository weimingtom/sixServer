/**
 * 生成协议文档
 */
package org.ngame.protocol.gen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.ngame.protocol.annotation.Proto;
import org.ngame.protocol.util.ClassUtil;
import org.ngame.protocol.util.FileUtil;
import org.ngame.protocol.util.IDLGen;

/**
 *
 * @author beykery
 */
public class Gen
{

  private static final String summary = "        /// <summary>\n"
          + "        /// %s\n"
          + "        /// </summary>";
  private static final String summary1 = "    /// <summary>\n"
          + "    /// %s\n"
          + "    /// </summary>";
  private static final String enumDesc = "[Description(\"%s\")]";

  /**
   * 扫描指定包下所有协议，生成协议文件
   *
   * @param args
   * @throws java.io.FileNotFoundException
   */
  public static void main(String[] args) throws FileNotFoundException, IOException, Exception
  {
    String proto = genProto();
    genNet(proto);
  }

  /**
   * 生成proto文件
   *
   * @return
   * @throws java.io.IOException
   */
  public static String genProto() throws IOException
  {
    StringBuilder sb = new StringBuilder("package protocol;\n\n");
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
    for (Class c : l)
    {
      Proto p = (Proto) c.getAnnotation(Proto.class);
      sb.append("//协议:").append(p.value()).append(",").append(p.description()).append("\n");
      sb.append(IDLGen.idl(c)).append("\n");
    }
    System.out.println(sb);
    File f = new File("./protocol.proto");
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
   * 生成c#代码
   *
   * @param proto
   * @throws java.io.IOException
   */
  public static void genNet(String proto) throws IOException, Exception
  {
    File dest = new File("./net/Protocols/");//目标文件夹
    if (dest.getParentFile().exists())
    {
      FileUtil.delete(dest.getParentFile());
    }
    StringBuilder sb;
    StringBuilder one = null;
    String claz = null;
    int cmd = 0;
    String desc = null;
    String[] lines = split(proto, '\n');
    boolean used = false;
    sb = new StringBuilder();
    sb.append("using ProtoBuf;\n");
    sb.append("using System.Collections.Generic;\n");
    sb.append("using System;\n\n");
    sb.append("namespace protocol\n{\n");
    writeOneNetClassHeader(sb, dest);
    String line = null;
    int index = 0;
    while (index < lines.length)
    {
      line = lines[index++].trim();
      if (line.isEmpty())
      {
        continue;
      }
      String[] token = split(line, ' ');
      if (token[0].startsWith("//协议:"))//请求协议
      {
        int i = line.indexOf(',');
        cmd = Integer.parseInt(line.substring(5, i));//请求的协议号
        desc = line.substring(i + 1);
        used = false;
      } else if (token[0].startsWith("message"))//协议体的开始
      {
        claz = token[1];
        sb = new StringBuilder();
        sb.append("using ProtoBuf;\n");
        sb.append("using System.Collections.Generic;\n");
        sb.append("using System;\n\n");
        sb.append("namespace protocol\n{\n");

        one = new StringBuilder();//初始化单个net class
        if (!used)
        {
          sb.append(String.format(summary1, desc + " 协议:" + cmd)).append("\n");
          one.append(String.format(summary1, desc + " 协议:" + cmd)).append("\n");
          sb.append("\t[Proto(value=").append(cmd).append(",description=\"").append(desc).append("\")]\n");
          one.append("\t[Proto(value=").append(cmd).append(",description=\"").append(desc).append("\")]\n");
        }
        sb.append("\t[ProtoContract]\n");
        sb.append("\tpublic class ").append(claz);
        one.append("\t[ProtoContract]\n");
        one.append("\tpublic class ").append(claz);
        if (!used)
        {
          used = true;
        }
        sb.append("\n\t{\n");
        one.append("\n\t{\n");
      } else if (token[0].startsWith("enum"))//枚举
      {
        claz = token[1];
        sb = new StringBuilder();
        sb.append("using ProtoBuf;\n");
        sb.append("using System.Collections.Generic;\n");
        sb.append("using System.Runtime.Serialization;\n");
        sb.append("using System.ComponentModel;\n");
        sb.append("using System;\n\n");
        sb.append("namespace protocol\n{\n");
        sb.append("\t[ProtoContract]\n");
        sb.append("\tpublic enum ").append(claz);
        one.append("\t[ProtoContract]\n");
        one.append("\tpublic enum ").append(claz);
        sb.append("\n\t{\n");
        one.append("\n\t{\n");
      } else if (token[0].startsWith("}"))//结束标记
      {
        sb.append("\n\t}\n}");
        one.append("\n\t}\n\n");
        writeNetClass(sb, claz, dest);
        appendOneNetClass(one, dest);
      } else if (token[0].startsWith("optional"))//可选
      {
        String type = toNetType(token[1], false);//类型
        String name = varName(token[2]);//变量名
        int i = varIndex(token[2]);//顺序
        if (token.length >= 5 && token[3].equals("//"))//带注释
        {
          sb.append(String.format(summary, line.substring(line.indexOf("//") + 2))).append("\n");
        }
        sb.append("\t\t").append("[ProtoMember(").append(i).append(", IsRequired = false)]\n");
        sb.append("\t\t").append("public ").append(type).append(" ").append(name).append(";");

        if (token.length >= 5 && token[3].equals("//"))//带注释
        {
          one.append(String.format(summary, line.substring(line.indexOf("//") + 2))).append("\n");
        }
        one.append("\t\t").append("[ProtoMember(").append(i).append(", IsRequired = false)]\n");
        one.append("\t\t").append("public ").append(type).append(" ").append(name).append(";");
        sb.append("\n");
        one.append("\n");
      } else if (token[0].startsWith("required"))//必选
      {
        String type = toNetType(token[1], false);//类型
        String name = varName(token[2]);//变量名
        int i = varIndex(token[2]);//顺序
        if (token.length >= 5 && token[3].equals("//"))//带注释
        {
          sb.append(String.format(summary, line.substring(line.indexOf("//") + 2))).append("\n");
        }
        sb.append("\t\t").append("[ProtoMember(").append(i).append(", IsRequired = true)]\n");
        sb.append("\t\t").append("public ").append(type).append(" ").append(name).append(";");

        if (token.length >= 5 && token[3].equals("//"))//带注释
        {
          one.append(String.format(summary, line.substring(line.indexOf("//") + 2))).append("\n");
        }
        one.append("\t\t").append("[ProtoMember(").append(i).append(", IsRequired = true)]\n");
        one.append("\t\t").append("public ").append(type).append(" ").append(name).append(";");
        sb.append("\n");
        one.append("\n");
      } else if (token[0].startsWith("repeated"))//必选
      {
        String type = toNetType(token[1], true);//类型
        String name = varName(token[2]);//变量名
        int i = varIndex(token[2]);//顺序
        if (token.length >= 5 && token[3].equals("//"))//带注释
        {
          sb.append(String.format(summary, line.substring(line.indexOf("//") + 2))).append("\n");
        }
        sb.append("\t\t").append("[ProtoMember(").append(i).append(", IsRequired = false)]\n");

        sb.append("\t\t").append("public ").append(type).append(" ").append(name).append(";");
        if (token.length >= 5 && token[3].equals("//"))//带注释
        {
          one.append(String.format(summary, line.substring(line.indexOf("//") + 2))).append("\n");
        }
        one.append("\t\t").append("[ProtoMember(").append(i).append(", IsRequired = false)]\n");
        one.append("\t\t").append("public ").append(type).append(" ").append(name).append(";");
        sb.append("\n");
        one.append("\n");
      } else
      {
        String temp = line.substring(line.indexOf("//") + 2);
        if (token.length >= 3 && token[1].equals("//"))
        {
          sb.append(String.format(summary, temp)).append("\n");
          one.append(String.format(summary, temp)).append("\n");
        }
        sb.append("\t\t").append("[EnumMember]").append("\n");
        sb.append("\t\t").append("[ProtoEnum]").append("\n");
        sb.append("\t\t").append(String.format(enumDesc, temp.trim())).append("\n");
        sb.append("\t\t").append(token[0].substring(0, token[0].length() - 1));
        if (lines[index].length() > 4)//下一行还有
        {
          sb.append(",");
        }
        sb.append("\n");
      }
    }
    appendEndOneNetClass("\n}", dest);
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
   * 空
   *
   * @param item
   * @return
   */
  private static boolean isEmpty(String item)
  {
    return item == null || item.isEmpty();
  }

  /**
   * 写入一个c#类
   *
   * @param sb
   * @param claz
   * @param dest
   */
  private static void writeNetClass(StringBuilder sb, String claz, File dest) throws FileNotFoundException, IOException
  {
    File d = new File(dest, claz + ".cs");
    if (!d.exists())
    {
      d.getParentFile().mkdirs();
      d.createNewFile();
    }
    try (FileOutputStream fos = new FileOutputStream(d))
    {
      fos.write(sb.toString().getBytes("utf-8"));
      fos.flush();
    }
  }

  /**
   * 变量名
   *
   * @param token
   * @return
   */
  private static String varName(String token)
  {
    String[] ss = split(token, '=');
    return ss[0];
  }

  /**
   * 变量顺序
   *
   * @param token
   * @return
   */
  private static int varIndex(String token)
  {
    String[] ss = split(token, '=');
    return Integer.parseInt(ss[1].substring(0, ss[1].length() - 1));
  }

  /**
   * 转换成c#类型
   *
   * @param token
   * @return
   */
  private static String toNetType(String token, boolean repeated)
  {
    String t;
    switch (token)
    {
      case "string":
        t = "string";
        break;
      case "int32":
        t = "int";
        break;
      case "bool":
        t = "bool";
        break;
      case "int64":
        t = "long";
        break;
      case "float":
        t = "float";
        break;
      case "double":
        t = "double";
        break;
      case "bytes":
        t = "byte[]";
        break;
      default:
        t = token;
        break;
    }
    if (repeated)
    {
      return "List<" + t + ">";
    }
    return t;
  }

  /**
   * 写入netclass文件的头
   *
   * @param sb
   * @param dest
   */
  private static void writeOneNetClassHeader(StringBuilder sb, File dest) throws IOException
  {
    File d = new File(dest, "Protocol.cs");
    if (!d.exists())
    {
      d.getParentFile().mkdirs();
      d.createNewFile();
    }
    try (FileOutputStream fos = new FileOutputStream(d))
    {
      fos.write(sb.toString().getBytes("utf-8"));
      fos.flush();
    }
  }

  /**
   * 填写一个net类
   *
   * @param one
   * @param dest
   */
  private static void appendOneNetClass(StringBuilder sb, File dest) throws IOException
  {
    File d = new File(dest.getParentFile(), "Protocol.cs");
    try (FileOutputStream fos = new FileOutputStream(d, true))
    {
      fos.write(sb.toString().getBytes("utf-8"));
      fos.flush();
    }
  }

  /**
   * 写入结束符
   *
   * @param string
   * @param dest
   */
  private static void appendEndOneNetClass(String end, File dest) throws Exception
  {
    File d = new File(dest, "Protocol.cs");
    try (FileOutputStream fos = new FileOutputStream(d, true))
    {
      fos.write(end.getBytes("utf-8"));
      fos.flush();
    }
  }

  /**
   * 生成idl
   *
   * @param c
   * @return
   */
//  private static String[] getIDL(Class c)
//  {
//    String idl = IDLGen.getIDL(c);
//    
//  }
}
