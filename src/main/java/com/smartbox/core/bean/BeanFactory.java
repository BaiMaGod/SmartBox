package com.smartbox.core.bean;

import com.smartbox.core.config.BeanConfig;
import com.smartbox.core.exception.BeanFactoryException;

import java.util.List;

/**
 * 这是所有Bean工厂类的祖先接口，定义Bean工厂类的规范
 */
public interface BeanFactory extends Factory{


    /**
     * 初始化一组对象
     * @param beanInfos 要初始化的一组对象的BeanInfo对象 的集合
     */
    List<BeanConfig> instence(List<BeanConfig> beanInfos) throws BeanFactoryException;
}
