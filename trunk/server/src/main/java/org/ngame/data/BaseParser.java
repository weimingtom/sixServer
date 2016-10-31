/**
 * 基类
 */
package org.ngame.data;

import com.alibaba.fastjson.JSONArray;

/**
 *
 * @author Beykery
 */
public class BaseParser
{

	/**
	 * 转换为int
	 *
	 * @param s
	 * @return
	 */
	public int toInt(String s)
	{
		return (int) Double.parseDouble(s);
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
	 * 列表
	 *
	 * @param s
	 * @return
	 */
	public JSONArray toIntList(String s)
	{
		if (s == null || s.isEmpty())
		{
			return null;
		}
		JSONArray arr = new JSONArray();
		int start = 0;
		int end = 0;
		int len = s.length();
		while (++end <= len)
		{
			if (end >= len || s.charAt(end) == ',')
			{
				arr.add(toInt(s.substring(start, end)));
				start = end + 1;
			}
		}
		return arr;
	}

	/**
	 * 列表
	 *
	 * @param s
	 * @return
	 */
	public JSONArray toStringList(String s)
	{
		if (s == null || s.isEmpty())
		{
			return null;
		}
		JSONArray arr = new JSONArray();
		int start = 0;
		int end = 0;
		int len = s.length();
		while (++end <= len)
		{
			if (end >= len || s.charAt(end) == ',')
			{
				arr.add(s.substring(start, end));
				start = end + 1;
			}
		}
		return arr;
	}

	/**
	 * 列表
	 *
	 * @param s
	 * @return
	 */
	public JSONArray toFloatList(String s)
	{
		if (s == null || s.isEmpty())
		{
			return null;
		}
		JSONArray arr = new JSONArray();
		int start = 0;
		int end = 0;
		int len = s.length();
		while (++end <= len)
		{
			if (end >= len || s.charAt(end) == ',')
			{
				arr.add(toFloat(s.substring(start, end)));
				start = end + 1;
			}
		}
		return arr;
	}
}
