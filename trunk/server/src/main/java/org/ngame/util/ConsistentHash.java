package org.ngame.util;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 一致性hash
 *
 * @param <T>
 * @author beykery
 */
public final class ConsistentHash<T>
{

  private final Hashing hash;
  private final SortedMap<Long, T> circle = new TreeMap<>();
  private final Set<T> all = new LinkedHashSet<>();

  /**
   * 构造一个一致性hash
   *
   * @param hashFunction
   */
  public ConsistentHash(Hashing hashFunction)
  {
    this.hash = hashFunction;
  }

  /**
   * 添加节点
   *
   * @param node
   * @param load
   */
  public void add(T node, int load)
  {
    this.all.add(node);
    for (int i = 0; i < load; i++)
    {
      circle.put(hash.hash(node.toString() + i), node);
    }
  }

  public void remove(T node)
  {
    this.all.remove(node);
    for (int i = 0;; i++)
    {
      circle.remove(hash.hash(node.toString() + i));
    }
  }

  public T get(Object key)
  {
    if (circle.isEmpty())
    {
      return null;
    }
    long h = hash.hash(key.toString());
    if (!circle.containsKey(h))
    {
      SortedMap<Long, T> tailMap = circle.tailMap(h);
      h = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
    }
    return circle.get(h);
  }

  /**
   * 所有的节点
   *
   * @return
   */
  public Set<T> allNode()
  {
    return this.all;
  }
}
