package org.ngame.domain.em;

import com.baidu.bjf.remoting.protobuf.EnumReadable;

/**
 *
 * @author wangmanman
 *
 */
public enum EResType implements EnumReadable
{

  DIAMOND(1, "钻石"),
  GOLD(2, "黄金"),
  ENERGY(3, "体力"),
  EXP(4, "经验");

  public static EResType parse(int value)
  {
    for (EResType e : EResType.values())
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
    EResType e = parse(value);
    return e.getName();
  }

  private EResType(int value, String name)
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
