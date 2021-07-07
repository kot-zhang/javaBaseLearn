package com.frame.study.SpringBeanExtension;

import javax.annotation.PostConstruct;

public class Ex_ConditionBefore {


    public void say() {
        System.out.println("Ex_ConditionBefore    处理");
    }


    @PostConstruct
    public void add() {
        System.out.println("add ");
    }
}
