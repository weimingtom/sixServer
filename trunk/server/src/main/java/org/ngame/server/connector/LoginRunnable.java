package org.ngame.server.connector;

import org.ngame.util.HttpUtils;

public class LoginRunnable implements Runnable
{

  private final long uid;
  private final int gid;
  private final int sid;
  private final String url;

  LoginRunnable(long uid, int gid, int sid, String url)
  {
    this.uid = uid;
    this.gid = gid;
    this.sid = sid;
    this.url = url;
  }

  @Override
  public void run()
  {
    try
    {
      byte[] content = HttpUtils.post(url, "uid=" + uid + "&gid=" + gid + "&sid=" + sid);
      System.out.println(new String(content));
    } catch (Exception e)
    {
      e.printStackTrace();
    }

  }
}
