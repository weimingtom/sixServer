/**
 * 表达文件名的构造
 */
package org.ngame.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author csf
 */
@Target(value =
{
  ElementType.FIELD
})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Name
{

  public int value() default 0;
}
