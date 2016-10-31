package org.ngame.server.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileTest
{
  private byte[] c;
  static int sum;

  public static void main(String[] args) throws Exception
  {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    System.out.println(df.format(new Date(1461074407171L)));
//    process(new File("./"));
//    System.out.println(sum);

//      boolean b=FileTest.class.getDeclaredField("c").getType()==byte[].class;
//      System.out.println(b);
    
//    InputStream in = FileTest.class.getResourceAsStream("conf.Properties");
//    Properties properties = new Properties();
//    properties.load(in);
//    System.out.println(properties.get("serverHost"));
  }

  public static void process(File file)
  {

    if (file.isFile() && (file.getName().endsWith(".groovy") || file.getName().endsWith(".java")))
    {
      int line = count(file);
      sum += line;
    } else
    {
      File[] fs = file.listFiles();
      if (fs != null)
      {
        for (int i = 0; i < fs.length; i++)
        {
          process(fs[i]);
        }
      }
    }
  }

  public static int count(File file)
  {
    int line = 0;
    try
    {
      FileInputStream fs = new FileInputStream(file);
//			FileReader fr = new FileReader(file);
      BufferedReader br = new BufferedReader(new InputStreamReader(fs));
//			BufferedReader br = new BufferedReader(fr);
      //	String s = br.readLine();//
      String s;
      while ((s = br.readLine()) != null) //			while (s != null)
      {
//				System.out.println(s);//输出每一行读取到的内容
        line++;
      }
      br.close();
    } catch (Exception e)
    {
    }
    return line;
  }
//	public static void main(String[] args)
//	{
//		FileReader input;
//		try
//		{
//			input = new FileReader("D:/aa.txt");
//			File f = new File("E:/august/xgameServer/src");
//			System.out.println(f.list());//取得目录下的文件，只取得一层返回string[]
////			System.out.println(f.listFiles());//
//			BufferedReader br = new BufferedReader(input);
////			char[] readbuffer=new char[100];//自己设定读取内容的多少byte,,,,,,,类似于byteBuffer
////			int l=br.read(readbuffer);//读取到内容的的长度
//			for (int i = 0; i < 10; i++)
//			{
////				System.out.println("read: " + br.read());//返回int型，得到的是内容对应的ASCII码数值
//				System.out.println("read: " + (char)br.read());//转换输出读取到的字符内容
////				System.out.println("readLine: " + br.readLine());//readLine读取一行的内容，返回string
//			}
//			br.close();//关闭一般都是调用最近的，这样br关掉时会调用input.close()
////			input.close();
//		} catch (FileNotFoundException e)
//		{
//			e.printStackTrace();
//		} catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//	}
}
