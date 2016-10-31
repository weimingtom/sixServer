/**
 * gzip
 */
package org.ngame.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 *
 * @author beykery
 */
public class Gzip
{

  /**
   * 压缩
   *
   * @param bs
   * @return
   */
  public static byte[] gzip(byte[] bs)
  {
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      GZIPOutputStream out = new GZIPOutputStream(baos);
      out.write(bs);
      out.flush();
      return baos.toByteArray();
    } catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 解压
   *
   * @param bs
   * @return
   */
  public static byte[] unGzip(byte[] bs)
  {
    try
    {
      GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(bs));
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      int n = 0;
      byte[] buf = new byte[1024];
      while ((n = in.read(buf)) > 0)
      {
        baos.write(buf, 0, n);
      }
      return baos.toByteArray();
    } catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }
}
