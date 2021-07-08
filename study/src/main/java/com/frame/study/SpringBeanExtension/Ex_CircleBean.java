package com.frame.study.SpringBeanExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Ex_CircleBean {

    @Autowired
    private Ex_CircleBeanB ex_circleBeanB;

    public void create() {
        System.out.println("Ex_CircleBean");
    }
}
