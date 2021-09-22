/**
 * 
 */
package org.iman.Heimdallr.config.annotations;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(PARAMETER)
/**
 * @author ey
 *
 */
public @interface JsonParam {

    String value() default "";
}
