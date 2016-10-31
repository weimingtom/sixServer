/**
 * 游戏中的定时调度任务
 */
package org.ngame.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import java.util.logging.Logger;

/**
 * 模板
 *
 * @author beykery
 */
public abstract class GameJob implements Job
{

  private static final Logger LOG = Logger.getLogger(GameJob.class.getName());
  protected String name;
  protected String group;
  protected JobExecutionContext context;

  public GameJob()
  {
  }

  /**
   * 构造
   *
   * @param name
   * @param group
   */
  public GameJob(String name, String group)
  {
    this.name = name;
    this.group = group;
  }

  public String getName()
  {
    return name;
  }

  public JobExecutionContext getContext()
  {
    return context;
  }

  public String getGroup()
  {
    return group;
  }

  /**
   * 任务内容
   *
   * @param context
   * @throws JobExecutionException
   */
  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException
  {
    this.context = context;
    this.name = context.getJobDetail().getKey().getName();
    this.group = context.getJobDetail().getKey().getGroup();
    this.process();
  }

  /**
   * 处理定时逻辑
   */
  protected abstract void process();

  /**
   * 立即结束(用户有可能手动结束掉,可立即计算收益)
   *
   * @param name
   * @param group
   * @param remainTime
   */
  public abstract void immediatelyProcess(String name, String group, long remainTime);
}
