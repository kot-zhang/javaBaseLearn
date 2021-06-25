package com.frame.study.SpringBeanExtension;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class Ex_ApplicationContext implements ApplicationContextAware {

    private ApplicationContext applicationContext;




    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    /**
     * 通过bean 名称获取容器中的Bean
     */
    public Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }
}
