package protocol;

import org.junit.Test;
import org.ngame.protocol.gen.Gen;

/**
 *
 * @author beykery
 */
public class TestGen
{

  @Test
  public  void test() throws Exception
  {
    String proto = Gen.genProto();
    Gen.genNet(proto);
    assert(true);
  }
}
