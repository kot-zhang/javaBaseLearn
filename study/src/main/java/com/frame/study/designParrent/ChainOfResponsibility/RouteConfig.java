package com.frame.study.designParrent.ChainOfResponsibility;

import org.springframework.context.annotation.Configuration;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class RouteConfig {

    private List<RouterChain> routers;

    public RouteConfig(List<RouterChain> routers) {
        this.routers = routers.stream().sorted(Comparator.comparingInt(o -> o.getClass().getAnnotation(RouteAnno.class).order())).collect(Collectors.toList());
    }


    public RouteChainWrapper createChainWrapper() {
        return new RouteChainWrapper(routers);
    }
}
