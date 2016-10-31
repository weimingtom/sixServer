package org.ocean.util.any;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

/**
 * AnySDK 支付通知验签算法
 *
 * @author libo
 * @date 2014-08-13
 */
public class PayNotify
{

  /**
   * 全局数组,用于base64
   */
  private final String[] _strDigits =
  {
    "0", "1", "2", "3", "4", "5",
    "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"
  };

  public void PayNotify()
  {
  }

  /**
   * 验证签名
   *
   * @param paramValues
   * @param originSign
   * @param privateKey
   * @return boolean
   */
  public boolean checkSign(String paramValues, String originSign, String privateKey)
  {
    if (originSign == null)
    {
      return false;
    }
    String newSign = getSign(paramValues, privateKey);
    return newSign.equals(originSign);
  }

  /**
   * 计算待签字符串的sign值
   *
   * @param paramValues
   * @param privateKey
   * @return String 计算所得到的sign签名
   */
  public String getSign(String paramValues, String privateKey)
  {
    String md5Values = MD5Encode(paramValues);
    md5Values = MD5Encode(md5Values.toLowerCase() + privateKey).toLowerCase();
    return md5Values;
  }

  /**
   * MD5编码算法
   *
   * @param sourceStr
   * @return String md5值
   */
  public String MD5Encode(String sourceStr)
  {
    String signStr = null;
    try
    {
      byte[] bytes = sourceStr.getBytes("utf-8");
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(bytes);
      byte[] md5Byte = md5.digest();
      if (md5Byte != null)
      {
        signStr = _byteToString(md5Byte);
      }
    } catch (NoSuchAlgorithmException e)
    {
    } catch (UnsupportedEncodingException e)
    {
    }
    return signStr;
  }

  /**
   * 返回形式为数字跟字符串
   */
  private String _byteToArrayString(byte bByte)
  {
    int iRet = bByte;
    // System.out.println("iRet="+iRet);
    if (iRet < 0)
    {
      iRet += 256;
    }
    int iD1 = iRet / 16;
    int iD2 = iRet % 16;
    return _strDigits[iD1] + _strDigits[iD2];
  }

  /**
   * 返回形式只为数字
   */
  private String _byteToNum(byte bByte)
  {
    int iRet = bByte;
    //System.out.println("iRet1=" + iRet);
    if (iRet < 0)
    {
      iRet += 256;
    }
    return String.valueOf(iRet);
  }

  /**
   * 转换字节数组为16进制字串
   */
  private String _byteToString(byte[] bByte)
  {
    StringBuilder sBuffer = new StringBuilder();
    for (int i = 0; i < bByte.length; i++)
    {
      sBuffer.append(_byteToArrayString(bByte[i]));
    }
    return sBuffer.toString();
  }
}
