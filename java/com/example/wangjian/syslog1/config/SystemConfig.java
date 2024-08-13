package com.example.wangjian.syslog1.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SystemConfig implements WebMvcConfigurer {

    public void addInterceptors(InterceptorRegistry interceptorRegistry){
        interceptorRegistry.addInterceptor(new TokenInterceptor()).addPathPatterns("/**").excludePathPatterns("swagger-ui.html","/swagger-resource/**");

    }


}
