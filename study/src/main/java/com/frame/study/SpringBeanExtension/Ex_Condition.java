package com.frame.study.SpringBeanExtension;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;


import javax.annotation.PostConstruct;

@Configuration
public class Ex_Condition {


    @PostConstruct
    public void add() {
        System.out.println("Ex_Condition add");
    }
}
