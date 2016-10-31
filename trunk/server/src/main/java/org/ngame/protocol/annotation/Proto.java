/**
 * 协议
 */
package org.ngame.protocol.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记协议名
 *
 * @author beykery
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Proto
{

  int value() default 0;

  String description() default "";
}
