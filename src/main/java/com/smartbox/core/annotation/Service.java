package com.smartbox.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME) //运行时启动
@Documented
@Component
public @interface Service {
    /**
     * 别名
     */
    String value() default "";
}
