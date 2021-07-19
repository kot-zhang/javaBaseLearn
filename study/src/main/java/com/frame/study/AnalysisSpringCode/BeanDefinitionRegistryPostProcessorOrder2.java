package com.frame.study.AnalysisSpringCode;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
public class BeanDefinitionRegistryPostProcessorOrder2 implements BeanDefinitionRegistryPostProcessor, Ordered {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("BeanDefinitionRegistryPostProcessorOrder2:::" + "postProcessBeanFactory");
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        System.out.println("BeanDefinitionRegistryPostProcessorOrder2:::" + "postProcessBeanDefinitionRegistry");
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
