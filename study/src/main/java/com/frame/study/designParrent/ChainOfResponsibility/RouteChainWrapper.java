package com.frame.study.designParrent.ChainOfResponsibility;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class RouteChainWrapper {
    private final AtomicInteger atomicInteger = new AtomicInteger(-1);

    private List<RouterChain> routerChains;

    public RouteChainWrapper(List<RouterChain> routerChains) {
        this.routerChains = routerChains;
    }


    public void route() {
        if (atomicInteger.incrementAndGet() == routerChains.size()) {
            return;
        }
        RouterChain router = routerChains.get(atomicInteger.get());
        if (!router.enable()) {
            route();
            return;
        }
        router.route(this);
    }

}
