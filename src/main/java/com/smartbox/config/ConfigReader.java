package com.smartbox.config;

import com.smartbox.bean.BeanInfo;
import com.smartbox.exception.ConfigException;
import com.smartbox.exception.XmlConfigException;

import java.util.List;

/**
 * 这是所有配置文件读取类的祖先接口，定义配置文件读取的规范
 */
public interface ConfigReader {

    /**
     * 加载配置文件
     * @param path 配置文件路径
     */
    void loadConfig(String path) throws ConfigException;

    /**
     * 读取配置文件
     */
    List<BeanInfo> readConfig() throws ConfigException;

    /**
     * 分析配置文件某个细节
     */
    void analysis() throws ConfigException;
}
