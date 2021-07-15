package com.frame.study.SpringBeanExtension;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;

import javax.annotation.PostConstruct;

@AutoConfigureAfter(Ex_Condition.class)
public class Ex_After {

    @PostConstruct
    public void  test(){
        System.out.println("Ex_After");
    }
}
