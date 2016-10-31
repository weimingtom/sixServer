/**
 * io流处理工具
 */
package org.ngame.util;

import io.netty.buffer.ByteBuf;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author beykery
 */
public class IOUtil
{

	private IOUtil()
	{
	}

	/**
	 * 读字符串
	 *
	 * @param bb
	 * @return
	 */
	public static String readString(ByteBuf bb)
	{
		byte[] bs = new byte[bb.readShort()];
		bb.readBytes(bs);
		try
		{
			return new String(bs,"utf-8");
		} catch (UnsupportedEncodingException ex)
		{
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 读字符串
	 *
	 * @param bb
	 * @param ks
	 * @return
	 */
	public static String readString(ByteBuf bb, byte[] ks)
	{
		try
		{
			byte[] bs = new byte[bb.readShort()];
			bb.readBytes(bs);
			MathUtil.xor(bs, ks);
			return new String(bs,"utf-8");
		} catch (UnsupportedEncodingException ex)
		{
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 读取流中所有内容
	 *
	 * @param is
	 * @return
	 */
	public static byte[] getContent(InputStream is)
	{
		BufferedInputStream bis = new BufferedInputStream(is);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int l;
		try
		{
			while ((l = bis.read(buf)) > 0)
			{
				baos.write(buf, 0, l);
			}
			return baos.toByteArray();
		} catch (Exception e)
		{
			return null;
		}
	}
}
