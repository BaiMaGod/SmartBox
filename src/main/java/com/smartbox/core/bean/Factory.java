package com.smartbox.core.bean;

import com.smartbox.core.exception.FactoryException;

/**
 * 这是所有工厂类的祖先接口，定义工厂类的规范
 */
public interface Factory {

    /**
     * 初始化一个对象
     * @param className 要初始化对象的类名
     * @return 返回一个实例对象
     */
    Object instance(String className) throws FactoryException;

    /**
     * 初始化一个对象
     * @param clazz 要初始化对象的clazz对象
     * @return 返回一个实例对象
     */
    Object instance(Class clazz) throws FactoryException;
}
