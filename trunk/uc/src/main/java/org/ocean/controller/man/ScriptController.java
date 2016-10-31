/**
 * 脚本
 */
package org.ocean.controller.man;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.ocean.annotation.Auth;
import org.ocean.dao.BaseDao;
import org.ocean.domain.Game;
import org.ocean.domain.GameServer;
import org.ocean.service.GameService;
import org.ocean.service.ScriptService;
import org.ocean.util.FileUtil;
import org.ocean.util.HttpUtil;
import org.ocean.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * 用来编辑、执行、推送脚本
 *
 * @author beykery
 */
@Controller
@RequestMapping(value = "/")
public class ScriptController
{

  private final Logger LOG = Logger.getLogger(ScriptController.class);
  @Autowired
  private BaseDao dao;
  @Autowired
  private ScriptService scriptSerivce;
  @Autowired
  private GameService gameService;

  /**
   * 脚本页面
   *
   * @param session
   * @param model
   * @return
   */
  @Auth(note = "脚本列表")
  @RequestMapping(value = "scriptList.man", method = RequestMethod.GET)
  public String scriptList(HttpSession session, ModelMap model)
  {
    Long tarGame = (Long) session.getAttribute("curGame");
    if (tarGame == null)
    {
      model.addAttribute("error", "尚未选中任何游戏");
    }
    return "scriptList";
  }

  @RequestMapping(value = "scriptTree.man")
  public String scriptTree(WebRequest request, HttpSession session, HttpServletResponse response) throws UnsupportedEncodingException
  {
    Long tarGame = (Long) session.getAttribute("curGame");
    Map<String, String[]> ps = request.getParameterMap();
    String path = ps.containsKey("id") ? ps.get("id")[0] : null;
    if (path == null && tarGame != null)
    {
      Game g = dao.get(Game.class, tarGame);
      ServletContext servletContext = session.getServletContext();
      path = servletContext.getRealPath("/") + g.getName() + "/script/";
      JSONArray arr = new JSONArray();
      File root = new File(path);
      if (root.exists())
      {
        file2Tree(root, arr, true);
      }
      HttpUtil.response(response, arr.toString().getBytes("UTF-8"));
      return null;
    } else//不在处理
    {
      HttpUtil.response(response, "[]".getBytes("UTF-8"));
      return null;
    }
  }

  /**
   * 获取脚本内容
   *
   * @param path
   * @param response
   * @return
   * @throws UnsupportedEncodingException
   */
  @RequestMapping(value = "getScriptContent.man")
  public String getScriptContent(@RequestParam(value = "path") String path, HttpServletResponse response) throws UnsupportedEncodingException
  {
    File f = new File(path);
    if (f.exists())
    {
      String content = FileUtil.getFileContent(f, "UTF-8");
      HttpUtil.response(response, content.getBytes("UTF-8"));
    } else
    {
      HttpUtil.response(response, "不存在.".getBytes("UTF-8"));
    }
    return null;
  }

  @Auth(note = "脚本重命名")
  @RequestMapping(value = "renameScriptFile.man")
  public String renameScriptFile(@RequestParam(value = "path") String path,
          @RequestParam(value = "to") String to,
          HttpServletResponse response) throws UnsupportedEncodingException
  {
    File f = new File(path);
    if (f.exists())
    {
      boolean r = f.renameTo(new File(f.getParentFile(), to));
      HttpUtil.response(response, (r ? "1" : "无法重命名").getBytes("UTF-8"));
    } else
    {
      HttpUtil.response(response, "文件不存在.".getBytes("UTF-8"));
    }
    return null;
  }

  /**
   * 修改脚本内容
   *
   * @param path
   * @param content
   * @param response
   * @return
   * @throws UnsupportedEncodingException
   */
  @Auth(note = "脚本修改")
  @RequestMapping(value = "editScript.man")
  public String editScript(@RequestParam(value = "scriptPath") String path, @RequestParam(value = "scriptContent") String content, HttpServletResponse response) throws UnsupportedEncodingException
  {
    File f = new File(path);
    if (f.exists())
    {
      FileUtil.write(f, content);
      HttpUtil.response(response, "1".getBytes("UTF-8"));
    } else
    {
      HttpUtil.response(response, "0".getBytes("UTF-8"));
    }
    return null;
  }

  /**
   * 创建文件
   *
   * @param path
   * @param file
   * @param session
   * @param response
   * @return
   * @throws java.io.UnsupportedEncodingException
   */
  @Auth(note = "脚本新增")
  @RequestMapping(value = "addScriptFile.man")
  public String addScriptFile(@RequestParam(value = "path") String path,
          @RequestParam(value = "file") String file,
          HttpSession session,
          HttpServletResponse response) throws UnsupportedEncodingException, IOException
  {
    Long tarGame = (Long) session.getAttribute("curGame");
    Game g = dao.get(Game.class, tarGame);
    ServletContext servletContext = session.getServletContext();
    String root = servletContext.getRealPath("/") + g.getName() + "/script";
    root = root.replace('\\', '/');
    JSONObject r = new JSONObject();
    File d = new File(path);
    if (d.exists() && d.isDirectory() && path.startsWith(root))
    {
      JSONObject data = new JSONObject();
      r.put("data", data);
      boolean c;
      File dest = new File(d, file);
      if (dest.getName().endsWith(".groovy"))//脚本文件
      {
        c = dest.createNewFile();
        data.put("file", true);
      } else//文件夹
      {
        c = dest.mkdirs();
        data.put("file", false);
        data.put("state", "open");
        data.put("children", new JSONArray().toString());
      }
      data.put("id", dest.getAbsolutePath());
      data.put("text", dest.getName());
      r.put("result", c ? 1 : 0);
    } else
    {
      r.put("result", 0);
    }
    HttpUtil.response(response, r.toString().getBytes("UTF-8"));
    return null;
  }

  /**
   * 删除脚本文件
   *
   * @param path
   * @param remote
   * @param session
   * @param response
   * @return
   * @throws UnsupportedEncodingException
   */
  @Auth(note = "脚本删除")
  @RequestMapping(value = "deleteScriptFile.man")
  public String deleteScriptFile(@RequestParam(value = "path") String path,
          @RequestParam(value = "river") boolean remote,
          HttpSession session,
          HttpServletResponse response) throws UnsupportedEncodingException
  {
    Long tarGame = (Long) session.getAttribute("curGame");
    Game g = dao.get(Game.class, tarGame);
    ServletContext servletContext = session.getServletContext();
    String root = servletContext.getRealPath("/") + g.getName() + "/script/";
    root = root.replace('\\', '/');
    boolean r = false;
    Result re = null;
    path = path.replace('\\', '/');
    File f = new File(path);
    if (f.exists() && path.startsWith(root))
    {
      if (remote)//删除所有服务器上的脚本文件
      {
        Collection<GameServer> gs = scriptSerivce.getServers(tarGame, new int[]
        {
          -1
        });
        re = deleteRemoteScript(f, gs);
      } else
      {
        r = delete(f);
      }
    }
    HttpUtil.response(response, (r ? "1" : (re == null ? "0" : re.getInfo())));
    return null;
  }

  /**
   * 推送脚本到服务器
   *
   * @param session
   * @param path
   * @param sid
   * @param response
   * @return
   * @throws java.io.UnsupportedEncodingException
   */
  @Auth(note = "脚本推送")
  @RequestMapping(value = "pushScript.man")
  public String pushScript(HttpSession session,
          @RequestParam(value = "path") String path,
          @RequestParam(value = "sid") String sid,
          HttpServletResponse response) throws UnsupportedEncodingException
  {
    Long tarGame = (Long) session.getAttribute("curGame");
    File f = new File(path);
    int[] ss = splitSid(sid);
    if (f.exists() && ss != null && tarGame != null)
    {
      List<GameServer> gs = scriptSerivce.getServers(tarGame, ss);
      if (gs != null && !gs.isEmpty())
      {
        Map<File, List<GameServer>> fails = new HashMap<>();
        push(f, gs, fails);
        HttpUtil.response(response, fails.isEmpty() ? "推送成功" : errorResult(fails));
      } else
      {
        HttpUtil.response(response, "无法推送脚本，参数错误".getBytes("UTF-8"));
      }
    } else
    {
      HttpUtil.response(response, "无法推送脚本，参数错误".getBytes("UTF-8"));
    }
    return null;
  }

  /**
   * 错误结果汇总
   *
   * @param fails
   * @return
   */
  private String errorResult(Map<File, List<GameServer>> fails)
  {
    StringBuilder sb = new StringBuilder();
    sb.append("失败的推送如下:\n");
    for (Map.Entry<File, List<GameServer>> en : fails.entrySet())
    {
      sb.append("\t").append(en.getKey().getAbsolutePath()).append(" : ").append("\n");
      for (GameServer gs : en.getValue())
      {
        sb.append("\t\t").append(gs.getName()).append("\n");
      }
    }
    return sb.toString();
  }

  /**
   * 推送并执行脚本（只能执行一个脚本不可执行多个）
   *
   * @param session
   * @param path
   * @param sid
   * @param response
   * @return
   * @throws java.io.UnsupportedEncodingException
   */
  @Auth(note = "脚本执行")
  @RequestMapping(value = "pushAndExeScript.man")
  public String pushAndExeScript(HttpSession session,
          @RequestParam(value = "path") String path,
          @RequestParam(value = "sid") String sid,
          HttpServletResponse response) throws UnsupportedEncodingException
  {
    Long tarGame = (Long) session.getAttribute("curGame");
    File f = new File(path);
    int ss = Integer.parseInt(sid);
    if (f.exists() && f.isFile() && tarGame != null)
    {
      List<GameServer> gs = scriptSerivce.getServers(tarGame, new int[]
      {
        ss
      });
      if (gs != null && !gs.isEmpty())
      {
        List<Result> r = pushOne(f, gs, true);
        HttpUtil.response(response, totalResult(r, gs));
      } else
      {
        HttpUtil.response(response, "无法推送执行脚本，参数错误");
      }
    } else
    {
      HttpUtil.response(response, "无法推送执行脚本，参数错误");
    }
    return null;
  }

  /**
   * 汇总结果
   *
   * @param r
   * @return
   */
  private String totalResult(List<Result> rs, List<GameServer> gs)
  {
    StringBuilder sb = new StringBuilder();
    sb.append("结果：\n");
    for (int i = 0; i < rs.size(); i++)
    {
      sb.append("\t").append(gs.get(i).getName()).append("--").append(rs.get(i).toString()).append("\n");
    }
    return sb.toString();
  }

  /**
   * 搜索file下所有文件，添加到tree里面
   *
   * @param file
   * @return
   */
  private void file2Tree(File file, JSONArray arr, boolean root)
  {
    JSONObject item = new JSONObject();
    arr.add(item);
    String path = file.getAbsolutePath();
    path = path.replace('\\', '/');
    item.put("id", path);
    item.put("text", file.getName());
    if (file.isDirectory())
    {
      item.put("file", false);
      item.put("state", root ? "open" : "closed");
      File[] fs = file.listFiles();
      JSONArray temp = new JSONArray();
      item.put("children", temp);
      for (File f : fs)
      {
        file2Tree(f, temp, false);
      }
    } else
    {
      item.put("file", true);
    }
  }

  /**
   * 分割为int数组
   *
   * @param sid
   * @return
   */
  private int[] splitSid(String sid)
  {
    try
    {
      String[] ss = StringUtil.split(sid, ',');
      int[] r = new int[ss.length];
      for (int i = 0; i < ss.length; i++)
      {
        r[i] = Integer.parseInt(ss[i].trim());
      }
      return r;
    } catch (Exception e)
    {
      return null;
    }
  }

  /**
   * 推送
   *
   * @param f
   * @param ss
   * @return
   */
  private void push(File f, List<GameServer> gs, Map<File, List<GameServer>> fails)
  {
    if (f.isFile())
    {
      List<Result> r = pushOne(f, gs, false);
      for (int i = 0; i < r.size(); i++)
      {
        Result item = r.get(i);
        if (!item.isSuccess())
        {
          List<GameServer> fgs = fails.get(f);
          if (fgs == null)
          {
            fgs = new ArrayList<>();
            fails.put(f, fgs);
          }
          fgs.add(gs.get(i));
        }
      }
    } else
    {
      File[] fs = f.listFiles();
      for (File item : fs)
      {
        push(item, gs, fails);
      }
    }
  }

  /**
   * 推送一个脚本文件
   *
   * @param f
   * @param gs
   */
  private List<Result> pushOne(File f, Collection<GameServer> gs, boolean exe)
  {
    List<Result> rl = new ArrayList<>();
    for (GameServer s : gs)
    {
      String addr = s.getAddress();
      int index = addr.indexOf(":");
      String ip = addr.substring(0, index);
      String url = "http://" + ip + ":" + s.getRiverPort() + "/river/script/pushScript.pf";
      if (exe)
      {
        url = "http://" + ip + ":" + s.getRiverPort() + "/river/script/exeScript.pf";
      }
      String content = FileUtil.getFileContent(f);
      String path = f.getAbsolutePath();
      path = path.replace('\\', '/');
      int idx = path.indexOf("/script");
      path = "." + path.substring(idx);
      StringBuilder sb = new StringBuilder();
      sb.append(path).append("|");
      sb.append(content);
      try
      {
        byte[] r = HttpUtil.post(url, sb.toString().getBytes("UTF-8"), dao.get(Game.class, s.getGameId()).getDesKey());
        String ss = new String(r, "utf-8");
        Result re = JSONObject.parseObject(ss, Result.class);
        rl.add(re);
      } catch (Exception e)
      {
        e.printStackTrace();
        LOG.error("未知错误" + e.getMessage());
        rl.add(null);
      }
    }
    return rl;
  }

  /**
   * 删除远程游戏服务器上的脚本文件
   *
   * @param f
   * @param gs
   */
  private Result deleteRemoteScript(File f, Collection<GameServer> gs)
  {
    for (GameServer s : gs)
    {
      String addr = s.getAddress();
      int index = addr.indexOf(":");
      String ip = addr.substring(0, index);
      String url = "http://" + ip + ":" + s.getRiverPort() + "/river/script/deleteScript.pf";//删除脚本的url
      String path = f.getAbsolutePath();
      path = path.replace('\\', '/');
      int idx = path.indexOf("/script");
      path = "." + path.substring(idx);//服务器上的相对路径
      StringBuilder sb = new StringBuilder();
      sb.append("file=").append(path);
      try
      {
        byte[] r = HttpUtil.post(url, sb.toString().getBytes("UTF-8"), dao.get(Game.class, s.getGameId()).getDesKey());
        Result re = JSONObject.parseObject(new String(r, "utf-8"), Result.class);
        return re;
      } catch (Exception e)
      {
        LOG.error("删除远程脚本:" + e.getMessage());
        return null;
      }
    }
    return null;
  }

  /**
   * 删除文件或文件夹
   *
   * @param f
   * @return
   */
  private boolean delete(File f)
  {
    if (f.isFile())
    {
      try
      {
        Files.deleteIfExists(f.toPath());
      } catch (Exception e)
      {
        LOG.error("无法删除文件：" + f);
        return false;
      }
      return true;
    } else
    {
      File[] fs = f.listFiles();
      for (File item : fs)
      {
        delete(item);
      }
      return f.delete();
    }
  }

  @Auth(note = "脚本上传")
  @RequestMapping(value = "uploadScriptFile.man")
  public String uploadScriptFile(@RequestParam("scriptFileContent") CommonsMultipartFile zipFile,
          HttpSession session,
          HttpServletResponse response) throws IOException
  {
    Long tarGame = (Long) session.getAttribute("curGame");
    if (tarGame == null)
    {
      HttpUtil.response(response, "未选择任何游戏".getBytes("utf-8"));
    } else
    {
      Game g = this.gameService.getGameById(tarGame);
      InputStream in = zipFile.getInputStream();
      ServletContext servletContext = session.getServletContext();
      String path = servletContext.getRealPath("/") + g.getName() + "/";
      ZipInputStream zin = new ZipInputStream(in, Charset.forName("GBK"));
      ZipEntry en;
      try
      {
        while ((en = zin.getNextEntry()) != null)
        {
          if (!en.isDirectory())
          {
            File d = new File(path + en.getName());
            d.getParentFile().mkdirs();
            FileUtil.write(d, zin);
          }
        }
      } catch (Exception e)
      {
        LOG.error("解压缩失败");
        HttpUtil.response(response, "解压缩失败".getBytes("utf-8"));
        return null;
      }
      HttpUtil.response(response, "1".getBytes("utf-8"));
    }
    return null;
  }

}
