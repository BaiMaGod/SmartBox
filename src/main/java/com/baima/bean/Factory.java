package com.baima.bean;

import com.baima.exception.FactoryException;
import com.baima.exception.SimpleFactoryException;

/**
 * 这是所有工厂类的祖先接口，定义工厂类的规范
 */
public interface Factory {

    /**
     * 初始化一个对象
     * @param className 要初始化对象的类名
     * @return
     */
    Object instance(String className) throws FactoryException, SimpleFactoryException;

    /**
     * 初始化一个对象
     * @param clazz 要初始化对象的clazz对象
     * @return
     */
    Object instance(Class clazz) throws FactoryException;
}
