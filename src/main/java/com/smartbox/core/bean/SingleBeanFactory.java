package com.smartbox.core.bean;

import com.smartbox.core.config.BeanConfig;
import com.smartbox.core.exception.BeanFactoryException;
import com.smartbox.core.exception.SimpleFactoryException;

import java.util.List;

public class SingleBeanFactory implements BeanFactory{
    private static SingleBeanFactory singleBeanFactory;

    private SingleBeanFactory(){}

    public static SingleBeanFactory newInstance(){
        if(singleBeanFactory==null){
            singleBeanFactory = new SingleBeanFactory();
        }

        return singleBeanFactory;
    }


    @Override
    public Object instance(String className) throws SimpleFactoryException {
        Object object = null;
        try {
            Class clazz = Class.forName(className);

            object = clazz.newInstance();
        } catch (ClassNotFoundException e) {
            throw new SimpleFactoryException("["+className+"]类找不到，创建失败！");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return object;
    }

    @Override
    public Object instance(Class clazz) {
        Object object = null;

        try {
            object = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return object;
    }

    @Override
    public List<BeanConfig> instence(List<BeanConfig> beanConfigs) throws BeanFactoryException {
        Class clazz;
        String className = "";
        try {
            for (BeanConfig beanConfig : beanConfigs) {
                className = beanConfig.getClassName();

                clazz = Class.forName(className);
                beanConfig.setObject(clazz.newInstance());

            }
        } catch (ClassNotFoundException e) {
            throw new SimpleFactoryException("["+className+"]类找不到，创建失败！");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return beanConfigs;
    }
}
