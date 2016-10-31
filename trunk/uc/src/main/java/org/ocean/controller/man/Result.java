package org.ocean.controller.man;

/**
 * 结果
 */
public class Result
{

  private boolean success;
  private String info;

  public Result()
  {
  }

  public void setInfo(String info)
  {
    this.info = info;
  }

  public String getInfo()
  {
    return info;
  }

  public boolean isSuccess()
  {
    return success;
  }

  public void setSuccess(boolean success)
  {
    this.success = success;
  }

  @Override
  public String toString()
  {
    return success ? (info == null ? "成功" : info) : info;
  }

}
