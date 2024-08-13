//package com.example.wangjian.syslog1.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
///**
// * Created by 18274 on 2017/8/9. 不用了
// */
//@Configuration
//public class MyWebAppConfigurer implements WebMvcConfigurer {
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // 访问路径以 “/mystatic” 开头时，会去 “mystatic” 路径下找静态资源
//        registry
//                .addResourceHandler("/static/**")
//                .addResourceLocations("classpath:/static/","file:static/");
//    }
//
//}