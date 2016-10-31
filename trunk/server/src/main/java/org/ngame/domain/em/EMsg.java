/**
 * 飘字类型
 */
package org.ngame.domain.em;

import com.baidu.bjf.remoting.protobuf.EnumReadable;

/**
 *
 * @author beykery
 */
public enum EMsg implements EnumReadable
{
  Form(1, "弹框"),
  Fly(2, "飘字"),
  Marquee(3, "跑马灯");

  public static EMsg parse(int value)
  {
    for (EMsg e : EMsg.values())
    {
      if (e.getValue() == value)
      {
        return e;
      }
    }
    throw new IllegalArgumentException();
  }

  public static String getName(int value)
  {
    EMsg e = parse(value);
    return e.getName();
  }

  private EMsg(int value, String name)
  {
    this.value = value;
    this.name = name;
  }
  private int value;
  private String name;

  public int getValue()
  {
    return value;
  }

  public void setValue(int value)
  {
    this.value = value;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  @Override
  public int value()
  {
    return this.value;
  }

  @Override
  public String toString()
  {
    return name;
  }
}
