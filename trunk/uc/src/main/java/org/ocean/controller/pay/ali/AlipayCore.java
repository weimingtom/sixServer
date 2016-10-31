package org.ocean.controller.pay.ali;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.methods.multipart.FilePartSource;
import org.apache.commons.httpclient.methods.multipart.PartSource;

/* *
 *类名：AlipayFunction
 *功能：支付宝接口公用函数类
 *详细：该类是请求、通知返回两个文件所调用的公用函数核心处理文件，不需要修改
 *版本：3.3
 *日期：2012-08-14
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */
public class AlipayCore
{

  private static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";

  /**
   * 除去数组中的空值和签名参数
   *
   * @param sArray 签名参数组
   * @return 去掉空值与签名参数后的新签名参数组
   */
  public static Map<String, String> paraFilter(Map<String, String> sArray)
  {
    Map<String, String> result = new HashMap<>();
    if (sArray == null || sArray.size() <= 0)
    {
      return result;
    }
    for (String key : sArray.keySet())
    {
      String value = sArray.get(key);
      if (value == null || value.equals("") || key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type"))
      {
        continue;
      }
      result.put(key, value);
    }
    return result;
  }

  /**
   * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
   *
   * @param params 需要排序并参与字符拼接的参数组
   * @return 拼接后字符串
   */
  public static String createLinkString(Map<String, String> params)
  {
    List<String> keys = new ArrayList<>(params.keySet());
    Collections.sort(keys);
    StringBuilder prestr = new StringBuilder();
    for (int i = 0; i < keys.size(); i++)
    {
      String key = keys.get(i);
      String value = params.get(key);
      if (i == keys.size() - 1)
      {//拼接时，不包括最后一个&字符
        prestr.append(key).append("=").append(value);
      } else
      {
        prestr.append(key).append("=").append(value).append("&");
      }
    }
    return prestr.toString();
  }

  public static String createLinkStringWithEncode(Map<String, String> params)
  {
    List<String> keys = new ArrayList<>(params.keySet());
    Collections.sort(keys);
    StringBuilder prestr = new StringBuilder();
    for (int i = 0; i < keys.size(); i++)
    {
      String key = keys.get(i);
      String value = params.get(key);
      try
      {
        if (i == keys.size() - 1)
        {//拼接时，不包括最后一个&字符
          prestr.append(key).append("=").append(URLEncoder.encode(value, "UTF-8"));
        } else
        {
          prestr.append(key).append("=").append(URLEncoder.encode(value, "UTF-8")).append("&");
        }
      } catch (UnsupportedEncodingException e)
      {
        e.printStackTrace();
      }
    }
    return prestr.toString();
  }

  /**
   * 生成文件摘要
   *
   * @param strFilePath 文件路径
   * @param file_digest_type 摘要算法
   * @return 文件摘要结果
   * @throws java.io.IOException
   */
  public static String getAbstract(String strFilePath, String file_digest_type) throws IOException
  {
    PartSource file = new FilePartSource(new File(strFilePath));
    switch (file_digest_type)
    {
      case "MD5":
        return DigestUtils.md5Hex(file.createInputStream());
      case "SHA":
        return DigestUtils.sha256Hex(file.createInputStream());
      default:
        return "";
    }
  }

  public static String buildRequestMysign(Map<String, String> sPara)
  {
    String prestr = AlipayCore.createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
    String mysign = "";
    if (AlipayConfig.sign_type.equals("MD5"))
    {
      mysign = MD5.sign(prestr, AlipayConfig.key, AlipayConfig.input_charset);
    }
    return mysign;
  }

  /**
   * 生成要请求给支付宝的参数数组
   *
   * @param sParaTemp 请求前的参数数组
   * @return 要请求的参数数组
   */
  private static Map<String, String> buildRequestPara(Map<String, String> sParaTemp)
  {
    //除去数组中的空值和签名参数
    Map<String, String> sPara = AlipayCore.paraFilter(sParaTemp);
    //生成签名结果
    String mysign = buildRequestMysign(sPara);
    //签名结果与签名方式加入请求提交参数组中
    sPara.put("sign", mysign);
    sPara.put("sign_type", AlipayConfig.sign_type);
    return sPara;
  }

  /**
   * 建立请求，以表单HTML形式构造（默认）
   *
   * @param sParaTemp 请求参数数组
   * @param strMethod 提交方式。两个值可选：post、get
   * @param strButtonName 确认按钮显示文字
   * @return 提交表单HTML文本
   */
  public static String buildRequest(Map<String, String> sParaTemp, String strMethod, String strButtonName)
  {
    //待请求参数数组
    Map<String, String> sPara = buildRequestPara(sParaTemp);
    List<String> keys = new ArrayList<>(sPara.keySet());
    StringBuilder sbHtml = new StringBuilder();
//        sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\"" + ALIPAY_GATEWAY_NEW
//                      + "_input_charset=" + AlipayConfig.input_charset + "\" method=\"" + strMethod
//                      + "\">");
    for (int i = 0; i < keys.size(); i++)
    {
      String name = keys.get(i);
      String value = sPara.get(name);
      sbHtml.append("<input type=\"hidden\" name=\"").append(name).append("\" value=\"").append(value).append("\"/>");
    }
    //submit按钮控件请不要含有name属性
    sbHtml.append("<input type=\"submit\" value=\"").append(strButtonName).append("\" style=\"display:none;\"></form>");
//        sbHtml.append("<script>document.forms['alipaysubmit'].submit();</script>");
    return sbHtml.toString();
  }
}
