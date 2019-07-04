package com.smartbox.bean;

/**
 * 属性信息
 */
public class PropertyInfo {
    /**
     * 属性的字段名字
     */
    private String name;

    /**
     * 属性的类名
     */
    private String className;

    /**
     * 属性引用的bean对象id
     */
    private String ref;

    /**
     * 属性的值
     */
    private Object value;


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

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PropertyInfo{" +
                "name='" + name + '\'' +
                ", className='" + className + '\'' +
                ", value=" + value +
                '}';
    }
}
