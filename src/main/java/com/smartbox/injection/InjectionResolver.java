package com.smartbox.injection;

import com.smartbox.bean.BeanInfo;
import com.smartbox.bean.PropertyInfo;
import com.smartbox.box.Box;
import com.smartbox.exception.BoxException;

import java.lang.reflect.Field;
import java.util.HashMap;
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


    public void injection(Box box, List<BeanInfo> beanInfos) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        HashMap<String, PropertyInfo> hashMap;
        PropertyInfo propertyInfo;
        Class clazz;
        for (BeanInfo beanInfo : beanInfos) {
            hashMap = beanInfo.getProperties();
            if(hashMap == null) continue;

            //获得被注入对象的class对象
            clazz = Class.forName(beanInfo.getClassName());
            for (String s : hashMap.keySet()) {
                propertyInfo = hashMap.get(s);

                //获得被注入的属性对象
                Field field = clazz.getDeclaredField(propertyInfo.getName());
                //设置允许通过反射访问的权限
                field.setAccessible(true);
                //获得被注入的属性的类型
                Class<?> type = field.getType();

                //获得要注入的bean的id
                String ref = propertyInfo.getRef();

                //获得被注入对象
                Object bean = box.getBean(beanInfo.getId());
                //获得要注入的bean
                Object propertyBean = box.getBean(ref);

                field.set(bean,propertyBean);

                System.out.println("向["+beanInfo.getId()+"]注入["+ref+"]成功。");
            }
        }
    }
}
