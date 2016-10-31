/**
 * 跟excel表对应的数据，每行生成一个脚本文件
 */
package org.ngame.data;

import com.alibaba.fastjson.JSONArray;
import io.netty.util.internal.StringUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.ngame.util.ExcelUtil;
import org.ngame.util.FileUtil;

/**
 *
 * @author beykery
 */
public class Data
{

  private final File excel;
  private final int sheet;
  private final String[] kf;//主key
  private final String pkg;

  /**
   * 数据转换
   *
   * @param args
   */
  public static void main(String[] args)
  {

//    new Data(new File("./data/partner.xlsx"), 0, "partner", "id").parse();
//    new Data(new File("./data/suit.xlsx"), 0, "suit", "id").parse();
//    new Data(new File("./data/zhenyuanKong.xlsx"), 0, "zhenyuanKong", "id").parse();
//    new Data(new File("./data/zhenyuanTotalLevel.xlsx"), 0, "zhenyuanTotalLevel", "id").parse();
//    new Data(new File("./data/hero.xlsx"), 0, "hero", "id").parse();
//    new Data(new File("./data/equip.xlsx"), 0, "equip", "id").parse();
//    new Data(new File("./data/exp.xlsx"), 0, "level", "level").parse();
//    new Data(new File("./data/vip.xlsx"), 0, "vip", "level").parse();
//    new Data(new File("./data/equipHole.xlsx"), 0, "equipHole", "holeType", "level").parse();
//    new Data(new File("./data/equipHoleLevel.xlsx"), 0, "equipHoleLevel", "level").parse();

//    for (int i = 0; i < 14; i++)
//    {
//      new Data(new File("./data/item.xlsx"), i, "item", "id").parse();
//    }
//    new Data(new File("./data/randomGroup.xlsx"), 0, "randomGroup", "id").parse();//随机组
//    new Data(new File("./data/section.xlsx"), 0, "section", "id").parse();//章节
//    new Data(new File("./data/fb.xlsx"), 0, "fb", "id").parse("section", "type");//副本
//    new Data(new File("./data/partnerLevel.xlsx"), 0, "partnerLevel", "level").parse();//伙伴升级经验配置
//    new Data(new File("./data/partnerStar.xlsx"), 0, "partnerStar", "star").parse();//伙伴升级经验配置
//    new Data(new File("./data/partnerSkill.xlsx"), 0, "partnerSkill", "id").parse("pInitId");//伙伴技能
//    new Data(new File("./data/partnerAdvance.xlsx"), 0, "partnerAdvance", "qulity").parse();//伙伴进阶
	  new Data(new File("./data/base.xlsx"), 0, "base", "id").parse();
	  new Data(new File("./data/buff.xlsx"), 0, "buff", "id").parse();
	  new Data(new File("./data/skill.xlsx"), 0, "skill", "id").parse();
	  new Data(new File("./data/card.xlsx"), 0, "card", "id").parse();
    System.out.println("succeed");
  }

  /**
   * 构造
   *
   * @param excel 表格
   * @param sheet 页面
   * @param pkg 目标包
   * @param field 脚本文件名的组成字段
   */
  public Data(File excel, int sheet, String pkg, String... field)
  {
    this.excel = excel;
    this.sheet = sheet;
    this.kf = field;
    this.pkg = pkg;
  }

  /**
   * 解析
   */
  public void parse()
  {
    List<Map<String, String>> content = ExcelUtil.excelMap(excel, sheet, 1);
    String[][] temp = ExcelUtil.readExcel(excel, sheet, 0, 2, content.get(0).size());
    String[] fd = temp[0];//字段说明
    String[] title = temp[1];//字段
    List<Map<String, String>> lines = new ArrayList<>();//行内容
    Map<String, String> types = new LinkedHashMap<>();//字段类型
    Map<String, Boolean> excludes = new LinkedHashMap<>();//是否被排除
    for (Map<String, String> line : content)
    {
      Map<String, String> item = new LinkedHashMap<>();
      lines.add(item);
      for (Map.Entry<String, String> en : line.entrySet())
      {
        String key = en.getKey();
        String value = en.getValue();
        int index = key.indexOf(':');
        item.put(key.substring(0, index), value);
        if (key.endsWith("exclude"))
        {
          key = key.substring(0, key.lastIndexOf(':'));
          excludes.put(key.substring(0, index), Boolean.TRUE);
        } else
        {
          excludes.put(key.substring(0, index), Boolean.FALSE);
        }
        types.put(key.substring(0, index), key.substring(index + 1));
      }
    }
    for (int i = 0; i < lines.size(); i++)//检查是否有空洞
    {
      LinkedHashMap<String, String> line = (LinkedHashMap<String, String>) lines.get(i);
      for (int j = 0; j < line.size(); j++)
      {
        String k = title[j];
        int index = k.indexOf(':');
        k = k.substring(0, index);
        String v = line.get(k);
        if (v == null || v.trim().isEmpty())
        {
          System.out.println("空洞：" + (i + 3) + "/" + fd[j]);
        }
      }
    }
    if (kf.length > 1)//如果文件名是多个字段组成，则肯定不是多行表格
    {
      for (Map<String, String> line : lines)
      {
        StringBuilder fileName = new StringBuilder();
        for (String f : kf)
        {
          String t = line.get(f);
          try
          {
            String ii = this.parseValue(t, types.get(f), false);
            fileName.append(ii);
          } catch (Exception e)
          {
            fileName.append(t);
          }
          fileName.append("_");
        }
        fileName.deleteCharAt(fileName.length() - 1);
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(this.pkg).append(";\n\n");
        int i = 0;
        for (Map.Entry<String, String> en : line.entrySet())
        {
          String type = types.get(en.getKey());//类型
          String value = en.getValue();
          boolean array = value.startsWith("[") && value.endsWith("]");
          sb.append(en.getKey()).append(" = ");
          sb.append(parseValue(value, type, array)).append(";// ").append(fd[i]).append("\n");
          i++;
        }
        writeFile(this.pkg, fileName.toString(), sb.toString());
      }
    } else//需要考虑多行表格
    {
      String key = this.kf[0];
      List<Map<String, List<String>>> ml = toMline(key, lines);
      for (Map<String, List<String>> l : ml)
      {
        String fname = l.get(key).get(0);
        if (isFloat(fname))
        {
          fname = String.valueOf(toInt(fname));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(this.pkg).append(";\n\n");
        int i = 0;
        for (Map.Entry<String, List<String>> en : l.entrySet())
        {
          if (!excludes.get(en.getKey()))
          {
            String type = types.get(en.getKey());//类型
            List<String> value = en.getValue();
            boolean array = !en.getKey().equals(key);//如果是主key，则不是array
            sb.append(en.getKey()).append(" = ");
            if (!array)
            {
              sb.append(parseValue(value.get(0), type, false)).append(";// ").append(fd[i]).append("\n");
            } else
            {
              sb.append(parseValue(value, type)).append(";// ").append(fd[i]).append("\n");
            }
          }
          i++;
        }
        writeFile(this.pkg, fname, sb.toString());
      }
    }
  }

  /**
   * 解析(不能考虑多行表格)
   *
   * @param child
   */
  public void parse(String... child)
  {
    List<Map<String, String>> content = ExcelUtil.excelMap(excel, sheet, 1);
    String[][] temp = ExcelUtil.readExcel(excel, sheet, 0, 1, content.get(0).size());
    String[] fd = temp[0];//字段说明
    List<Map<String, String>> lines = new ArrayList<>();//行内容
    Map<String, String> types = new LinkedHashMap<>();//字段类型
    Map<String, Boolean> excludes = new LinkedHashMap<>();//是否被排除
    for (Map<String, String> line : content)
    {
      Map<String, String> item = new LinkedHashMap<>();
      lines.add(item);
      for (Map.Entry<String, String> en : line.entrySet())
      {
        String key = en.getKey();
        String value = en.getValue();
        int index = key.indexOf(':');
        item.put(key.substring(0, index), value);
        if (key.endsWith("exclude"))
        {
          key = key.substring(0, key.lastIndexOf(':'));
          excludes.put(key.substring(0, index), Boolean.TRUE);
        } else
        {
          excludes.put(key.substring(0, index), Boolean.FALSE);
        }
        types.put(key.substring(0, index), key.substring(index + 1));
      }
    }
    for (Map<String, String> line : lines)
    {
      StringBuilder fileName = new StringBuilder();
      for (String f : kf)
      {
        String t = line.get(f);
        try
        {
          String fname = t;
          if (isFloat(fname))
          {
            fname = String.valueOf(toInt(fname));
          }
          fileName.append(fname);
        } catch (Exception e)
        {
          fileName.append(t);
        }
        fileName.append("_");
      }
      fileName.deleteCharAt(fileName.length() - 1);

      StringBuilder tar = new StringBuilder();
      for (String c : child)
      {
        String item = line.get(c);
        boolean f = isFloat(item);
        item = f ? toInt(item) + "" : item;
        tar.append(item);
        tar.append(".");
      }
      tar.deleteCharAt(tar.length() - 1);
      String realPkg = this.pkg;
      StringBuilder sb = new StringBuilder();
      sb.append("package ").append(realPkg).append(";\n\n");
      int i = 0;
      for (Map.Entry<String, String> en : line.entrySet())
      {
        String type = types.get(en.getKey());//类型
        String value = en.getValue();
        boolean array = value != null && value.startsWith("[") && value.endsWith("]");
        sb.append(en.getKey()).append(" = ");
        sb.append(parseValue(value, type, array)).append(";// ").append(fd[i]).append("\n");
        i++;
      }
      writeFile(this.pkg + "." + tar, fileName.toString(), sb.toString());
    }
  }

  /**
   * 转换为int
   *
   * @param s
   * @return
   */
  public Integer toInt(String s)
  {
    return isEmpty(s) ? null : (int) Double.parseDouble(s);
  }

  /**
   * 转换为int
   *
   * @param s
   * @return
   */
  public long toLong(String s)
  {
    return (long) Double.parseDouble(s);
  }

  /**
   * 转换为int
   *
   * @param s
   * @param def
   * @return
   */
  public int toInt(String s, int def)
  {
    return s == null || s.isEmpty() ? def : (int) Double.parseDouble(s);
  }

  /**
   * 转换为int
   *
   * @param s
   * @return
   */
  public float toFloat(String s)
  {
    return (float) Double.parseDouble(s);
  }

  /**
   * 转换为int
   *
   * @param s
   * @param def
   * @return
   */
  public float toFloat(String s, float def)
  {
    return (float) (s == null || s.isEmpty() ? def : Double.parseDouble(s));
  }

  /**
   * 写入文件
   *
   * @param pkg
   * @param toString
   * @param toString0
   */
  private void writeFile(String pkg, String fileName, String content)
  {
    String[] sp = StringUtil.split(pkg, '.');
    StringBuilder sb = new StringBuilder();
    sb.append("./script/");
    for (String item : sp)
    {
      sb.append(item).append("/");
    }
    sb.append(fileName).append(".groovy");
    File dest = new File(sb.toString());
    FileUtil.write(dest, content);
  }

  /**
   * 转换成一个字符串
   *
   * @param value
   * @param type
   * @param array
   * @return
   */
  private String parseValue(String value, String type, boolean array)
  {
    if (array)
    {
      String temp = value.substring(1, value.length() - 1);
      String[] vs = StringUtil.split(temp, ',');
      switch (type.toLowerCase())
      {
        case "string":
          JSONArray arr = new JSONArray();
          arr.addAll(Arrays.asList(vs));
          return arr.toString() + " as String[]";
        case "float":
          StringBuilder sb = new StringBuilder();
          sb.append("[");
          for (String item : vs)
          {
            sb.append(Float.parseFloat(item)).append("f,");
          }
          sb.deleteCharAt(sb.length() - 1);
          sb.append("] as float[]");
          return sb.toString();
        case "int":
          return value + " as int[]";
        case "long":
          return value + " as long[]";
        case "boolean":
          return value + " as boolean[]";
      }
    } else
    {
      switch (type.toLowerCase())
      {
        case "float":
          return value + "f";
        case "string":
          if (value == null || value.trim().isEmpty())
          {
            return "null";
          } else
          {
            if (isInt(value))
            {
              return "\"" + toInt(value) + "\"";
            }
            return "\"" + value + "\"";
          }
        case "int":
          return String.valueOf(toInt(value));
        case "long":
          return String.valueOf(toLong(value));
        case "boolean":
          return toInt(value) == 0 ? "false" : "true";
      }
    }
    return value;
  }

  /**
   * 集合
   *
   * @param value
   * @param type
   * @return
   */
  private String parseValue(List<String> value, String type)
  {
    boolean array = false;
    if (value.size() == 1)
    {
      String head = value.get(0);
      if (head != null && head.startsWith("[") && head.endsWith("]"))
      {
        array = true;
      }
    }
    if (value.size() <= 1)//不需要使用
    {
      return this.parseValue(value.get(0), type, array);
    } else
    {
      switch (type.toLowerCase())
      {
        case "string":
          JSONArray arr = new JSONArray();
          arr.addAll(value);
          return arr.toString() + " as String[]";
        case "float":
          StringBuilder sb = new StringBuilder();
          sb.append("[");
          for (String item : value)
          {
            sb.append(Float.parseFloat(item)).append("f,");
          }
          sb.deleteCharAt(sb.length() - 1);
          sb.append("] as float[]");
          return sb.toString();
        case "int":
          sb = new StringBuilder();
          sb.append("[");
          for (String item : value)
          {
            sb.append(toInt(item)).append(",");
          }
          sb.deleteCharAt(sb.length() - 1);
          sb.append("] as int[]");
          return sb.toString();
        case "boolean":
          sb = new StringBuilder();
          sb.append("[");
          for (String item : value)
          {
            sb.append(toInt(item) == 0 ? "false" : "true").append(",");
          }
          sb.deleteCharAt(sb.length() - 1);
          sb.append("] as boolean[]");
          return sb.toString();
      }
    }
    return null;
  }

  /**
   * 单行变多行
   *
   * @param key
   * @param lines
   * @return
   */
  private List<Map<String, List<String>>> toMline(String key, List<Map<String, String>> lines)
  {
    List<Map<String, List<String>>> ml = new ArrayList<>();
    for (Map<String, String> m : lines)
    {
      String v = m.get(key);
      Map<String, List<String>> tail;
      if (ml.isEmpty())
      {
        tail = new LinkedHashMap<>();
        ml.add(tail);
        for (String k : m.keySet())
        {
          tail.put(k, new ArrayList<String>());
        }
      } else
      {
        tail = ml.get(ml.size() - 1);
      }
      List<String> l = tail.get(key);
      if (!l.isEmpty() && !l.contains(v))//不为空但是也不包含v，说明需要一个新的行
      {
        tail = new LinkedHashMap<>();
        ml.add(tail);
        for (String k : m.keySet())
        {
          tail.put(k, new ArrayList<String>());
        }
      }
      for (String k : m.keySet())
      {
        tail.get(k).add(m.get(k));
      }
    }
    return ml;
  }

  private boolean isFloat(String tar)
  {
    try
    {
      float f = Float.parseFloat(tar);
    } catch (Exception e)
    {
      return false;
    }
    return true;
  }

  private boolean isInt(String tar)
  {
    if (isFloat(tar))
    {
      float f = toFloat(tar);
      int d = toInt(tar);
      return d == f;
    }
    return false;
  }

  private boolean isEmpty(String s)
  {
    return s == null || s.isEmpty();
  }
}
