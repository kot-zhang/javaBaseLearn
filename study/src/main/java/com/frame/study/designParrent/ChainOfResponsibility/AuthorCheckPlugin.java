package com.frame.study.designParrent.ChainOfResponsibility;

/**
 * 权限校验
 */
@PluginAnno(order = 1, name = "AuthorCheckPlugin")
public class AuthorCheckPlugin implements RequestPlugin {

    @Override
    public void interceptor(InterceptorChainWrapper routeChainWrapper) {
        System.out.println("开始路由 ： AuthorCheckPlugin");
        routeChainWrapper.interceptor();
    }

    @Override
    public boolean enable() {
        return true;
    }
}
