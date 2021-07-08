package com.frame.study.SpringBeanExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class Ex_CircleBeanB {


    @Autowired
    private Ex_CircleBean ex_circleBean;

    @Async
    public void create() {
        ex_circleBean.create();
    }

}
