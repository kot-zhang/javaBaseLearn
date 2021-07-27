package com.frame.study.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CompentBean {


    @Bean
    public TestBeanThree testBeanThree() {
        return new TestBeanThree();
    }
}
