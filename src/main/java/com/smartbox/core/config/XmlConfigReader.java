package com.smartbox.core.config;

import com.smartbox.core.exception.XmlConfigException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlConfigReader implements ConfigReader{
    private static XmlConfigReader xmlConfigReader;

    private XmlConfigReader(){}


    public static XmlConfigReader newInstance(){
        if(xmlConfigReader==null){
            xmlConfigReader = new XmlConfigReader();
        }

        return xmlConfigReader;
    }

    @Override
    public BoxConfig loadConfig(String path) throws XmlConfigException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        BoxConfig config = new BoxConfig();

        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(path);


            config.setBasePackages(readComponentScan(document));
            config.setBeanConfigs(readBean(document));
        } catch (ParserConfigurationException|SAXException|IOException e) {
            e.printStackTrace();
            throw new XmlConfigException("xml配置文件加载失败，请xml检查配置文件是否合法");
        }

        return config;
    }

    public List<String> readComponentScan(Document document) throws XmlConfigException {
        List<String> basePackages = new ArrayList<>();

        NodeList elements = document.getElementsByTagName("component-scan");

        for (int i = 0; i < elements.getLength(); i++) {
            Element item = (Element) elements.item(i);
            String basePackage = item.getAttribute("basePackage");

            basePackages.add(basePackage);
        }

        return basePackages;
    }

    public List<BeanConfig> readBean(Document document) throws XmlConfigException {
        List<BeanConfig> beanConfigs = new ArrayList<>();
        //读取要加入到Box容器中的bean
        NodeList beans = document.getElementsByTagName("bean");


        for (int i = 0; i < beans.getLength(); i++) {
            Element element = (Element) beans.item(i);

            beanConfigs.add(analysisBean(element));
        }

        return beanConfigs;
    }


    public BeanConfig analysisBean(Element element) throws XmlConfigException {
        BeanConfig beanConfig = new BeanConfig();

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

        beanConfig.setId(id);
        beanConfig.setClassName(className);
        beanConfig.setProperties(analysisBeanProperties(element));

        return  beanConfig;
    }

    public List<PropertyConfig> analysisBeanProperties(Element element) throws XmlConfigException {
        List<PropertyConfig> properties = new ArrayList<>();

        NodeList elementProperties = element.getElementsByTagName("property");
        if(elementProperties == null){
            return null;
        }

        for (int i = 0; i < elementProperties.getLength(); i++) {
            Element element1 = (Element)elementProperties.item(i);

            PropertyConfig propertyConfig = analysisProperty(element1);
            properties.add(propertyConfig);
        }


        return properties;
    }

    public PropertyConfig analysisProperty(Element element) throws XmlConfigException {
        if(element == null){
            return null;
        }
        PropertyConfig propertyConfig =  new PropertyConfig();

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

        propertyConfig.setName(name);
        if(ref != null && !"".equals(ref)){
            propertyConfig.setRef(ref);
        }else{
            propertyConfig.setValue(value);
        }


        return propertyConfig;
    }

}
