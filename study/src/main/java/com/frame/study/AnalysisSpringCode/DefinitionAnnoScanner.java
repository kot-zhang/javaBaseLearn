package com.frame.study.AnalysisSpringCode;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

public class DefinitionAnnoScanner  extends ClassPathBeanDefinitionScanner {

    private Class<? extends Annotation> annotationClass;


    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public DefinitionAnnoScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }


    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        return beanDefinitions;
    }


    public void  registerFilters(){
        if (this.annotationClass != null) {
            addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
        }
    }
}
