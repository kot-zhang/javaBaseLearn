package com.frame.study.designParrent.ChainOfResponsibility;

import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RouteAnno {

    int order() default Ordered.HIGHEST_PRECEDENCE;

    String name();
}
