package com.smartbox.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME) //运行时启动
@Documented
public @interface AutoWired {
    /**
     * 可以 起一个别名
     * @return
     */
    String value() default "";
}
