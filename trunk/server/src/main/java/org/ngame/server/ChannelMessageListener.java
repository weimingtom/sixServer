/**
 * 通道监听
 */
package org.ngame.server;

/**
 *
 * @author beykery
 */
public interface ChannelMessageListener
{

	/**
	 * 消息
	 *
	 * @param channel
	 * @param message
	 */
	public void onChannelMessage(ServerChannel channel, Object message);
}
