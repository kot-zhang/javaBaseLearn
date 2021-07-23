package com.frame.study.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MyConfig {


    @Bean
    public TestBean testBean() {
        return new TestBean();
    }


    @Bean
    public TestBeanTwo testBeanTwo() {
        testBean();
        return new TestBeanTwo();
    }

}
