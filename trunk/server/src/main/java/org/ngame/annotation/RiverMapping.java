/**
 * 标记river的请求
 */
package org.ngame.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value =
{
  ElementType.METHOD
})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface RiverMapping
{

  public String value() default "";

  public String method() default "";
}
