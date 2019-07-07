package com.smartbox.core.config;


import java.util.List;

/**
 * 关于SmartBox容器配置信息基类，定义所有容器的共有属性
 */
public class BoxConfig{

    /**
     * 容器扫描的包，即容器的作用范围
     */
    protected List<String> basePackages;

    /**
     *  beans的配置信息
     */
    protected List<BeanConfig> beanConfigs;



    public List<String> getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(List<String> basePackages) {
        this.basePackages = basePackages;
    }

    public List<BeanConfig> getBeanConfigs() {
        return beanConfigs;
    }

    public void setBeanConfigs(List<BeanConfig> beanConfigs) {
        this.beanConfigs = beanConfigs;
    }

}
