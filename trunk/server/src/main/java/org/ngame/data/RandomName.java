/**
 * 处理随机名字
 */
package org.ngame.data;

import com.alibaba.fastjson.JSONArray;
import io.netty.util.internal.StringUtil;
import java.io.File;
import org.ngame.util.FileUtil;

/**
 *
 * @author beykery
 */
public class RandomName
{

  public static void main(String[] args)
  {
    File dest = new File("./script/player/names.groovy");
    JSONArray arr = new JSONArray();
    File f = new File("./data/firstname.txt");
    String c = FileUtil.getFileContent(f);
    String[] lines = StringUtil.split(c, '\n');
    for (String item : lines)
    {
      if (!item.trim().isEmpty())
      {
        arr.add(item.trim());
      }
    }
    StringBuilder sb = new StringBuilder();
    sb.append("package player;\n\n");
    sb.append("firstname=");
    sb.append(arr.toString());
    sb.append(" as String[];");
    FileUtil.write(dest, sb.toString() + "\n");

    arr = new JSONArray();
    f = new File("./data/man.txt");
    c = FileUtil.getFileContent(f);
    lines = StringUtil.split(c, '\n');
    for (String item : lines)
    {
      if (!item.trim().isEmpty())
      {
        arr.add(item.trim());
      }
    }
    sb = new StringBuilder();
    sb.append("man=");
    sb.append(arr.toString());
    sb.append(" as String[];");
    FileUtil.append(dest, sb.toString() + "\n");

    arr = new JSONArray();
    f = new File("./data/woman.txt");
    c = FileUtil.getFileContent(f);
    lines = StringUtil.split(c, '\n');
    for (String item : lines)
    {
      if (!item.trim().isEmpty())
      {
        arr.add(item.trim());
      }
    }
    sb = new StringBuilder();
    sb.append("woman=");
    sb.append(arr.toString());
    sb.append(" as String[];");
    FileUtil.append(dest, sb.toString() + "\n");
  }
}
