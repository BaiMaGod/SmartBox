package com.smartbox.bean;


import com.smartbox.config.XmlConfigReader;

public class BoxFactory implements Factory{
    private static BoxFactory boxFactory;
    private static XmlConfigReader xmlConfigReader;

    private BoxFactory(){

    }

    public static BoxFactory newInstance(){
        if(boxFactory==null){
            boxFactory = new BoxFactory();
        }
        return boxFactory;
    }


//    public static XmlConfigReader getXmlConfigReader(){
//        if(xmlConfigReader==null){
//            xmlConfigReader = new XmlConfigReader();
//        }
//
//
//        return xmlConfigReader;
//    }

    @Override
    public Object instance(String className) {
        return null;
    }

    @Override
    public Object instance(Class clazz) {
        return null;
    }
}
