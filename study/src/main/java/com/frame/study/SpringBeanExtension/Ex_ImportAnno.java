package com.frame.study.SpringBeanExtension;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(Ex_ImportBean.class)
public @interface Ex_ImportAnno {


}
