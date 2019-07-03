package com.baima.bean;

import com.baima.exception.BeanFactoryException;
import com.baima.exception.SimpleFactoryException;

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
    public List<BeanInfo> instence(List<BeanInfo> beanInfos) throws BeanFactoryException {
        Class clazz;
        String className = "";
        try {
            for (BeanInfo beanInfo : beanInfos) {
                className = beanInfo.getClassName();

                clazz = Class.forName(className);
                beanInfo.setObject(clazz.newInstance());

            }
        } catch (ClassNotFoundException e) {
            throw new SimpleFactoryException("["+className+"]类找不到，创建失败！");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return beanInfos;
    }
}
