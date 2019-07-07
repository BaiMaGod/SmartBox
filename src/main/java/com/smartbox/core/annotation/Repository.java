package com.smartbox.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Repository {
    /**
     * 别名
     * @return
     */
    String value() default "";

}
