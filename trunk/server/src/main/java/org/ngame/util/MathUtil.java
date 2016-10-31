package org.ngame.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MathUtil
{

  private static final Random r = new Random(System.currentTimeMillis());

  /**
   * 获取指定范围内的随机数[min,max)
   *
   * @param min 最小值
   * @param max 最大值
   * @return
   */
  public static int getRandomInt(int min, int max)
  {
    if (min >= max)
    {
      throw new IllegalArgumentException("范围参数非法:" + min + "/" + max);
    }
    int i = Math.abs(r.nextInt()) % (max - min);
    return min + i;
  }

  /**
   * 获取指定范围内的随机数[min,max)
   *
   * @param min 最小值
   * @param max 最大值
   * @return
   */
  public static long getRandomLong(long min, long max)
  {
    if (min >= max)
    {
      throw new IllegalArgumentException("范围参数非法:" + min + "/" + max);
    }
    long i = Math.abs(r.nextLong()) % (max - min);
    return min + i;
  }

  /**
   * 获取随机事件是否发生
   *
   * @param c 百分比，50代表50%的几率
   * @return
   */
  public static boolean isOccur(int c)
  {
    int i = Math.abs(r.nextInt() % 100);
    return i < c;
  }

  /**
   * 获取随机事件是否发生
   *
   * @param w
   * @param sum
   * @return
   */
  public static boolean isOccur(int w, int sum)
  {
    int i = Math.abs(r.nextInt() % sum);
    return i <= w;
  }

  /**
   * 获取随机事件是否发生
   *
   * @param c 几率
   * @return
   */
  public static boolean isOccur(float c)
  {
    float i = r.nextFloat();
    return i < c;
  }

  /**
   * 获取随机的几个整数
   *
   * @param s
   * @param e
   * @param w
   * @return
   */
  public static int[] getRandomInt(int s, int e, int w)
  {
    int[] result = new int[w];
    for (int i = 0; i < result.length; i++)
    {
      result[i] = s - 1;
    }
    Map<Integer, Integer> history = new HashMap<>();
    for (int i = 0; i < result.length; i++)
    {
      int temp = getRandomInt(s, e);
      while (history.get(temp) != null)
      {
        temp = getRandomInt(s, e);
      }
      result[i] = temp;
      history.put(temp, temp);
    }
    return result;
  }

  /**
   * 随机排列
   *
   * @param ids
   */
  public static void randomSort(LinkedHashSet<String> ids)
  {
    List<String> l = new LinkedList<>(ids);
    Collections.shuffle(l);
    ids.clear();
    ids.addAll(l);
  }

  /**
   * 计算随机index
   *
   * @param base
   * @param e
   * @return index
   */
  public static int getRandomIndex(int base, int... e)
  {
    int random = r.nextInt(base);
    int next = 0;
    for (int i = 0; i < e.length; i++)
    {
      next += e[i];
      if (random < next)
      {
        return i;
      }
    }
    return -1;
  }

  /**
   * 计算随机index
   *
   * @param e
   * @return index
   */
  public static int getRandomIndex(float... e)
  {
    float sum = sum(e);
    float random = r.nextFloat() * sum;
    float next = 0;
    for (int i = 0; i < e.length; i++)
    {
      next += e[i];
      if (random < next)
      {
        return i;
      }
    }
    return -1;
  }

  public static float nextFloat()
  {
    return r.nextFloat();
  }

  public static int nextInt(int range)
  {
    return r.nextInt(range);
  }

  public static void main(String... arg)
  {
    int[] aa = new int[]
    {
      1, 2, 3, 4, 5, 6, 7, 8, 9
    };
    List<Integer> l = new ArrayList<>();
    for (int i : aa)
    {
      l.add(i);
    }
    Collections.shuffle(l);
    Arrays.asList(aa);
    for (int i : l)
    {
      System.out.print(i + "  ");
    }
  }

  /**
   * 计算一个随机范围
   *
   * @param arr
   * @param c 元素数量
   * @param size 每元素大小
   * @return
   */
  public static int[] getRandomRegion(int[] arr, int c, int size)
  {
    int[] temp = new int[arr.length];
    System.arraycopy(arr, 0, temp, 0, arr.length);
    for (int i = 0; i < temp.length / size; i++)
    {
      int index = getRandomInt(1, temp.length / size) * size;
      for (int j = 0; j < size; j++)
      {
        int v = temp[index + j];
        temp[index + j] = temp[j];
        temp[j] = v;
      }
    }
    int[] rr = new int[c * size];
    System.arraycopy(temp, 0, rr, 0, rr.length);
    return rr;
  }

  /**
   * 计算和
   *
   * @param e
   * @return
   */
  private static float sum(float[] e)
  {
    float sum = 0;
    for (float f : e)
    {
      sum += f;
    }
    return sum;
  }

  private static int sum(int[] wh)
  {
    int sum = 0;
    for (int f : wh)
    {
      sum += f;
    }
    return sum;
  }

  /**
   * 取异或
   *
   * @param bs
   * @param ks
   */
  public static void xor(byte[] bs, byte[] ks)
  {
    if (ks != null && ks.length > 0)
    {
      for (int i = 0; i < bs.length; i++)
      {
        bs[i] = (byte) (bs[i] ^ ks[i % ks.length]);
      }
    }
  }

  /**
   * random index
   *
   * @param wh
   * @return
   */
  public static int getRandomIndex(int... wh)
  {
    int sum = sum(wh);
    float random = r.nextFloat() * sum;
    int next = 0;
    for (int i = 0; i < wh.length; i++)
    {
      next += wh[i];
      if (random < next)
      {
        return i;
      }
    }
    return -1;
  }

}
