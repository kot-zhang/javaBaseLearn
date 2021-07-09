package com.frame.study.designParrent.ChainOfResponsibility;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 插件调用链
 */
public class InterceptorChainWrapper {
    private final AtomicInteger atomicInteger = new AtomicInteger(-1);

    private List<RequestPlugin> requestPlugins;

    public InterceptorChainWrapper(List<RequestPlugin> requestPlugins) {
        this.requestPlugins = requestPlugins;
    }

    /**
     * 实际触发
     */
    public void interceptor() {
        if (atomicInteger.incrementAndGet() == requestPlugins.size()) {
            return;
        }
        RequestPlugin plugin = requestPlugins.get(atomicInteger.get());
        if (!plugin.enable()) {
            interceptor();
            return;
        }
        plugin.interceptor(this);
    }
}
