package com.frame.study.designParrent.ChainOfResponsibility;

public interface RouterChain {

    void route(RouteChainWrapper routeChainWrapper);


    boolean enable();
}
