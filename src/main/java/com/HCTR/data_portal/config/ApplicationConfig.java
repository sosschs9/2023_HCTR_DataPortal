package com.HCTR.data_portal.config;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

// 레이어드 아키텍처에서 Controller가 사용하는 Bean들에 대해 설정을 한다.
// dao, service를 컴포넌트 스캔하여 찾도록 한다.
// 어노테이션으로 트랜잭션을 관리하기 위해 @EnableTransactionManagement를 설정하였다.
@Configuration
@ComponentScan(basePackages = {"com.HCTR.data_portal.repository", "com.HCTR.data_portal.service"})
@ComponentScan(basePackageClasses = HadoopConfig.class)
@EnableTransactionManagement
public class ApplicationConfig{

    @Bean
    public BasicDataSource dataSource() {
        BasicDataSource datasource = new BasicDataSource();
        datasource.setDriverClassName("org.mariadb.jdbc.Driver");
        datasource.setUrl("jdbc:mariadb://155.230.118.225:3306/dataportal");
        datasource.setUsername("datadoctor");
        datasource.setPassword("datadoctor1!2");

        return datasource;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/*.xml"));
        return sessionFactory;
    }

    @Bean
    public SqlSessionTemplate sqlSession() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory().getObject());
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        resolver.setMaxUploadSize(10000000);
        return resolver;
    }
}

