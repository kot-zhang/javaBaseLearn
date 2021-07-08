package com.frame.study.designParrent.ChainOfResponsibility;

@RouteAnno(order = 2, name = "ParseHandlePlugin")
public class ParseHandlePlugin implements RouterChain {

    @Override
    public void route(RouteChainWrapper routeChainWrapper) {
        System.out.println("开始路由 ： ParseHandlePlugin");
        routeChainWrapper.route();
    }


    @Override
    public boolean enable() {
        return false;
    }
}
