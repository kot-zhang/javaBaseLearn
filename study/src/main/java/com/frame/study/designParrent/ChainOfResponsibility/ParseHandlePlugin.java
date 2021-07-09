package com.frame.study.designParrent.ChainOfResponsibility;

/**
 * 解析处理
 */
@PluginAnno(order = 2, name = "ParseHandlePlugin")
public class ParseHandlePlugin implements RequestPlugin {

    @Override
    public void interceptor(InterceptorChainWrapper routeChainWrapper) {
        System.out.println("开始路由 ： ParseHandlePlugin");
        routeChainWrapper.interceptor();
    }

    @Override
    public boolean enable() {
        return false;
    }
}
