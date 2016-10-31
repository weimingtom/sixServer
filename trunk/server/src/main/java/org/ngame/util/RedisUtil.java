/**
 * 持久化工具
 */
package org.ngame.util;

import io.netty.util.concurrent.FastThreadLocal;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.rojo.repository.Rojo;
import org.rojo.util.CacheoutListerner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 *
 * @author beykery
 */
public class RedisUtil
{

  private static final Logger LOG = Logger.getLogger(RedisUtil.class);
  private static final RedisUtil util = new RedisUtil();
  private JedisPool pool;
  private FastThreadLocal<Rojo> re;
  private int db;

  private RedisUtil()
  {
    try
    {
      JedisPoolConfig config = new JedisPoolConfig();
      Properties p = new Properties();
      p.load(new FileInputStream(new File("./jedis.properties")));
      config.setMaxTotal(Integer.valueOf(p.getProperty("redis.pool.maxActive")));
      config.setMaxIdle(Integer.valueOf(p.getProperty("redis.pool.maxIdle")));
      config.setMaxWaitMillis(Long.valueOf(p.getProperty("redis.pool.maxWait")));
      config.setTestOnBorrow(Boolean.valueOf(p.getProperty("redis.pool.testOnBorrow")));
      config.setTestOnReturn(Boolean.valueOf(p.getProperty("redis.pool.testOnReturn")));
      pool = new JedisPool(config, p.getProperty("redis.ip"), Integer.valueOf(p.getProperty("redis.port")), 2000, p.getProperty("redis.pass"));
      db = Integer.valueOf(p.getProperty("redis.db", "0"));
      re = new FastThreadLocal<>();
      Rojo.setCacheoutListerner(new CacheoutListerner()
      {

        @Override
        public void onCacheout(Class claz, String id)
        {
          LOG.error(claz.getName() + ":" + id + " out of cache.");
        }
      });
    } catch (Exception e)
    {
      LOG.error("无法连接redis");
    }
  }

  /**
   * 连接
   *
   * @return
   */
  public static Jedis jedis()
  {
    return rojo().getJedis();
  }

  /**
   * 肉鸡
   *
   * @return
   */
  public static Rojo rojo()
  {
    Rojo r = util.re.get();
    Jedis je = r == null ? null : r.getJedis();
    if (je == null || je.getClient().isBroken())
    {
      if (je != null)
      {
        je.close();
      }
      je = util.pool.getResource();
      je.select(util.db);
      r = new Rojo(je);
      util.re.set(r);
    }
    return r;
  }

  /**
   * 生成一个id
   *
   * @param rojo
   * @param claz
   * @param sid
   * @return 1f490
   */
  public static String id(Rojo rojo, Class claz, int sid)
  {
    return sid + "f" + rojo.getJedis().incr(claz.getSimpleName() + ":::id");
  }
}
