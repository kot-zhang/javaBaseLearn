package com.frame.study.SpringBeanExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;


import javax.annotation.PostConstruct;

@AutoConfigureAfter(Ex_ConditionBefore.class)
public class Ex_Condition {


    @PostConstruct
    public void add() {
        System.out.println("Ex_Condition add");
    }
}
