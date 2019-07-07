package com.smartbox.core.box;



import com.smartbox.core.annotation.resolver.AnnotationResolver;
import com.smartbox.core.bean.BeanFactory;
import com.smartbox.core.bean.SingleBeanFactory;
import com.smartbox.core.config.BeanConfig;
import com.smartbox.core.config.BoxConfig;
import com.smartbox.core.config.ConfigReader;
import com.smartbox.core.config.XmlConfigReader;
import com.smartbox.core.exception.BeanFactoryException;
import com.smartbox.core.exception.ConfigException;
import com.smartbox.core.exception.FactoryException;
import com.smartbox.core.injection.InjectionResolver;
import com.smartbox.core.exception.BoxException;

import java.util.HashMap;
import java.util.List;

public class SimpleBox implements Box{
    private static Box simpleBox;

    private static HashMap<String, BeanConfig> box = new HashMap<>();

    private static BoxConfig boxConfig;

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

        // 读配置文件
        readConfig(path);

        // 扫描包
        scannerPackage(boxConfig);

        // 实例化对象
        instanceObject();

        // 装载对象
        loadObject();

        // 注入依赖对象
        injectionObject();

        System.out.println("初始化容器成功！");
    }

    public static void readConfig(String path){
        System.out.println("box容器正在读取配置文件......");

        ConfigReader xmlConfigReader = XmlConfigReader.newInstance();
        try {
            boxConfig = xmlConfigReader.loadConfig(path);

        } catch (ConfigException e) {
            e.printStackTrace();
        }

        System.out.println("box容器读取配置文件完成。");
    }

    public static void scannerPackage(BoxConfig boxConfig){
        if(boxConfig.getBasePackages()==null){
            return;
        }
        System.out.println("正在扫描包......");

        AnnotationResolver annotationResolver = AnnotationResolver.newInstance();

        try {
            for (String basePackage : boxConfig.getBasePackages()) {
                boxConfig.getBeanConfigs().addAll(annotationResolver.scanComponent(basePackage));
                System.out.println("包【"+basePackage+"】扫描完毕");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FactoryException e) {
            e.printStackTrace();
        } catch (BoxException e) {
            e.printStackTrace();
        }

        System.out.println("扫描完成。");
    }

    public static void instanceObject(){
        System.out.println("box容器正在初始化对象......");

        BeanFactory singleBeanFactory = SingleBeanFactory.newInstance();
        try {
            boxConfig.setBeanConfigs(singleBeanFactory.instence(boxConfig.getBeanConfigs()));
        } catch (BeanFactoryException e) {
            e.printStackTrace();
        }

        System.out.println("box容器初始化对象完成。");
    }

    public static void loadObject(){
        System.out.println("box容器正在装载对象......");
        for (BeanConfig beanConfig : boxConfig.getBeanConfigs()) {
            box.put(beanConfig.getId(),beanConfig);
            System.out.println("装载第"+box.size()+"个对象：["+beanConfig.getId()+"]成功。");
        }
    }

    public static void injectionObject(){
        System.out.println("正在注入依赖......");

        InjectionResolver injectionResolver = InjectionResolver.newInstance();
        try {
            injectionResolver.injection(simpleBox,boxConfig.getBeanConfigs());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }  catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        System.out.println("依赖注入完成。");
    }


    @Override
    public void addBean(String id,String className,Object object)  throws BoxException {
        if(box.containsKey(id)){
            throw new BoxException("bean 的 id 不能重复");
        }
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setId(id);
        beanConfig.setObject(object);

        box.put(id,beanConfig);
    }

    @Override
    public void addBean(BeanConfig beanConfig) throws BoxException {
        if(box.containsKey(beanConfig.getId())){
            throw new BoxException("bean 的 id 不能重复");
        }
        box.put(beanConfig.getId(),beanConfig);
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

    @Override
    public void checkBean(){
        for (String s : box.keySet()) {
            System.out.println(box.get(s).getId());
        }
    }
}
