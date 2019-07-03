package com.baima.config;

import com.baima.exception.ConfigException;
import com.baima.exception.XmlConfigException;

/**
 * 这是所有配置文件读取类的祖先接口，定义配置文件读取的规范
 */
public interface ConfigReader {

    /**
     * 加载配置文件
     * @param path 配置文件路径
     */
    void loadConfig(String path) throws ConfigException, XmlConfigException;

    /**
     * 读取配置文件
     */
    void readConfig() throws ConfigException, XmlConfigException;

    /**
     * 分析配置文件某个细节
     */
    void analysis() throws ConfigException;
}
