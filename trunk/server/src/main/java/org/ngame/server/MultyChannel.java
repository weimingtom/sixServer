/**
 * 多路channel
 */
package org.ngame.server;

import io.netty.channel.EventLoopGroup;
import java.net.InetSocketAddress;

/**
 *
 * @author beykery
 */
public class MultyChannel
{

	private final InetSocketAddress addr;//地址
	private final int count;//路数
	private final ServerChannel[] channels;

	/**
	 * 多路通道
	 *
	 * @param addr
	 * @param count
	 * @param group
	 */
	public MultyChannel(InetSocketAddress addr, int count, EventLoopGroup group)
	{
		this.addr = addr;
		this.count = count;
		channels = new ServerChannel[count];
		for (int i = 0; i < count; i++)
		{
			channels[i] = new ServerChannel(addr, group);
		}
	}

	/**
	 * 连接
	 */
	public void connect()
	{
		for (int i = 0; i < count; i++)
		{
			if (!channels[i].isOpen())
			{
				channels[i].connect();
			}
		}
	}

	/**
	 * 返回所有的连接
	 *
	 * @return
	 */
	public ServerChannel[] getChannels()
	{
		return channels;
	}

	/**
	 * 返回总数
	 *
	 * @return
	 */
	public long count()
	{
		return count;
	}
}
