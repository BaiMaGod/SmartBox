package com.smartbox.box;



import com.smartbox.bean.BeanFactory;
import com.smartbox.bean.BeanInfo;
import com.smartbox.bean.SingleBeanFactory;
import com.smartbox.config.ConfigReader;
import com.smartbox.config.XmlConfigReader;
import com.smartbox.exception.BeanFactoryException;
import com.smartbox.exception.BoxException;
import com.smartbox.exception.ConfigException;
import com.smartbox.exception.XmlConfigException;
import com.smartbox.injection.InjectionResolver;

import java.util.HashMap;
import java.util.List;

public class SimpleBox implements Box{
    private static Box simpleBox;

    private static HashMap<String, BeanInfo> box = new HashMap<>();

    private SimpleBox(){ }

    public static Box getInstance() {
        if(simpleBox==null){
            simpleBox = new SimpleBox();
        }

        return simpleBox;
    }

    public static Box instanceForXml(String path) {
        if(simpleBox==null){
            simpleBox = new SimpleBox();
        }

        initBox("src\\main\\resource\\"+path);
        return simpleBox;
    }

    private static void initBox(String path){
        System.out.println("box容器正在初始化......");

        System.out.println("box容器正在读取配置文件......");
        ConfigReader xmlConfigReader = XmlConfigReader.newInstance();
        List<BeanInfo> beanInfos = null;
        try {
            xmlConfigReader.loadConfig(path);

        } catch (ConfigException e) {
            e.printStackTrace();
        }
        try {
            beanInfos = xmlConfigReader.readConfig();
        } catch (ConfigException e) {
            e.printStackTrace();
        }
        System.out.println("box容器读取配置文件完成。");

        System.out.println("box容器正在初始化对象......");
        BeanFactory singleBeanFactory = SingleBeanFactory.newInstance();
        InjectionResolver injectionResolver = InjectionResolver.newInstance();
        try {
            beanInfos = singleBeanFactory.instence(beanInfos);
        } catch (BeanFactoryException e) {
            e.printStackTrace();
        }
        System.out.println("box容器初始化对象完成。");

        System.out.println("box容器正在装载对象......");
        for (BeanInfo beanInfo : beanInfos) {
            box.put(beanInfo.getId(),beanInfo);
            System.out.println("装载第"+box.size()+"个对象：["+beanInfo.getId()+"]成功。");
        }

        System.out.println("正在注入依赖......");
        try {
            injectionResolver.injection(simpleBox,beanInfos);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }  catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println("依赖注入完成。");

        System.out.println("初始化容器成功！");
    }

    @Override
    public void addBean(String id,String className,Object object)  throws BoxException {
        if(box.containsKey(id)){
            throw new BoxException("bean 的 id 不能重复");
        }
        BeanInfo beanInfo = new BeanInfo();
        beanInfo.setId(id);
        beanInfo.setObject(object);

        box.put(id,beanInfo);
    }

    @Override
    public Object getBean(String name) {

        return box.get(name).getObject();
    }

    @Override
    public Object getBean(Class clazz) {
        return null;
    }

    @Override
    public void removeBean(String name) {
//        if(box.containsKey(name)){
            box.remove(name);
//        }
    }

    @Override
    public void removeBean(Object object) {

    }

    @Override
    public void removeBean(Class clazz) {

    }

    public boolean containsType(){

        return false;
    }
}
