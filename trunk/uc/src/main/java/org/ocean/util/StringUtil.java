/**
 * 字符串
 */
package org.ocean.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.WebRequest;

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
  
  public static String getParameter(WebRequest request, String key) {
		Map<String, String[]> ps = request.getParameterMap();
		String [] strArr = ps.get(key);
		if(strArr == null) return null;
		String str = ps.get(key)[0];
		return str;
	}
	
	public static String[] getParameters(WebRequest request, String key) {
		Map<String, String[]> ps = request.getParameterMap();
		String [] strArr = ps.get(key);
		if(strArr == null) return null;
		
		return ps.get(key);
	}

	public static String getParameter(WebRequest request, String key, String defaultStr) {
		String str = getParameter(request, key);
		return str == null ? defaultStr : str;
	}

	public static int getIntParameter(WebRequest request, String key) {
		String str = getParameter(request, key);
		if (str == null || str.isEmpty()) {
			return -1;
		}
			
		return Integer.parseInt(str);
	}
	
	public static float getFloatParameter(WebRequest request, String key) {
		String str = getParameter(request, key);
		if (str == null || str.isEmpty())
			return -1;

		return Float.parseFloat(str);
	}
	
	public static long getLongParameter(WebRequest request, String key) {
		String str = getParameter(request, key);
		if (str == null || str.isEmpty())
			return -1L;
		
		return Long.parseLong(str);
	}
	
	public static Boolean getBooleanParameter(WebRequest request, String key) {
		String str = getParameter(request, key);
		if (str == null || str.isEmpty())
			return false;

		return Boolean.parseBoolean(str);
	}
	
	public static int[] getIntsParameter(WebRequest request, String key) {
		String [] strArr = getParameters(request, key);
		if(strArr == null) return null;
		
		int [] result = new int[strArr.length];
		for(int i=0; i<strArr.length; i++) {
			String str = strArr[i];
			if (str == null || str.isEmpty())
				return null;
			result[i] = Integer.parseInt(str);
		}
		return result;
	}

	public static int getIntParameter(WebRequest request, String key, int defaultInt) {
		int intStr = getIntParameter(request, key);
		return intStr == -1 ? defaultInt : intStr;
	}
	
	public static int getServerId(HttpSession session) {
		Object obj = session.getAttribute("curServer");
		if(obj == null) return -1;
		
		return (int)obj; 
	}
	
	public static Map<String, String> getMap(String paras) {
		Map<String, String> map = new HashMap<>();
		if(paras == null) return map;
		for(String para : paras.split("&")) {
			String [] str = para.split("=");
			if(str.length == 2) {
				map.put(str[0], str[1]);
			}
		}		
		return map;
	}

}
