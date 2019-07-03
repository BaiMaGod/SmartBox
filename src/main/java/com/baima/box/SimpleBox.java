package com.baima.box;



import com.baima.bean.BeanInfo;
import com.baima.bean.SingleBeanFactory;
import com.baima.config.XmlConfigReader;
import com.baima.exception.BeanFactoryException;
import com.baima.exception.BoxException;
import com.baima.exception.XmlConfigException;

import java.util.HashMap;
import java.util.List;

public class SimpleBox implements Box{
    private static SimpleBox simpleBox;

    private static HashMap<String, BeanInfo> box = new HashMap<>();

    private SimpleBox(){ }

    public static SimpleBox getInstance() {
        if(simpleBox==null){
            simpleBox = new SimpleBox();
        }

        return simpleBox;
    }

    public static SimpleBox instanceForXml(String path) {
        if(simpleBox==null){
            simpleBox = new SimpleBox();
        }

        initBox("src\\main\\resource\\"+path);
        return simpleBox;
    }

    private static void initBox(String path){
        System.out.println("box容器正在初始化......");

        System.out.println("box容器正在读取配置文件......");
        XmlConfigReader xmlConfigReader = XmlConfigReader.newInstance();
        try {
            xmlConfigReader.loadConfig(path);

        } catch (XmlConfigException e) {
            e.printStackTrace();
        }
        try {
            xmlConfigReader.readConfig();
        } catch (XmlConfigException e) {
            e.printStackTrace();
        }
        List<BeanInfo> beanInfos = xmlConfigReader.getBeanInfos();
        System.out.println("box容器读取配置文件完成。");

        System.out.println("box容器正在初始化对象......");
        SingleBeanFactory singleBeanFactory = SingleBeanFactory.newInstance();
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
        if(box.containsKey(name)){
            box.remove(name);
        }
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
