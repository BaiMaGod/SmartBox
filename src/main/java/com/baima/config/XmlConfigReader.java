package com.baima.config;

import com.baima.bean.BeanInfo;
import com.baima.exception.XmlConfigException;
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

    private Document document;
    private List<BeanInfo> beanInfos = new ArrayList<>();

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
    public void readConfig() throws XmlConfigException {
        //读取要加入到Box容器中的bean
        NodeList beans = document.getElementsByTagName("bean");


        for (int i = 0; i < beans.getLength(); i++) {
            Element element = (Element) beans.item(i);

            beanInfos.add(analysisBean(element));
        }
    }

    public List<BeanInfo> getBeanInfos(){
        return beanInfos;
    }

    private BeanInfo analysisBean(Element element) throws XmlConfigException {
        BeanInfo beanInfo = new BeanInfo();

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
        if("".equals(id)){
            throw new XmlConfigException("bean 的class 为空!");
        }

        beanInfo.setId(id);
        beanInfo.setClassName(className);

        return  beanInfo;
    }

    @Override
    public void analysis() {

    }
}
