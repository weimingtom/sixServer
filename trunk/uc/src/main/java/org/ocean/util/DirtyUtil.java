/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ocean.util;

import java.io.File;

/**
 *
 * @author beykery
 */
public class DirtyUtil
{
  public static void main(String[] args)
  {
    File f=new File("d:/dirty.txt");
    String content=FileUtil.getFileContent(f);
    content=content.replaceAll("\r\n", ",");
    System.out.println(content);
    FileUtil.write(new File("d:/dirty1.txt"), content);
  }
}
