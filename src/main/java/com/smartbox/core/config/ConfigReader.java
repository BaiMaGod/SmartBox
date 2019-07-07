package com.smartbox.core.config;

import com.smartbox.core.exception.ConfigException;


/**
 * 这是所有配置文件读取类的祖先接口，定义配置文件读取的规范
 */
public interface ConfigReader {

    /**
     * 加载配置文件
     * @param path 配置文件路径
     * @return 配置信息类
     */
    BoxConfig loadConfig(String path) throws ConfigException;



}
