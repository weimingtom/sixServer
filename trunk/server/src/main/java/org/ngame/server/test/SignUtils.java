package org.ngame.server.test;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import org.ngame.util.Base64;

public class SignUtils
{

  private static final String ALGORITHM = "RSA";

  private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

  private static final String DEFAULT_CHARSET = "UTF-8";

  public static String sign(String content, String privateKey)
  {
    try
    {
      PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
      KeyFactory keyf = KeyFactory.getInstance(ALGORITHM,"BC");
      PrivateKey priKey = keyf.generatePrivate(priPKCS8);
      java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
      signature.initSign(priKey);
      signature.update(content.getBytes(DEFAULT_CHARSET));
      byte[] signed = signature.sign();
      return Base64.encode(signed);
    } catch (Exception e)
    {
      e.printStackTrace();
    }

    return null;
  }

  public static void main(String[] args)
  {
    String r = sign("dlkfjtest", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDdHOL7mi67bICp5sU8U1E8YBoGgCSYacv8f9lYZ22dj8AnqNqQgnkTpjFDzVJnYJvRb6roQqDigA0ZKIUCFjx/4utvh1DIlFjkwI+U6LvHGVspTmGiBKhRWhyBka0hB5bQ69IRw7xWS1wmDTKtHZByt3XbL5Y4Fdb38niKSgy/xwIDAQAB");
    System.out.println(r);
  }
}
