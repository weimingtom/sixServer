/**
 * 渠道工具
 */
package org.ngame.util;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Beykery
 */
public class ChannelUtil
{

	/**
	 * 计算91的序列号（Sign）
	 *
	 * @param ps
	 * @return
	 */
	public static String get91LoginSign(Object... ps)
	{
		StringBuilder sb = new StringBuilder();
		for (Object item : ps)
		{
			sb.append(item);
		}
		return hashToMD5Hex(sb.toString());
	}

	/**
	 * 91的签名算法
	 *
	 * @param format
	 * @return
	 */
	public static String hashToMD5Hex(String format)
	{
		String sign = null;
		try
		{
			byte[] bytes = format.getBytes("utf-8");
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(bytes);
			byte[] md5Byte = md5.digest();
			if (md5Byte != null)
			{
				sign = HexBin.encode(md5Byte);
			}
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return sign;
	}

	public static byte[] getBytes(char[] chars)
	{
		Charset cs = Charset.forName("UTF-8");
		CharBuffer cb = CharBuffer.allocate(chars.length);
		cb.put(chars);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);
		return bb.array();
	}

  private ChannelUtil()
  {
  }
}
