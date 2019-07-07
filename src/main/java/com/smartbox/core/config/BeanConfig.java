package com.smartbox.core.config;

import java.util.List;

/**
 * 关于bean的配置信息类，定义bean可以配置哪些属性
 */
public class BeanConfig {
    /**
     * 用于在box容器中的唯一标识符
     */
    private String id;

    /**
     * name，在容器中的名字，在容器中必须唯一
     */
    private String name;

    /**
     * bean指定的类名，全限定名
     */
    private String className;

    /**
     * bean 的 class对象
     */
    private Class clazz;

    /**
     * 实例对象
     */
    private Object object;

    /**
     * 属性注入信息,key:属性名，value:属性信息类
     */
    private List<PropertyConfig> properties;


    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<PropertyConfig> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyConfig> properties) {
        this.properties = properties;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}