/**
 * 聊天逻辑
 */
package org.ngame.server.chat;

import java.io.File;
import java.util.Collection;
import java.util.List;
import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import org.ngame.script.ScriptEngine;
import org.springframework.stereotype.Component;

/**
 *
 * @author Beykery
 */
@Component
public class ChatHandler
{

  public final Trie searcher;
  public final static int CHANNEL_PUBLIC = 0;
  public final static int CHANNEL_PARTY = 1;

  public ChatHandler()
  {
    ScriptEngine se = new ScriptEngine();
    Trie.TrieBuilder bu = Trie.builder().caseInsensitive();
    List<String> dirty = (List<String>) se.getProperty(new File("./script/dirty/dirtyWords.groovy"), "dirties", true);
    for (String d : dirty)
    {
      bu.addKeyword(d.trim());
    }
    searcher=bu.build();
  }

  /**
   * 过滤
   *
   * @param content
   * @return
   */
  public String replace(String content)
  {
    Collection<Emit> emits = searcher.parseText(content);
    if (!emits.isEmpty())
    {
      StringBuilder sb = new StringBuilder();
      int start = 0;
      for (Emit e : emits)
      {
        sb.append(content.substring(start, e.getStart()));
        sb.append("**");
        start = e.getEnd() + 1;
      }
      sb.append(content.substring(start));
      return sb.toString();
    }
    return content;
  }

  /**
   * 检查里面是否有关键字或者敏感词
   *
   * @param text
   * @return
   */
  public boolean contains(String text)
  {
    return !searcher.parseText(text).isEmpty();
  }
}
