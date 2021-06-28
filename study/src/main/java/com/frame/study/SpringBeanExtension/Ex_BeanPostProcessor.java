package com.frame.study.SpringBeanExtension;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class Ex_BeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(Objects.equals(beanName,"ex_Bean")){
            Ex_Bean ex_bean = (Ex_Bean)bean;
            System.out.println(JSON.toJSONString(ex_bean));
        }
        return bean;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
