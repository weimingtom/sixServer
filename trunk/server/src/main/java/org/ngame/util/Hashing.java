/**
 * jedis的hash值计算
 */
package org.ngame.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * hash值的计算，默认为md5
 *
 * @author beykery
 */
public interface Hashing
{

	public static final Hashing MURMUR_HASH = new MurmurHash();
	public ThreadLocal<MessageDigest> md5Holder = new ThreadLocal<>();
	public static final Hashing MD5 = new Hashing()
	{
		@Override
		public long hash(String key)
		{
			return hash(key.getBytes());
		}

		@Override
		public long hash(byte[] key)
		{
			try
			{
				if (md5Holder.get() == null)
				{
					md5Holder.set(MessageDigest.getInstance("MD5"));
				}
			} catch (NoSuchAlgorithmException e)
			{
				throw new IllegalStateException("++++ no md5 algorythm found");
			}
			MessageDigest md5 = md5Holder.get();
			md5.reset();
			md5.update(key);
			byte[] bKey = md5.digest();
			long res = ((long) (bKey[3] & 0xFF) << 24)
					| ((long) (bKey[2] & 0xFF) << 16)
					| ((long) (bKey[1] & 0xFF) << 8) | bKey[0] & 0xFF;
			return res;
		}
	};

	public long hash(String key);

	public long hash(byte[] key);
}