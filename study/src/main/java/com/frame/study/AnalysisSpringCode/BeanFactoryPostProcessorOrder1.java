package com.frame.study.AnalysisSpringCode;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
public class BeanFactoryPostProcessorOrder1 implements BeanFactoryPostProcessor, Ordered {


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("BeanFactoryPostProcessorOrder1:::"+"postProcessBeanFactory");
    }


    @Override
    public int getOrder() {
        return 1;
    }
}
