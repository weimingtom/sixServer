package org.ngame.util;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.io.InputStream;

/**
 * 效率问题
 */
@Deprecated
public class ByteBufInputStream extends InputStream
{

	private ByteBuf message;
	private byte[] ks;
	private int index;
	private int length;

	public ByteBufInputStream(ByteBuf message, byte[] ks)
	{
		this.message = message;
		this.ks = ks;
		this.length = message.readShort();
	}

	/**
	 * Reads the next byte of data from the input stream. The value byte is
	 * returned as an <code>int</code> in the range <code>0</code> to
	 * <code>255</code>. If no byte is available because the end of the stream
	 * has been reached, the value <code>-1</code> is returned. This method
	 * blocks until input data is available, the end of the stream is detected,
	 * or an exception is thrown.
	 * <p/>
	 * <p>
	 * A subclass must provide an implementation of this method.
	 *
	 * @return the next byte of data, or <code>-1</code> if the end of the
	 * stream is reached.
	 * @throws IOException if an I/O error occurs.
	 */
	@Override
	public int read() throws IOException
	{
		if (index >= length)
		{
			return -1;
		}
		byte t = this.message.readByte();
		if (ks != null)
		{
			t = (byte) (t ^ ks[index % ks.length]);
			index++;
		}
		return t;
	}

}
