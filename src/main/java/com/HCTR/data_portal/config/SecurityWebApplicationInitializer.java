package com.HCTR.data_portal.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/*
   AbstractSecurityWebApplicationInitializer를 상속받는 클래스를 작성해야 스프링 시큐리티 필터들이 활성화된다.
 */
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
}