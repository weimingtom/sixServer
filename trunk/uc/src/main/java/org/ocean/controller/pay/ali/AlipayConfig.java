package org.ocean.controller.pay.ali;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。

 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */
public class AlipayConfig
{

  // ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
  // 合作身份者ID，以2088开头由16位纯数字组成的字符串
  public static String partner = "2088711877021439";

  public static String seller_id = "2088711877021439";

  public static String seller_email = "dev@nookjoy.com";
  // 商户的私钥
  public static String key = "o8j3pd23dnx6iue3zwp8bx2ax1q6meyb";

  public static String return_url = "";

  public static String show_url = "www.nookjoy.com";

  public static String notifyUrl = "http://ocean.nookjoy.com/ocean/alipayNotify.pay";

  // ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
  // 字符编码格式 目前支持 gbk 或 utf-8
  public static String input_charset = "utf-8";

  // 签名方式 不需修改
  public static String sign_type = "MD5";

  public static String payment_type = "1";

  public static String alipay_service = "create_direct_pay_by_user";

  public static String alipay_url = "https://mapi.alipay.com/gateway.do?";

  public static void setPartner(String partner)
  {
    AlipayConfig.partner = partner;
  }

  public static void setSeller_id(String seller_id)
  {
    AlipayConfig.seller_id = seller_id;
  }

  public static void setSeller_email(String seller_email)
  {
    AlipayConfig.seller_email = seller_email;
  }

  public static void setKey(String key)
  {
    AlipayConfig.key = key;
  }

  public static void setReturn_url(String return_url)
  {
    AlipayConfig.return_url = return_url;
  }

  public static void setShow_url(String show_url)
  {
    AlipayConfig.show_url = show_url;
  }

  public static void setInput_charset(String input_charset)
  {
    AlipayConfig.input_charset = input_charset;
  }

  public static void setSign_type(String sign_type)
  {
    AlipayConfig.sign_type = sign_type;
  }

  public static void setPayment_type(String payment_type)
  {
    AlipayConfig.payment_type = payment_type;
  }

  public static void setAlipay_service(String alipay_service)
  {
    AlipayConfig.alipay_service = alipay_service;
  }

  public static void setAlipay_url(String alipay_url)
  {
    AlipayConfig.alipay_url = alipay_url;
  }

  public static void setNotifyUrl(String notifyUrl)
  {
    AlipayConfig.notifyUrl = notifyUrl;
  }

}
