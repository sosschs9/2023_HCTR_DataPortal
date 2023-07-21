package com.HCTR.data_portal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


// @Configuration 클래스에서 등록하는 경우
@Configuration
@ComponentScan(basePackageClasses = HadoopConfigChecker.class)
public class AppConfig {


    @Bean
    public HadoopConfigChecker hadoopConfigChecker() {
        return new HadoopConfigChecker();
    }
}
