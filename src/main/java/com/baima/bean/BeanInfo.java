package com.baima.bean;

import java.util.HashMap;

/**
 * 要注入的bean的信息类
 */
public class BeanInfo {
    /**
     * 用于在box容器中的唯一标识符
     */
    private String id;

    /**
     * 类名
     */
    private String className;

    /**
     * 实例对象
     */
    private Object object;

    /**
     * 属性，key为属性字段名，value为类型名
     */
    private HashMap<String,String> properties;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public HashMap<String, String> getProperties() {
        return properties;
    }

    public void setProperties(HashMap<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "BeanInfo{" +
                "id='" + id + '\'' +
                ", className='" + className + '\'' +
                ", properties=" + properties +
                '}';
    }
}
