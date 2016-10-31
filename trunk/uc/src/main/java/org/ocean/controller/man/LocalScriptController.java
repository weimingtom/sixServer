/**
 * 本地脚本
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.ocean.annotation.Auth;
import org.ocean.service.ScriptEngine;
import org.ocean.util.FileUtil;
import org.ocean.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class LocalScriptController
{

  private final Logger LOG = Logger.getLogger(LocalScriptController.class);
  @Autowired
  private ScriptEngine engine;

  /**
   * 本地脚本页面
   *
   * @param session
   * @param model
   * @return
   */
  @Auth(note = "本地脚本列表")
  @RequestMapping(value = "localScriptList.man")
  public String localScriptList(HttpSession session, ModelMap model)
  {
    return "localScriptList";
  }

  /**
   * 本地所有的脚本
   *
   * @param request
   * @param session
   * @param response
   * @return
   * @throws UnsupportedEncodingException
   */
  @RequestMapping(value = "localScriptTree.man")
  public String localScriptTree(WebRequest request, HttpSession session, HttpServletResponse response) throws UnsupportedEncodingException
  {
    ServletContext servletContext = session.getServletContext();
    String path = servletContext.getRealPath("/") + "script/";
    JSONArray arr = new JSONArray();
    File root = new File(path);
    if (root.exists())
    {
      file2Tree(root, arr, true);
    }
    HttpUtil.response(response, arr.toString().getBytes("UTF-8"));
    return null;
  }

  /**
   * 获取脚本内容
   *
   * @param path
   * @param response
   * @return
   * @throws UnsupportedEncodingException
   */
  @RequestMapping(value = "getLocalScriptContent.man")
  public String getLocalScriptContent(@RequestParam(value = "path") String path, HttpServletResponse response) throws UnsupportedEncodingException
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

  @Auth(note = "本地脚本重命名")
  @RequestMapping(value = "renameLocalScriptFile.man")
  public String renameLocalScriptFile(@RequestParam(value = "path") String path,
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
   * 修改内容
   *
   * @param path
   * @param content
   * @param response
   * @return
   * @throws UnsupportedEncodingException
   */
  @Auth(note = "本地脚本修改")
  @RequestMapping(value = "editLocalScript.man")
  public String editLocalScript(@RequestParam(value = "localScriptPath") String path, @RequestParam(value = "localScriptContent") String content, HttpServletResponse response) throws UnsupportedEncodingException
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
  @RequestMapping(value = "addLocalScriptFile.man")
  public String addLocalScriptFile(@RequestParam(value = "path") String path,
          @RequestParam(value = "file") String file,
          HttpSession session,
          HttpServletResponse response) throws UnsupportedEncodingException, IOException
  {
    ServletContext servletContext = session.getServletContext();
    String root = servletContext.getRealPath("/") + "script";
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
   * 删除本地脚本文件
   *
   * @param path
   * @param session
   * @param response
   * @return
   * @throws UnsupportedEncodingException
   */
  @Auth(note = "本地脚本删除")
  @RequestMapping(value = "deleteLocalScriptFile.man")
  public String deleteLocalScriptFile(@RequestParam(value = "path") String path,
          HttpSession session,
          HttpServletResponse response) throws UnsupportedEncodingException
  {
    ServletContext servletContext = session.getServletContext();
    String root = servletContext.getRealPath("/") + "script/";
    root = root.replace('\\', '/');
    boolean r = false;
    Result re = null;
    path = path.replace('\\', '/');
    File f = new File(path);
    if (f.exists() && path.startsWith(root))
    {
      r = delete(f);
    }
    HttpUtil.response(response, (r ? "1" : (re == null ? "0" : re.getInfo())));
    return null;
  }

  /**
   * 执行脚本
   *
   * @param session
   * @param path
   * @param response
   * @return
   * @throws java.io.UnsupportedEncodingException
   */
  @Auth(note = "本地脚本执行")
  @RequestMapping(value = "runLocalScriptFile.man")
  public String runLocalScriptFile(HttpSession session,
          @RequestParam(value = "path") String path,
          HttpServletResponse response) throws UnsupportedEncodingException
  {
    File f = new File(path);
    if (f.exists() && f.isFile())
    {
      Object temp = this.engine.executeFile(f);
      String r = temp != null ? (engine.isPrimitive(temp) ? temp.toString() : JSONObject.toJSONString(temp)) : "成功";
      HttpUtil.response(response, r);
    } else
    {
      HttpUtil.response(response, "无法推送执行脚本，参数错误");
    }
    return null;
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

  @Auth(note = "本地脚本上传")
  @RequestMapping(value = "uploadLocalScriptFile.man")
  public String uploadLocalScriptFile(@RequestParam("localScriptFileContent") CommonsMultipartFile zipFile,
          HttpSession session,
          HttpServletResponse response) throws IOException
  {
    InputStream in = zipFile.getInputStream();
    ServletContext servletContext = session.getServletContext();
    String path = servletContext.getRealPath("/");
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
    return null;
  }

}
