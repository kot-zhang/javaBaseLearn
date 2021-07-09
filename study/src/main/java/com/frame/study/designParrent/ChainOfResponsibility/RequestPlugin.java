package com.frame.study.designParrent.ChainOfResponsibility;


/**
 * 插件接口定义
 */
public interface RequestPlugin {

    /**
     * 路由
     */
    void interceptor(InterceptorChainWrapper routeChainWrapper);

    /**
     * 是否启用
     */
    boolean enable();
}
