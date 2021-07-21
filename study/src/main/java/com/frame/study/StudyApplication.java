package com.frame.study;

import com.frame.study.SpringBeanExtension.Ex_ImportAnno;
import com.frame.study.designParrent.ChainOfResponsibility.RequestPluginConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {"com.frame.study.AnalysisSpringCode"})
@ImportResource(locations = {"classpath:BeanXml.xml"})
@Ex_ImportAnno
@EnableAsync
public class StudyApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(StudyApplication.class, args);
        RequestPluginConfig requestPluginConfig = context.getBean(RequestPluginConfig.class);
        requestPluginConfig.createChainWrapper().interceptor();
    }

}
