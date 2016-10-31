/**
 * 字符串处理
 */
package org.ngame.protocol.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author beykery
 */
public class StringUtil
{

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
}
