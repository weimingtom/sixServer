package org.ngame.server.test;

import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.bouncycastle.util.encoders.UrlBase64;
import org.ngame.util.DESMD5;

/**
 * com Server
 *
 * @author Aina.huang E-mail: 674023920@qq.com
 * @version 创建时间：2010 Jul 14, 2010 10:45:35 AM 类说明
 */
public class Main
{

	private static final int PORT = 9999;// 端口监听

	private List<Socket> mList = new ArrayList<>();// 存放客户端socket
	private ServerSocket server = null;
	private ExecutorService mExecutorService = null;// 线程池

	/**
	 * 初始化系统配置
	 */
	static
	{
		try
		{
			Properties p = new Properties();//系统属性
			p.load(new InputStreamReader(new FileInputStream(new File("./system.properties")), "UTF-8"));
			Set<Object> set = p.keySet();
			for (Object key : set)
			{
				System.getProperties().setProperty(key.toString(), p.getProperty(key.toString()));
			}
		} catch (Exception e)
		{

		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException, UnsupportedEncodingException, Exception
	{
     JSONObject json = new JSONObject();
  json.put("id", 1);
  json.put("usercode", "beykery");
  json.put("testing", true);
  String s = new String(UrlBase64.encode(DESMD5.encrypt(json.toString().getBytes("UTF-8"), "421w6tW1ivg=")), "UTF-8");
    System.out.println(s);
//		NServer x = new TestServer(new InetSocketAddress(8888));
//		x.setProtocol(LVProtocol.class);
//		x.start();

		//	testAes();
		// TODO Auto-generated method stub
		//	System.out.println(sum(3));
//		int today = TimeUtils.dayofMonth(System.currentTimeMillis());
//		System.out.println(today);
		//System.out.println(System.currentTimeMillis()-24*3600*1000);
//		String a = "qwertyuiopasdfghjklzxcvbnm[];',./l\\=-";
//		System.out.println(a);
//		String key = "11game";
//		byte[] a1 = a.getBytes();
//		print(a1);
//		MathUtil.xor(a1, key.getBytes());
//		print(a1);
//		MathUtil.xor(a1, key.getBytes());
//		print(a1);
//		System.out.println(new String(a1));
//		JsonObject json = new JsonObject();
//		json.add("res", "3");
//		json.add("clientVersion", "2.0");
//		System.out.println(json.toString());
	}

	private static void print(byte[] a1)
	{
		for (int i = 0; i < a1.length; i++)
		{
			System.out.print(a1[i] + " ");
		}
		System.out.println();
	}

	static int sum(int n)
	{
		if (n <= 1)
		{
			return 1;
		} else
		{
			return (n + sum(--n));
		}
	}

	public Main()
	{
		try
		{
			server = new ServerSocket(PORT);
			mExecutorService = Executors.newCachedThreadPool();// 创建一个线程池
			System.out.println("Server Start...");
			Socket client = null;
			while (true)
			{
				client = server.accept();
				mList.add(client);
				mExecutorService.execute(new Service(client));// 开启一个客户端线程.
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public class Service implements Runnable
	{

		private Socket socket;
		private BufferedReader in = null;
		private String msg = "";

		public Service(Socket socket)
		{
			this.socket = socket;
			try
			{
				in = new BufferedReader(new InputStreamReader(socket
						.getInputStream()));
				msg = "user:" + this.socket.getInetAddress() + " come total:"
						+ mList.size();
				this.sendmsg();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		@Override
		public void run()
		{
			try
			{
				while (true)
				{
					if ((msg = in.readLine()) != null)
					{
						if (msg.equals("exit"))
						{
							System.out.println("sssssssssss");
							mList.remove(socket);
							in.close();
							msg = "user:" + socket.getInetAddress()
									+ " exit total:" + mList.size();
							socket.close();
							System.out.println(msg);
							this.sendmsg();
							break;
						} else
						{
							msg = socket.getInetAddress() + " : " + msg;
							this.sendmsg();
						}
					}

				}
			} catch (Exception ex)
			{
				System.out.println("server 读取数据异常");
				ex.printStackTrace();
			}
		}

		/**
		 * 发送消息给所有客户端
		 */
		public void sendmsg()
		{
			msg = "{\"action\":\"join\",\"step\":\"init\",\"message\":null,\"data\":[{\"id\":34,\"nickname\":\"99999\",\"photo\":\"sss\",\"beans\":1878423,\"score\":2200},{\"id\":34,\"nickname\":\"44444\",\"photo\":\"sss\",\"beans\":1878423,\"score\":2200},{\"id\":34,\"nickname\":\"33333\",\"photo\":\"sss\",\"beans\":1878423,\"score\":2200},{\"id\":34,\"nickname\":\"2222\",\"photo\":\"sss\",\"beans\":1878423,\"score\":2200},{\"id\":34,\"nickname\":\"11111\",\"photo\":\"sss\",\"beans\":1878423,\"score\":2200}]}";
			System.out.println(msg);
			int num = mList.size();
			for (int i = 0; i < num; i++)
			{
				Socket mSocket = mList.get(i);
				PrintWriter pout = null;
				try
				{
					pout = new PrintWriter(new BufferedWriter(
							new OutputStreamWriter(mSocket.getOutputStream())),
							true);
					pout.println(msg);
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 测试
	 */
	private static void testAes()
	{

	}
}
