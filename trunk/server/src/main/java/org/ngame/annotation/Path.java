/**
 * 路径变量
 */
package org.ngame.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author sf
 */
@Target(value =
{
  ElementType.FIELD
})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Path
{

  public int value() default 0;
}
