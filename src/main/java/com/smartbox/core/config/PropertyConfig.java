package com.smartbox.core.config;

import java.lang.reflect.Field;

/**
 * bean 的 属性配置信息
 */
public class PropertyConfig{

    /**
     * 属性名
     */
    private String name;

    /**
     * 属性引用
     */
    private String ref;

    /**
     * 注入属性的值，值和引用只能选一个
     */
    private String value;

    /**
     * Field 对象
     * @return
     */
    private Field field;



    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}