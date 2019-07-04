package com.smartbox.config;

import com.smartbox.bean.BeanInfo;
import com.smartbox.bean.PropertyInfo;
import com.smartbox.exception.XmlConfigException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XmlConfigReader implements ConfigReader{
    private static XmlConfigReader xmlConfigReader;

    private Document document;

    private XmlConfigReader(){}


    public static XmlConfigReader newInstance(){
        if(xmlConfigReader==null){
            xmlConfigReader = new XmlConfigReader();
        }

        return xmlConfigReader;
    }

    @Override
    public void loadConfig(String path) throws XmlConfigException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(path);


        } catch (ParserConfigurationException|SAXException|IOException e) {
            e.printStackTrace();
            throw new XmlConfigException("xml配置文件加载失败，请xml检查配置文件是否合法");
        }

    }

    @Override
    public List<BeanInfo> readConfig() throws XmlConfigException {
        List<BeanInfo> beanInfos = new ArrayList<>();
        //读取要加入到Box容器中的bean
        NodeList beans = document.getElementsByTagName("bean");


        for (int i = 0; i < beans.getLength(); i++) {
            Element element = (Element) beans.item(i);

            beanInfos.add(analysisBean(element));
        }

        return beanInfos;
    }


    private BeanInfo analysisBean(Element element) throws XmlConfigException {
        BeanInfo beanInfo = new BeanInfo();

        // element.getAttribute（String name）方法，如果没有该属性，返回空串
        String id = element.getAttribute("id");
        String className = element.getAttribute("class");
        System.out.println("发现一个bean:"+id+" 类名为:"+className);

        if(id==null){
            throw new XmlConfigException("bean 的id 为null ,必须为bean 指定id属性");
        }
        if("".equals(id)){
            throw new XmlConfigException("bean 的id 为空 ,必须为bean 指定id属性为一个非空字符串");
        }

        if(className==null){
            throw new XmlConfigException("bean 的class 为null ,必须为bean 指定class 属性,为全限定类名");
        }
        if("".equals(className)){
            throw new XmlConfigException("bean 的class 为空!");
        }

        beanInfo.setId(id);
        beanInfo.setClassName(className);
        beanInfo.setProperties(analysisBeanProperties(element));

        return  beanInfo;
    }

    private HashMap<String, PropertyInfo> analysisBeanProperties(Element element) throws XmlConfigException {
        HashMap<String,PropertyInfo> properties = new HashMap<>();

        NodeList elementProperties = element.getElementsByTagName("property");
        if(elementProperties == null){
            return null;
        }

        for (int i = 0; i < elementProperties.getLength(); i++) {
            Element element1 = (Element)elementProperties.item(i);

            PropertyInfo propertyInfo = analysisProperty(element1);
            properties.put(propertyInfo.getName(),propertyInfo);
        }


        return properties;
    }

    private PropertyInfo analysisProperty(Element element) throws XmlConfigException {
        if(element == null){
            return null;
        }
        PropertyInfo propertyInfo =  new PropertyInfo();

        String name = element.getAttribute("name");
        if(name == null){
            throw new XmlConfigException("Property 的 name 为 null ,必须为 Property 指定 name 属性");
        }
        if("".equals(name)){
            throw new XmlConfigException("Property 的 name 为空 ,必须为bean 指定 name 属性为一个非空字符串");
        }

        String ref = element.getAttribute("ref");
        String value = element.getAttribute("value");

        if(ref == null && value == null){
            throw new XmlConfigException("Property 的 值 为 null ,必须为 Property 指定 ref 属性引用一个bean对象作为属性值，或者指定 value 属性直接赋值（仅针对基本数据类型）");
        }
        if(ref != null && value != null && !"".equals(ref) && !"".equals(value)){
            throw new XmlConfigException("Property 的 值 冲突，ref 和 value 只能设置一个");
        }

        propertyInfo.setName(name);
        if(ref != null && !"".equals(ref)){
            propertyInfo.setRef(ref);
        }else{
            propertyInfo.setValue(value);
        }


        return propertyInfo;
    }

    @Override
    public void analysis() {

    }
}
