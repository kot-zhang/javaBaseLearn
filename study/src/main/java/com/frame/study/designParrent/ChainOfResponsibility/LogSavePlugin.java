package com.frame.study.designParrent.ChainOfResponsibility;

@RouteAnno(order = 3, name = "LogSavePlugin")
public class LogSavePlugin implements RouterChain {


    @Override
    public void route(RouteChainWrapper routeChainWrapper) {
        System.out.println("开始路由 ： LogSavePlugin");
        routeChainWrapper.route();
    }


    @Override
    public boolean enable() {
        return true;
    }
}
