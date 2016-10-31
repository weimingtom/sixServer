/**
 * 生成id
 */
package org.ngame.util;

import java.util.UUID;

/**
 *
 * @author beykery
 */
public class IDGenerator
{

	private IDGenerator()
	{
	}

	/**
	 * 生成一个uuid
	 *
	 * @return
	 */
	public static String getNextID()
	{
		return UUID.randomUUID().toString();
	}
}
