package com.frame.study.designParrent.ChainOfResponsibility;


@RouteAnno(order = 1, name = "AuthorCheckPlugin")
public class AuthorCheckPlugin implements RouterChain {


    @Override
    public void route(RouteChainWrapper routeChainWrapper) {
        System.out.println("开始路由 ： AuthorCheckPlugin");
        routeChainWrapper.route();
    }


    @Override
    public boolean enable() {
        return true;
    }
}
