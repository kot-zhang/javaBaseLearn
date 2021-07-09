package com.frame.study.designParrent.ChainOfResponsibility;

/**
 * 日志处理
 */
@PluginAnno(order = 3, name = "LogSavePlugin")
public class LogSavePlugin implements RequestPlugin {

    @Override
    public void interceptor(InterceptorChainWrapper routeChainWrapper) {
        System.out.println("开始路由 ： LogSavePlugin");
        routeChainWrapper.interceptor();
    }

    @Override
    public boolean enable() {
        return true;
    }
}
