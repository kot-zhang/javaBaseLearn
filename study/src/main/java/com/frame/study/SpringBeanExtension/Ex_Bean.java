package com.frame.study.SpringBeanExtension;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Ex_Bean {

    @Value("${spring_test_name}")
    private String sname;


    public void say() {
        System.out.println("Ex_Bean say what!!!!");
    }
}
