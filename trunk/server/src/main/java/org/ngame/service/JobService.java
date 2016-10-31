/**
 * 用来调度游戏中所有的定时服务
 */
package org.ngame.service;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ngame.job.GameJob;
import org.springframework.stereotype.Component;

/**
 * 调度服务
 *
 * @author beykery
 */
@Component
public class JobService
{

  private static final Logger LOG = Logger.getLogger(JobService.class.getName());
  private SchedulerFactory sf;
  private Scheduler sched;

  /**
   * 指定服务器上下文
   *
   */
  public JobService()
  {
    try
    {
      Properties p = new Properties();//系统属性
      p.load(new InputStreamReader(new FileInputStream(new File("./quartz.properties")), "UTF-8"));
      p.setProperty("org.quartz.scheduler.instanceName", "job");
      sf = new StdSchedulerFactory(p);
      sched = sf.getScheduler();
      sched.start();
    } catch (Exception e)
    {
      e.printStackTrace();
      LOG.log(Level.SEVERE, "无法启动定时服务");
    }
  }

  /**
   * 添加一个延时任务，使用默认的任务组名，触发器名，触发器组名
   *
   * @param job 任务
   * @param millSecond 延迟的秒数
   * @return
   */
  public boolean addJob(GameJob job, long millSecond)
  {
    JobDetail detail = JobBuilder.newJob(job.getClass()).withIdentity(job.getName(), job.getGroup()).build();
    Date startTime = new Date(System.currentTimeMillis() + millSecond);
    Trigger trigger = TriggerBuilder.newTrigger().withIdentity(job.getName(), job.getGroup()).startAt(startTime).build();
    try
    {
      sched.scheduleJob(detail, trigger);
      return true;
    } catch (SchedulerException ex)
    {
      ex.printStackTrace();
      LOG.log(Level.SEVERE, "无法添加延时任务：" + job.toString() + ":" + ex.getMessage());
    }
    return false;
  }

  /**
   * 修改滞后时间
   *
   * @param name
   * @param group
   * @param second
   * @return
   */
  public boolean modifyJob(String name, String group, int second)
  {
    try
    {
      JobDetail jd = sched.getJobDetail(new JobKey(name, group));
      if (jd != null)
      {
        Date d = this.getStartTime(name, group);
        d = new Date(d.getTime() + second * 1000);
        this.removeJob(name, group);
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group).startAt(d).build();
        this.sched.scheduleJob(jd, trigger);
      } else
      {
        return false;
      }
      return true;
    } catch (SchedulerException ex)
    {
      LOG.log(Level.WARNING, "添加job滞后时间失败");
    }
    return false;
  }

  public boolean modifyOrAddJob(GameJob job, int second)
  {
    String name = job.getName();
    String group = job.getGroup();
    try
    {
      JobDetail jd = sched.getJobDetail(new JobKey(name, group));
      Date d;
      Trigger trigger;
      if (jd != null)
      {
        d = this.getStartTime(name, group);
        d = new Date(d.getTime() + second * 1000);
        this.removeJob(name, group);
        trigger = TriggerBuilder.newTrigger().withIdentity(name, group).startAt(d).build();
      } else
      {
        jd = JobBuilder.newJob(job.getClass()).withIdentity(name, group).build();
        d = new Date(System.currentTimeMillis() + 1000L * second);
        trigger = TriggerBuilder.newTrigger().withIdentity(name, group).startAt(d).build();
      }
      sched.scheduleJob(jd, trigger);
      return true;
    } catch (SchedulerException ex)
    {
      LOG.log(Level.WARNING, "添加job滞后时间失败");
    }
    return false;
  }

  /**
   * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
   *
   * @param job 任务
   * @param cron
   * @return
   */
  public boolean addJob(GameJob job, String cron)
  {
    JobDetail detail = JobBuilder.newJob(job.getClass()).withIdentity(job.getName(), job.getGroup()).build();
    CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(job.getName(), job.getGroup()).withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
    try
    {
      sched.scheduleJob(detail, trigger);
      LOG.log(Level.INFO, "添加任务：" + detail.getKey().getGroup() + "  " + detail.getKey().getName());
      return true;
    } catch (SchedulerException ex)
    {
      LOG.log(Level.SEVERE, "无法添加定时任务：" + job.toString() + ":" + ex.getMessage());
    }
    return false;
  }

  /**
   * 添加一个循环任务
   *
   * @param job
   * @param repeatCount 循环次数
   * @param repeatInterval 循环间隔
   * @return
   */
  public boolean addJob(GameJob job, int repeatCount, int repeatInterval)
  {
    JobDetail detail = JobBuilder.newJob(job.getClass()).withIdentity(job.getName(), job.getGroup()).build();
    Date startTime = new Date(System.currentTimeMillis() + 1000L * repeatInterval);
    Trigger trigger = TriggerBuilder.newTrigger().withIdentity(job.getName(), job.getGroup()).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(repeatInterval).withRepeatCount(repeatCount)).startAt(startTime).build();
    try
    {
      sched.scheduleJob(detail, trigger);
      return true;
    } catch (SchedulerException ex)
    {
      LOG.log(Level.SEVERE, "无法添加循环任务：" + job.toString() + ":" + ex.getMessage());
    }
    return false;
  }

  /**
   * 修改一个任务的cron
   *
   * @param name
   * @param group
   * @param cron
   * @return
   */
  public boolean modifyCron(String name, String group, String cron)
  {
    try
    {
      Trigger trigger = sched.getTrigger(new TriggerKey(name, group));
      if (trigger != null)
      {
        CronTrigger ct = TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
        sched.rescheduleJob(new TriggerKey(name, group), ct);
      }
    } catch (Exception e)
    {
      LOG.log(Level.WARNING, "修改定时服务cron失败");
      return false;
    }
    return true;
  }

  /**
   * 获取下次触发时间
   *
   * @param name
   * @param group
   * @return
   */
  public Date getNextFireTime(String name, String group)
  {
    try
    {
      Trigger trigger = sched.getTrigger(new TriggerKey(name, group));
      if (trigger != null)
      {
        return trigger.getNextFireTime();
      }
    } catch (Exception e)
    {
      LOG.log(Level.WARNING, "获取下次触发时间失败");
    }
    return null;
  }

  /**
   * 获取开始启动的时间
   *
   * @param name
   * @param group
   * @return
   */
  public Date getStartTime(String name, String group)
  {
    try
    {
      Trigger trigger = sched.getTrigger(new TriggerKey(name, group));
      if (trigger != null)
      {
        return trigger.getStartTime();
      }
    } catch (Exception e)
    {
      LOG.log(Level.WARNING, "获取下次触发时间失败");
    }
    return null;
  }

  /**
   * 获取剩余时间
   *
   * @param name
   * @param group
   * @return
   */
  public long getRemainTime(String name, String group)
  {
    Date d = this.getNextFireTime(name, group);
    if (d != null)
    {
      long t = d.getTime() - System.currentTimeMillis();
      return t >= 0 ? t : 0;
    }
    return 0;
  }

  /**
   * 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
   *
   * @param name
   * @param group
   * @return
   */
  public boolean removeJob(String name, String group)
  {
    try
    {
      return sched.deleteJob(new JobKey(name, group));
    } catch (SchedulerException ex)
    {
      LOG.log(Level.WARNING, "无法移出指定的定时任务");
    }
    return false;
  }

  /**
   * 立即完成任务
   *
   * @param name
   * @param group
   * @return
   */
  public boolean completeJob(String name, String group)
  {
    long remainTime = getRemainTime(name, group);
    try
    {
      JobDetail jd = sched.getJobDetail(new JobKey(name, group));
      if (this.removeJob(name, group))
      {
        Class<? extends Job> jobClass = jd.getJobClass();
        GameJob job = (GameJob) jobClass.newInstance();
        job.immediatelyProcess(name, group, remainTime);
        return true;
      } else
      {
        LOG.log(Level.WARNING, "该JOB已经完成");
      }
    } catch (SchedulerException | InstantiationException | IllegalAccessException e)
    {
      LOG.log(Level.WARNING, e.getMessage());
    }
    return false;
  }

  /**
   * 是否还存在
   *
   * @param name
   * @param group
   * @return
   */
  public boolean exist(String name, String group)
  {
    boolean e = false;
    try
    {
      e = this.sched.getJobDetail(new JobKey(name, group)) != null;
    } catch (SchedulerException ex)
    {
      LOG.log(Level.WARNING, "查询调度是否存在异常");
    }
    return e;
  }
}
