package com.frame.study.designParrent.ChainOfResponsibility;

import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 插件注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface PluginAnno {

    int order() default Ordered.HIGHEST_PRECEDENCE;

    String name();
}
