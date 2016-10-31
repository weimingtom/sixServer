/**
 * 用来加密解密以及计算md5值
 */
package org.ocean.util;

import java.io.UnsupportedEncodingException;
import sun.misc.BASE64Encoder;
import javax.crypto.*;
import java.security.*;
import javax.crypto.spec.DESKeySpec;
import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;

/**
 * des加密和取md5算法
 *
 * @author beykery
 */
public class DESMD5
{

  private static final Logger LOG = Logger.getLogger(DESMD5.class);
  public static final String ALGORITHM = "DES";

  private static Key toKey(byte[] key) throws Exception
  {
    DESKeySpec dks = new DESKeySpec(key);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
    SecretKey secretKey = keyFactory.generateSecret(dks);
    return secretKey;
  }

  /**
   * 解密
   *
   * @param data
   * @param key
   * @return
   * @throws Exception
   */
  public static byte[] decrypt(byte[] data, String key) throws Exception
  {
    Key k = toKey(decryptBASE64(key));
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, k);
    return cipher.doFinal(data);
  }

  /**
   * 加密
   *
   * @param data
   * @param key
   * @return
   * @throws Exception
   */
  public static byte[] encrypt(byte[] data, String key) throws Exception
  {
    Key k = toKey(decryptBASE64(key));
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, k);
    return cipher.doFinal(data);
  }

  /**
   * 生成密钥
   *
   * @return
   * @throws Exception
   */
  public static String initKey() throws Exception
  {
    return initKey(null);
  }

  /**
   * 生成密钥
   *
   * @param seed
   * @return
   * @throws Exception
   */
  public static String initKey(String seed) throws Exception
  {
    SecureRandom secureRandom = null;
    if (seed != null)
    {
      secureRandom = new SecureRandom(decryptBASE64(seed));
    } else
    {
      secureRandom = new SecureRandom();
    }
    KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);
    kg.init(secureRandom);
    SecretKey secretKey = kg.generateKey();
    return encryptBASE64(secretKey.getEncoded());
  }

  /**
   * BASE64解密
   *
   * @param key
   * @return
   * @throws Exception
   */
  public static byte[] decryptBASE64(String key) throws Exception
  {
    return (new BASE64Decoder()).decodeBuffer(key);
  }

  /**
   * BASE64加密
   *
   * @param key
   * @return
   * @throws Exception
   */
  public static String encryptBASE64(byte[] key)
  {
    return (new BASE64Encoder()).encodeBuffer(key);
  }

  /**
   * 取md5值
   *
   * @param content
   * @return
   */
  public static byte[] getMD5(byte[] content)
  {
    try
    {
      MessageDigest md = MessageDigest.getInstance("MD5");
      return md.digest(content);
    } catch (NoSuchAlgorithmException ex)
    {
      LOG.error("取md5失败");
    }
    return null;
  }

  /**
   * md5指纹
   *
   * @param s
   * @param charset
   * @return
   */
  public final static String MD5Encoder(String s, String charset)
  {
    try
    {
      byte[] btInput = s.getBytes(charset);
      MessageDigest mdInst = MessageDigest.getInstance("MD5");
      mdInst.update(btInput);
      byte[] md = mdInst.digest();
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < md.length; i++)
      {
        int val = ((int) md[i]) & 0xff;
        if (val < 16)
        {
          sb.append("0");
        }
        sb.append(Integer.toHexString(val));
      }
      return sb.toString();
    } catch (Exception e)
    {
      return null;
    }
  }

  public static void main(String[] args) throws UnsupportedEncodingException
  {
    String c = MD5Encoder("明天", "utf-8");
//    String c = md5("salkjdfkljaaslkj;dfaslkdjfalkj;sdfaslkd;jf12312slkdj");
    System.out.println(c);
  }

}
