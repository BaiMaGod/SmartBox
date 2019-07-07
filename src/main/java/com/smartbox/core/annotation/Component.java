package com.smartbox.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME) //运行时启动
@Documented
public @interface Component {

    /**
     * 可以为 Component 起一个别名
     * @return
     */
    String value() default "";
}
