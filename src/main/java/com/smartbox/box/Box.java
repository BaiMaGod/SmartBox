package com.smartbox.box;

import com.smartbox.exception.BoxException;

/**
 *  这是所有Box容器类的祖先接口，定义box容器的规范
 */
public interface Box {


    /**
     * 向box容器中加入一个bean对象，由box容器管理起来
     * @param id    要加入容器的对象的id
     * @param className 对象的类名
     * @param object 要加入容器的对象
     */
    void addBean(String id, String className, Object object) throws BoxException;

    /**
     * 从box容器中获取一个对象
     * @param name 要获取的对象的名字（是存在box容器中的对象名字，不是类名）
     * @return 目标对象
     */
    Object getBean(String name);


    /**
     * 从box容器中获取一个对象
     * @param clazz 要获取对象的Class对象
     * @return 返回要获取的对象
     */
    Object getBean(Class clazz) throws BoxException;

    /**
     * 从box容器中移除一个bean对象
     * @param name  要移除对象的名字（是存在box容器中的对象名字，不是类名）
     */
    void removeBean(String name) throws BoxException;

    /**
     * 从box容器中移除一个bean对象
     * @param object  要移除的对象
     */
    void removeBean(Object object) throws BoxException;

    /**
     * 从box容器中移除一个bean对象
     * @param clazz  要移除对象的Class对象
     */
    void removeBean(Class clazz) throws BoxException;
}
