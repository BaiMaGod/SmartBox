package com.smartbox.core.annotation.resolver;


import com.smartbox.core.annotation.AutoWired;
import com.smartbox.core.annotation.Component;
import com.smartbox.core.annotation.Repository;
import com.smartbox.core.annotation.Service;
import com.smartbox.core.config.BeanConfig;
import com.smartbox.core.config.PropertyConfig;
import com.smartbox.core.exception.BoxException;
import com.smartbox.core.exception.FactoryException;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;


/**
 * 注解处理器，负责包扫描，注解类、注解属性的识别
 */
public class AnnotationResolver {
    private static AnnotationResolver annotationResolver;

    private AnnotationResolver(){}

    public static AnnotationResolver newInstance(){
        if(annotationResolver==null){
            annotationResolver = new AnnotationResolver();
        }

        return annotationResolver;
    }

    public File getFile(String packageName){
        //获取包名对应的全路径名，是在磁盘上的路径，即目录
        String packagePath = this.getClass().getClassLoader().getResource(packageName.replace(".", "/")).getPath();

        return new File(packagePath);
    }

    public List<Class> getAllAnnotation(String packageName) throws ClassNotFoundException {
        // 获取该目录的 File对象
        File file = getFile(packageName);

        ArrayList<Class> list = new ArrayList<>();
        //遍历该目录中的文件
        for (File file1 : file.listFiles()) {
            if(file1.isDirectory()){
                // 如果当前File对象是一个文件夹，就跳过
                continue;
            }
            // 拼凑类的全限定名
            String className = packageName + "." + file1.getName().replace(".class","");

            list.add(Class.forName(className));

        }
        return list;
    }


    /**
     * 扫描包，识别 包 中所有的类
     * @return 类的全限定名集合
     */
    public List<String> scanPackage(String packageName,ArrayList<String> classNames) {
        // 获取该目录的 File对象
        File file = getFile(packageName);

        //遍历该目录中的文件
        for (File file1 : file.listFiles()) {
            if(file1.isDirectory()){
                // 如果当前File对象是一个文件夹，就以同样的方式扫描该包
                scanPackage(packageName + "." + file1.getName(),classNames);
            }else{
                // 拼凑类的全限定名
                String className = packageName + "." + file1.getName().replace(".class","");

                classNames.add(className);

//                System.out.println(className);
            }

        }

        return classNames;
    }


    /**
     * 注解扫描，将指定的注解扫描
     * @param classNames   类名列表
     * @param annotation
     * @return  List<BeanConfig>
     */
    public List<BeanConfig> scanAnnotation(List<String> classNames, Class annotation) throws ClassNotFoundException {
        List<BeanConfig> classes = new ArrayList<>();

        BeanConfig beanConfig;
        for (String className : classNames) {
            Class<?> clazz = Class.forName(className);

            if(clazz.isAnnotationPresent(annotation)){
                beanConfig = new BeanConfig();
                beanConfig.setId(clazz.getName());
//                classes.add(clazz);
            }
        }

        return classes;
    }

    /**
     * 扫描指定包下 被component 注解的类及其子类
     * @param packageName 包
     */
    public List<BeanConfig> scanComponent(String packageName) throws ClassNotFoundException, FactoryException, BoxException {
        if(packageName==null) return null;

        List<String> classNames = scanPackage(packageName,new ArrayList<String>());

        System.out.println("共"+classNames.size()+"个类");

        List<BeanConfig> beanConfigs = new ArrayList<>();

        for (String className : classNames) {
            System.out.println("扫描到一个类:["+className+"]");

            Class clazz = Class.forName(className);

            Component component = (Component) clazz.getDeclaredAnnotation(Component.class);
            if(component != null){
                System.out.println("扫描到一个被 component 注解的类:["+className+"]");
                if(!"".equals(component.value())){
                    beanConfigs.add(getBeanConfig(clazz,className,component.value()));
                }else{
                    beanConfigs.add(getDefaultBeanConfig(clazz,className));
                }
                continue;
            }

            Service service = (Service) clazz.getDeclaredAnnotation(Service.class);
            if(service != null){
                System.out.println("扫描到一个被 Service 注解的类:["+className+"]");
                if(!"".equals(service.value())){
                    beanConfigs.add(getBeanConfig(clazz,className,service.value()));
                }else{
                    beanConfigs.add(getDefaultBeanConfig(clazz,className));
                }
                continue;
            }
            Repository repository = (Repository) clazz.getDeclaredAnnotation(Repository.class);
            if(repository != null){
                System.out.println("扫描到一个被 Repository 注解的类:["+className+"]");
                if(!"".equals(repository.value())){
                    beanConfigs.add(getBeanConfig(clazz,className,repository.value()));
                }else{
                    beanConfigs.add(getDefaultBeanConfig(clazz,className));
                }
            }
        }

        return beanConfigs;
    }


    public BeanConfig getBeanConfig(Class clazz,String className,String id){
        BeanConfig beanConfig = new BeanConfig();

        beanConfig.setId(id);
        beanConfig.setClassName(className);
        beanConfig.setClazz(clazz);
        beanConfig.setProperties(scanAutoWired(clazz));

        return beanConfig;
    }

    public BeanConfig getDefaultBeanConfig(Class clazz,String className){
        BeanConfig beanConfig = new BeanConfig();

        // 获取该类实现的所有接口
        Class[] interfaces = clazz.getInterfaces();
        // 如果实现的接口个数不为0
        if(interfaces.length>0){
            beanConfig.setId(toLowFirstWord(interfaces[0].getSimpleName()));
        }else{
            beanConfig.setId(toLowFirstWord(clazz.getSimpleName()));
        }
        beanConfig.setClassName(className);
        beanConfig.setClazz(clazz);
        beanConfig.setProperties(scanAutoWired(clazz));

        return beanConfig;
    }

    /**
     * 扫描被 AutoWired 注解的属性
     * @param clazz
     */
    public List<PropertyConfig> scanAutoWired(Class clazz) {
        if(clazz==null) return null;

        List<PropertyConfig> propertyConfigs = new ArrayList<>();

        //获取该类的所有属性对象
        Field[] declaredFields = clazz.getDeclaredFields();

        PropertyConfig propertyConfig;
        for (Field declaredField : declaredFields) {
            AutoWired autoWired = declaredField.getDeclaredAnnotation(AutoWired.class);
            //如果该属性被 AutoWired 注解
            if(autoWired != null){
                propertyConfig = new PropertyConfig();
                propertyConfig.setField(declaredField);

                if("".equals(autoWired.value())){
                    // 如果注解的 value 属性为空，则使用默认的按类型注入
                    propertyConfig.setRef(toLowFirstWord(declaredField.getName()));
                }else{
                    // 如果指定了注入对象，则注入指定的对象
                    propertyConfig.setRef(autoWired.value());
                }

                propertyConfigs.add(propertyConfig);
            }
        }

        return propertyConfigs;
    }



    public String toLowFirstWord(String str){
        if(str == null) return null;

        if(Character.isLowerCase(str.charAt(0))){
            return str;
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * 扫描包下指定被注解的类
     * @param packageName   包名
     */
    public List<BeanConfig> scanAnnotation(String packageName,Class annotation) throws ClassNotFoundException, FactoryException, BoxException {

        if(annotation == Component.class){
//            return scanComponent(scanPackage(packageName));
        }

        if(annotation == AutoWired.class){
//            return scanAutoWired();
        }

        return null;
    }


    /**
     * 扫描类中的全部属性
     * @param clazz 要扫描的类
     * @return  属性类型集合
     */
    public Field[] scanField(Class clazz){

        return clazz.getDeclaredFields();
    }

    /**
     * 扫描类中的属性，识别 属性上的 注解
     * @param clazz 要扫描的类
     * @return  被注解的属性类型集合
     */
    public List<Field> scanField(Class clazz,Class annotation){
        List<Field> fields = new ArrayList<>();

        for (Field field : scanField(clazz)) {
            if(field.isAnnotationPresent(annotation)){
                fields.add(field);
            }
        }

        return fields;
    }
    


    /**
     * 扫描类中的全部方法
     * @param clazz 要扫描的类
     * @return  方法 集合
     */
    public Method[] scanMethod(Class clazz){

        return clazz.getDeclaredMethods();
    }


    /**
     * 扫描类中的方法，识别方法注解
     * @param clazz 要扫描的类
     * @param annotation 指定扫描的注解
     * @return 方法 集合
     */
    public List<Method> scanMethod(Class clazz,Class annotation){
        List<Method> methods = new ArrayList<>();

        for (Method method : scanMethod(clazz)) {
            if(method.isAnnotationPresent(annotation)){
                methods.add(method);
            }
        }

        return methods;
    }

    /**
     * 扫描方法的所有参数
     * @param method 要扫描的方法对象
     * @return 参数集合 集合
     */
    public Parameter[] scanParam(Method method){

        return method.getParameters();
    }

    /**
     * 扫描方法的参数,赛选出指定注解的参数
     * @param method 要扫描的方法对象
     * @param annotation 指定扫描的注解
     * @return 参数集合 集合
     */
    public List<Parameter> scanParam(Method method,Class annotation){
        List<Parameter> parameters = new ArrayList<>();

        for (Parameter parameter : scanParam(method)) {
            if(parameter.isAnnotationPresent(annotation)){
                parameters.add(parameter);
            }
        }

        return parameters;
    }

}
