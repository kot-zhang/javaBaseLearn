package com.frame.study.AnalysisSpringCode;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class DefinitionFactroyBean implements FactoryBean, InitializingBean {


    @Override
    public boolean isSingleton() {
        return true;
    }


    @Override
    public Class<?> getObjectType() {
        return null;
    }


    @Override
    public Object getObject() throws Exception {
        return null;
    }


    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
