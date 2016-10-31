package org.ngame.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import sun.nio.cs.ThreadLocalCoders;

/**
 * http请求
 *
 * @author beykery
 */
public class HttpUtils
{

  private static final Logger LOG = Logger.getLogger(HttpUtils.class);
  private final static char[] hexDigits =
  {
    '0', '1', '2', '3', '4', '5', '6', '7',
    '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
  };

  /**
   * 请求参数的转换
   *
   * @param s
   * @return
   */
  public static Map<String, String[]> parseQuery(String s)
  {
    boolean b = false;
    Map<String, List<String>> params = new LinkedHashMap<>();
    String name = null;
    int pos = 0; // Beginning of the unprocessed region
    int i;       // End of the unprocessed region
    char c;  // Current character
    for (i = 0; i < s.length(); i++)
    {
      c = s.charAt(i);
      if (c == '=' && name == null)
      {
        if (pos != i)
        {
          name = decodeComponent(s.substring(pos, i));
        }
        pos = i + 1;
        // http://www.w3.org/TR/html401/appendix/notes.html#h-B.2.2
      } else if (c == '&' || c == ';')
      {
        if (name == null && pos != i)
        {
          // We haven't seen an `=' so far but moved forward.
          // Must be a param of the form '&a&' so add it with
          // an empty value.
          if (!addParam(params, decodeComponent(s.substring(pos, i)), ""))
          {
            b = true;
            break;
          }
        } else if (name != null)
        {
          if (!addParam(params, name, decodeComponent(s.substring(pos, i))))
          {
            b = true;
            break;
          }
          name = null;
        }
        pos = i + 1;
      }
    }
    if (!b)
    {
      if (pos != i)
      {  // Are there characters we haven't dealt with?
        if (name == null)
        {     // Yes and we haven't seen any `='.
          addParam(params, decodeComponent(s.substring(pos, i)), "");
        } else
        {                // Yes and this must be the last value.
          addParam(params, name, decodeComponent(s.substring(pos, i)));
        }
      } else if (name != null)
      {  // Have we seen a name without value?
        addParam(params, name, "");
      }
    }
    Map<String, String[]> result = new HashMap<>();
    for (Map.Entry<String, List<String>> en : params.entrySet())
    {
      String[] vs = new String[en.getValue().size()];
      en.getValue().toArray(vs);
      result.put(en.getKey(), vs);
    }
    return result;
  }

  private static boolean addParam(Map<String, List<String>> params, String name, String value)
  {
    List<String> values = params.get(name);
    if (values == null)
    {
      values = new ArrayList<>(1);  // Often there's only 1 value.
      params.put(name, values);
    }
    values.add(value);
    return true;
  }

  private static String decodeComponent(final String s)
  {
    if (s == null)
    {
      return "";
    }
    final int size = s.length();
    boolean modified = false;
    for (int i = 0; i < size; i++)
    {
      final char c = s.charAt(i);
      if (c == '%' || c == '+')
      {
        modified = true;
        break;
      }
    }
    if (!modified)
    {
      return s;
    }
    final byte[] buf = new byte[size];
    int pos = 0;  // position in `buf'.
    for (int i = 0; i < size; i++)
    {
      char c = s.charAt(i);
      switch (c)
      {
        case '+':
          buf[pos++] = ' ';  // "+" -> " "
          break;
        case '%':
          if (i == size - 1)
          {
            throw new IllegalArgumentException("unterminated escape"
                    + " sequence at end of string: " + s);
          }
          c = s.charAt(++i);
          if (c == '%')
          {
            buf[pos++] = '%';  // "%%" -> "%"
            break;
          }
          if (i == size - 1)
          {
            throw new IllegalArgumentException("partial escape"
                    + " sequence at end of string: " + s);
          }
          c = decodeHexNibble(c);
          final char c2 = decodeHexNibble(s.charAt(++i));
          if (c == Character.MAX_VALUE || c2 == Character.MAX_VALUE)
          {
            throw new IllegalArgumentException(
                    "invalid escape sequence `%" + s.charAt(i - 1)
                    + s.charAt(i) + "' at index " + (i - 2)
                    + " of: " + s);
          }
          c = (char) (c * 16 + c2);
        // Fall through.
        default:
          buf[pos++] = (byte) c;
          break;
      }
    }
    return new String(buf, 0, pos);
  }

  /**
   * decodeHexNibble
   *
   * @param c
   * @return
   */
  private static char decodeHexNibble(final char c)
  {
    if ('0' <= c && c <= '9')
    {
      return (char) (c - '0');
    } else if ('a' <= c && c <= 'f')
    {
      return (char) (c - 'a' + 10);
    } else if ('A' <= c && c <= 'F')
    {
      return (char) (c - 'A' + 10);
    } else
    {
      return Character.MAX_VALUE;
    }
  }

  /**
   * 读取流
   *
   * @param u
   * @param post
   * @return
   */
  public static byte[] post(String u, byte[] post)
  {
    byte[] data = null;
    HttpURLConnection con = null;
    try
    {
      URL url = new URL(u);
      con = (HttpURLConnection) url.openConnection();
      con.setReadTimeout(300000);
      con.setConnectTimeout(300000);
      con.setRequestMethod("POST");
      con.setDoOutput(true);
      con.setDoInput(true);
//      int code = con.getResponseCode();
//      System.out.println(code);
      DataOutputStream dos = new DataOutputStream(con.getOutputStream());
      dos.write(post);
      dos.flush();
      DataInputStream dis = new DataInputStream(con.getInputStream());
      data = new byte[con.getContentLength()];
      dis.readFully(data);
    } catch (Exception e)
    {
      e.printStackTrace();
      LOG.error(e.getMessage());
    } finally
    {
      if (con != null)
      {
        con.disconnect();
      }
    }
    return data;
  }

  /**
   * post
   *
   * @param u
   * @param post
   * @return
   */
  public static byte[] post(String u, String post)
  {
    return post(u, post.getBytes());
  }

  /**
   * get请求
   *
   * @param u
   * @param query
   * @return
   */
  public static byte[] get(String u, Map<String, Object> query)
  {
    byte[] data = null;
    HttpURLConnection con = null;
    try
    {
      StringBuilder sb = new StringBuilder(u);
      if (!query.isEmpty())
      {
        sb.append("?");
        for (Map.Entry<String, Object> en : query.entrySet())
        {
          sb.append(en.getKey()).append("=").append(en.getValue().toString()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
      }
      URL url = new URL(sb.toString());
      con = (HttpURLConnection) url.openConnection();
      con.setReadTimeout(30000);
      con.setConnectTimeout(30000);
      con.setRequestMethod("GET");
      con.setDoOutput(true);
      con.setDoInput(true);
      DataInputStream dis = new DataInputStream(con.getInputStream());
      data = new byte[con.getContentLength()];
      dis.readFully(data);
    } catch (Exception e)
    {
      e.printStackTrace();
      LOG.error(e.getMessage());
    } finally
    {
      if (con != null)
      {
        con.disconnect();
      }
    }
    return data;
  }

  /**
   * encode
   *
   * @param s
   * @return
   */
  public static String encode(String s)
  {
    int n = s.length();
    if (n == 0)
    {
      return s;
    }
    // First check whether we actually need to encode
    for (int i = 0;;)
    {
      if (s.charAt(i) >= '\u0080')
      {
        break;
      }
      if (++i >= n)
      {
        return s;
      }
    }
    String ns = Normalizer.normalize(s, Normalizer.Form.NFC);
    ByteBuffer bb = null;
    try
    {
      bb = ThreadLocalCoders.encoderFor("UTF-8").encode(CharBuffer.wrap(ns));
    } catch (CharacterCodingException x)
    {
      assert false;
    }
    StringBuffer sb = new StringBuffer();
    while (bb.hasRemaining())
    {
      int b = bb.get() & 0xff;
      if (b >= 0x80)
      {
        appendEscape(sb, (byte) b);
      } else
      {
        sb.append((char) b);
      }
    }
    return sb.toString();
  }

  /**
   * decode
   *
   * @param s
   * @return
   */
  public static String decode(String s)
  {
    if (s == null)
    {
      return s;
    }
    int n = s.length();
    if (n == 0)
    {
      return s;
    }
    if (s.indexOf('%') < 0)
    {
      return s;
    }

    StringBuilder sb = new StringBuilder(n);
    ByteBuffer bb = ByteBuffer.allocate(n);
    CharBuffer cb = CharBuffer.allocate(n);
    CharsetDecoder dec = ThreadLocalCoders.decoderFor("UTF-8")
            .onMalformedInput(CodingErrorAction.REPLACE)
            .onUnmappableCharacter(CodingErrorAction.REPLACE);

    // This is not horribly efficient, but it will do for now
    char c = s.charAt(0);
    boolean betweenBrackets = false;

    for (int i = 0; i < n;)
    {
      assert c == s.charAt(i);    // Loop invariant
      if (c == '[')
      {
        betweenBrackets = true;
      } else if (betweenBrackets && c == ']')
      {
        betweenBrackets = false;
      }
      if (c != '%' || betweenBrackets)
      {
        sb.append(c);
        if (++i >= n)
        {
          break;
        }
        c = s.charAt(i);
        continue;
      }
      bb.clear();
      int ui = i;
      for (;;)
      {
        assert (n - i >= 2);
        bb.put(decode(s.charAt(++i), s.charAt(++i)));
        if (++i >= n)
        {
          break;
        }
        c = s.charAt(i);
        if (c != '%')
        {
          break;
        }
      }
      bb.flip();
      cb.clear();
      dec.reset();
      CoderResult cr = dec.decode(bb, cb, true);
      assert cr.isUnderflow();
      cr = dec.flush(cb);
      assert cr.isUnderflow();
      sb.append(cb.flip().toString());
    }
    return sb.toString();
  }

  private static void appendEscape(StringBuffer sb, byte b)
  {
    sb.append('%');
    sb.append(hexDigits[(b >> 4) & 0x0f]);
    sb.append(hexDigits[(b) & 0x0f]);
  }

  private static int decode(char c)
  {
    if ((c >= '0') && (c <= '9'))
    {
      return c - '0';
    }
    if ((c >= 'a') && (c <= 'f'))
    {
      return c - 'a' + 10;
    }
    if ((c >= 'A') && (c <= 'F'))
    {
      return c - 'A' + 10;
    }
    assert false;
    return -1;
  }

  private static byte decode(char c1, char c2)
  {
    return (byte) (((decode(c1) & 0xf) << 4) | ((decode(c2) & 0xf)));
  }

  /**
   * 读取流
   *
   * @param is
   * @return
   */
  public static byte[] content(InputStream is)
  {
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int n;
      while ((n = is.read(buffer)) > 0)
      {
        baos.write(buffer, 0, n);
      }
      return baos.toByteArray();
    } catch (Exception e)
    {
      LOG.error("无法读取流中数据");
    }
    return null;
  }

  private HttpUtils()
  {
  }
}
