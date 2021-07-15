package com.frame.study.SpringBeanExtension;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@AutoConfigureBefore(Ex_Condition.class)
public class Ex_ConditionBefore {

    @PostConstruct
    public void add() {
        System.out.println("add ");
    }
}
