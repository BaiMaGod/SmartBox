package com.smartbox.core.injection;

import com.smartbox.core.box.Box;
import com.smartbox.core.config.BeanConfig;
import com.smartbox.core.config.PropertyConfig;

import java.lang.reflect.Field;
import java.util.List;

public class InjectionResolver {
    private static InjectionResolver injectionResolver;

    private InjectionResolver(){}

    public static InjectionResolver newInstance(){
        if(injectionResolver == null){
            injectionResolver = new InjectionResolver();
        }

        return injectionResolver;
    }


    public void injection(Box box, List<BeanConfig> beanConfigs) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        if(beanConfigs == null){
            return;
        }

        Class clazz;

        // 遍历每一个装配的bean
        for (BeanConfig beanConfig : beanConfigs) {

            // 遍历每个bean要注入依赖的 属性
            for (PropertyConfig propertyConfig : beanConfig.getProperties()) {
                //获得被注入的属性对象,优先考虑 注解 注入
                Field field = propertyConfig.getField();
                // 如果field对象为空，说明是配置文件装配
                if(field == null){
                    // 根据配置文件中的类名，或得class对象
                    clazz = Class.forName(beanConfig.getClassName());

                    // 根据class对象获取 要注入的属性 的field对象
                    field = clazz.getDeclaredField(propertyConfig.getName());
                }

                // 获取依赖的id
                String ref = propertyConfig.getRef();

                // 不是注入bean，而是注入值
                if(ref == null){
                    injection(field, beanConfig.getObject(), propertyConfig.getValue());
                }else{
                    //获得被注入对象
                    Object bean = beanConfig.getObject();
                    //获得要注入的bean
                    Object propertyBean = box.getBean(ref);


                    //设置允许通过反射修改的权限
                    field.setAccessible(true);
                    // 赋值
                    field.set(bean,propertyBean);
                }

                System.out.println("向["+beanConfig.getId()+"]注入["+ref+"]成功。");
            }
        }
    }

    public void injection(Field field, Object o,String value) {
       field.setAccessible(true);
        try {
//            Class<?> type = field.getType();
            field.set(o,value);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
