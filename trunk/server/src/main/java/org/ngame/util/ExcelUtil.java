/**
 * 读取excel文件的工具
 */
package org.ngame.util;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author beykery
 */
public class ExcelUtil
{

  private static final Logger LOG = Logger.getLogger(ExcelUtil.class.getName());

  /**
   * 读取Excel文件的内容
   *
   * @param file 待读取的文件
   * @param sheetIndex
   * @param startLine
   * @param endLine
   * @param column
   * @return
   */
  public static String[][] readExcel(File file, int sheetIndex, int startLine, int endLine, int column)
  {
    String[][] r = null;
    try
    {
      XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
      XSSFSheet sheet = wb.getSheetAt(sheetIndex);
      int rows = endLine;
      r = new String[rows - startLine + 1][];
      for (int i = 0; i < r.length; i++)
      {
        XSSFRow row = sheet.getRow(i + startLine);

        r[i] = new String[column];
        for (int j = 0; j < r[i].length; j++)
        {
          XSSFCell cell = row.getCell(j);

          if (cell != null)
          {
            String raw = cell.toString().trim();
            // boolean isint = isInt(raw);
            // r[i][j] = isint ? parseInt(raw) + "" : raw;
            r[i][j] = raw;
          }
        }
      }
    } catch (Exception e)
    {
      e.printStackTrace();
      LOG.log(Level.WARNING, "读取excel文档失败");
    }
    return r;
  }

  /**
   * 读取Excel文件的内容
   *
   * @param file 待读取的文件
   * @param sheetIndex
   * @param startLine
   * @return
   */
  public static List<List<String>> readExcelInfo(File file, int sheetIndex, int startLine)
  {
    List<List<String>> content = new ArrayList<>();
    try
    {
      XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
      XSSFSheet sheet = wb.getSheetAt(sheetIndex);
      int len = 0;
      XSSFRow header = sheet.getRow(startLine);
      for (int j = 0;; j++)
      {
        XSSFCell cell = header.getCell(j);
        String item = cell == null ? null : cell.toString().trim();
        if (item == null || item.isEmpty())
        {
          len = j;
          break;
        }
      }
      for (int i = 0;; i++)
      {
        XSSFRow row = sheet.getRow(i + startLine);
        if (row != null)
        {
          List<String> line = new ArrayList<>();
          for (int j = 0; j < len; j++)
          {
            XSSFCell cell = row.getCell(j);
            String item = cell == null ? null : cell.toString().trim();
            if (j == 0 && (item == null || item.isEmpty()))
            {
              break;
            }
            line.add(item);
          }
          if (!line.isEmpty())
          {
            content.add(line);
          } else
          {
            break;
          }
        } else
        {
          break;
        }
      }
    } catch (Exception e)
    {
      e.printStackTrace();
      LOG.log(Level.WARNING, "读取excel文档失败");
    }
    return content;
  }

  public static List<Map<String, String>> excel(File file, int sheetIndex, int startLine, int endLine, int column)
  {
    List<Map<String, String>> list = new ArrayList<>();
    String[][] content = readExcel(file, sheetIndex, startLine, endLine, column);
    for (int i = 1; i < content.length; i++)
    {
      Map<String, String> map = new HashMap<>();
      for (int j = 0; j < content[i].length; j++)
      {
        map.put(content[0][j], content[i][j]);
      }
      list.add(map);
    }
    return list;
  }

  /**
   * @param file
   * @param sheetIndex
   * @param startLine
   * @return
   */
  public static List<Map<String, String>> excelMap(File file, int sheetIndex, int startLine)
  {
    List<Map<String, String>> list = new ArrayList<>();
    List<List<String>> content = readExcelInfo(file, sheetIndex, startLine);
    for (int i = 1; i < content.size(); i++)
    {
      Map<String, String> map = new LinkedHashMap<>();
      for (int j = 0; j < content.get(i).size(); j++)
      {
        map.put(content.get(0).get(j), content.get(i).get(j));
      }
      list.add(map);
    }
    return list;
  }

  /**
   * 转换成int
   *
   * @param s
   * @return
   */
  private static int parseInt(String s)
  {
    return (int) Float.parseFloat(s);
  }

  /**
   * 是否可以转换为整数
   *
   * @param s
   * @return s
   */
  private static boolean isInt(String s)
  {
    try
    {
      parseInt(s);
      return true;
    } catch (Exception e)
    {
      return false;
    }
  }

  /**
   * 将数转为26进制
   *
   * @param num
   * @return
   */
  public static String to26(int num)
  {
    StringBuilder str = new StringBuilder("");
    String digths = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    Stack<Character> s = new Stack<>();
    do
    {
      s.push(digths.charAt(num % 26));
      num /= 26;
    } while ((num != 0));
    while (!s.isEmpty())
    {
      str.append(s.pop());
    }
    return str.toString();
  }

  /**
   * 精度控制
   *
   * @param value
   * @param scale
   * @param roundingMode
   * @return
   */
  public static double round(double value, int scale, int roundingMode)
  {
    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(scale, roundingMode);
    double d = bd.doubleValue();
    return d;
  }

  public static void main(String[] args)
  {
    for (int i = 0; i < 100; i++)
    {
      System.out.println(to26(i));
    }
  }
}
