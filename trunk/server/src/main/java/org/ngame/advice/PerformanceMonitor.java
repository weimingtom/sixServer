/**
 * 监控各个方法的性能
 */
package org.ngame.advice;

import java.io.File;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.ngame.annotation.RequestMapping;
import org.ngame.data.init.D_System;
import org.ngame.script.ScriptEngine;
import org.ngame.server.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author chensf
 */
@Component
@Aspect
public class PerformanceMonitor
{

  private static final Logger LOG = Logger.getLogger(PerformanceMonitor.class);
  @Autowired
  private ScriptEngine engine;
  private static final File systemFile = new File("./script/system/system.groovy");

  @Around("execution(* org.ngame.server.*.*.*(..))")
  public Object authLog(ProceedingJoinPoint pjp) throws Throwable
  {
    long start = System.currentTimeMillis();
    MethodSignature signature = (MethodSignature) pjp.getSignature();
    Method method = signature.getMethod();
    RequestMapping rm = method.getAnnotation(RequestMapping.class);
    Object r = null;
    if (rm != null)
    {
      if (rm.auth())
      {
        Context context = Context.current();
        if (context.getSessionId() != null)
        {
          r = pjp.proceed();
        } else
        {
          LOG.error("登录验证失败:" + rm.value());
        }
      } else
      {
        r = pjp.proceed();
      }
      int threshold = engine.get(D_System.class, systemFile, true).getRequest_process_threshold();
      long cost = System.currentTimeMillis() - start;
      if (cost >= threshold)
      {
        LOG.error("耗时操作:" + rm.value() + ":" + cost);
      }
    } else
    {
      r = pjp.proceed();
    }
    return r;
  }
}
