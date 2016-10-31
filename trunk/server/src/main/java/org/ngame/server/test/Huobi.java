package org.ngame.server.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class Huobi
{

	private static class TrustAnyTrustManager implements X509TrustManager
	{

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException
		{
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException
		{
		}

		@Override
		public X509Certificate[] getAcceptedIssuers()
		{
			return new X509Certificate[]
			{
			};
		}
	}

	private static class TrustAnyHostnameVerifier implements HostnameVerifier
	{

		@Override
		public boolean verify(String hostname, SSLSession session)
		{
			return true;
		}
	}

	/**
	 * 访问
	 *
	 * @throws java.security.NoSuchAlgorithmException
	 * @throws java.security.KeyManagementException
	 * @throws java.net.MalformedURLException
	 */
	public void huo() throws NoSuchAlgorithmException, KeyManagementException, MalformedURLException, IOException
	{
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, new TrustManager[]
		{
			new TrustAnyTrustManager()
		}, new java.security.SecureRandom());
		//URL console = new URL("https://www.huobi.com/market/huobi.php?a=detail&jsoncallback=jQuery17106002512888517231_1386153918884&_=1386153923924");
		//https://api.huobi.com/staticmarket/detail_btc.js?callback=view_detail_btc&r=fr2dz2pic4rc0udi&_=1449478872432
    URL console = new URL("https://api.huobi.com/staticmarket/detail_btc.js?callback=view_detail_btc&r=fr2dz2pic4rc0udi&_=1449478872432");
    HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
		conn.setSSLSocketFactory(sc.getSocketFactory());
		conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestProperty("Accept", "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31");
		conn.connect();
		if (conn.getResponseCode() == 200)
		{
			InputStream is = conn.getInputStream();
			byte[] buff = new byte[1024];
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int n = 0;
			while ((n = is.read(buff)) > 0)
			{
				bos.write(buff, 0, n);
			}
			String content = new String(bos.toByteArray(), "utf-8");
			int index0 = content.indexOf('(');
			int index1 = content.lastIndexOf(')');
			content = content.substring(index0 + 1, index1);
			JSONObject json = JSON.parseObject(content);
			System.out.println(fz);
			System.out.println("sells:" + json.get("sells"));
			System.out.println("buys:" + json.get("buys"));
			System.out.println("trades:" + json.get("trades"));
			System.out.println("p_new:" + json.get("p_new"));
			System.out.println("level:" + json.get("level"));
			System.out.println("amount:" + json.get("amount"));
			System.out.println("total:" + json.get("total"));
			System.out.println("amp:" + json.get("amp"));
			System.out.println("p_open:" + json.get("p_open"));
			System.out.println("p_high:" + json.get("p_high"));
			System.out.println("p_low:" + json.get("p_low"));
			System.out.println("top_sell:" + json.get("top_sell"));
			System.out.println("top_buy:" + json.get("top_buy"));

		}
		conn.disconnect();
	}
	private static final String fz = "\n"
			+ "                   _ooOoo_\n"
			+ "                  o8888888o\n"
			+ "                  88\" . \"88\n"
			+ "                  (| -_- |)\n"
			+ "                  O\\  =  /O\n"
			+ "               ____/`---'\\____\n"
			+ "             .'  \\\\|     |//  `.\n"
			+ "            /  \\\\|||  :  |||//  \\\n"
			+ "           /  _||||| -:- |||||-  \\\n"
			+ "           |   | \\\\\\  -  /// |   |\n"
			+ "           | \\_|  ''\\---/''  |   |\n"
			+ "           \\  .-\\__  `-`  ___/-. /\n"
			+ "         ___`. .'  /--.--\\  `. . __\n"
			+ "      .\"\" '<  `.___\\_<|>_/___.'  >'\"\".\n"
			+ "     | | :  `- \\`.;`\\ _ /`;.`/ - ` : | |\n"
			+ "     \\  \\ `-.   \\_ __\\ /__ _/   .-` /  /\n"
			+ "======`-.____`-.___\\_____/___.-`____.-'======\n"
			+ "                   `=---='\n"
			+ "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n"
			+ "         佛祖保佑              比特币升\n";

	public static void main(String[] args) throws Exception
	{

		new Huobi().huo();
	}
}
