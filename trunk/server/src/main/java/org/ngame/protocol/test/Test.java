/**
 * 测试
 */
package org.ngame.protocol.test;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
//import org.ngame.domain.em.EColor;

import org.ngame.domain.em.EMsg;

/**
 *
 * @author beykery
 */
public class Test
{

  public static void main(String[] args) throws IOException
  {
    TestRequest r = new TestRequest();
    r.setC(EMsg.Fly);
    Codec codec = ProtobufProxy.create(TestRequest.class);
    byte[] content = codec.encode(r);
    write(content);
    codec = ProtobufProxy.create(TestRequest.class);
    TestRequest rr = (TestRequest) codec.decode(content);
    System.out.println(rr);
  }

  /**
   * 写入文件
   *
   * @param content
   */
  private static void write(byte[] content) throws FileNotFoundException, IOException
  {
    File f = new File("./data.d");
    f.delete();
    f.createNewFile();
    try (FileOutputStream fos = new FileOutputStream(f))
    {
      fos.write(content);
      fos.flush();
    }
  }
}
