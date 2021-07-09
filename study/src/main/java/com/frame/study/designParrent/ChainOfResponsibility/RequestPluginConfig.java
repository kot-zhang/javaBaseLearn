package com.frame.study.designParrent.ChainOfResponsibility;

import org.springframework.context.annotation.Configuration;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 路由配置
 */
@Configuration
public class RequestPluginConfig {

    private List<RequestPlugin> requestPlugins;

    /**
     * 注入相关处理器
     * 对处理器
     */
    public RequestPluginConfig(List<RequestPlugin> requestPlugins) {
        this.requestPlugins = requestPlugins.stream().sorted(Comparator.comparingInt(o -> o.getClass().getAnnotation(PluginAnno.class).order())).collect(Collectors.toList());
    }

    public InterceptorChainWrapper createChainWrapper() {
        return new InterceptorChainWrapper(requestPlugins);
    }
}
