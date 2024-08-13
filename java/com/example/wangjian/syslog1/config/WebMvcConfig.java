//package com.example.wangjian.syslog1.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.view.InternalResourceViewResolver;
//import org.springframework.web.servlet.view.JstlView;
//
//@Configuration
//public class WebMvcConfig implements WebMvcConfigurer {
//
//    private static final String DEFAULT_JSP_PREFIX = "/WEB-INF/views/";
//
//    private static final String JSP_SUFFIX = ".jsp";
//
//
//    //静态资源处理
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/assets/**").addResourceLocations("/assets");
//        registry.addResourceHandler("/res/**").addResourceLocations("/res/");
//    }
//
//
//    //视图解析器，这里使用了 InternalResourceViewResolver 类来解析 JSP 视图。设置了 JSTL 视图类（JstlView.class）、JSP 文件的前缀和后缀
//    @Override
//    public void configureViewResolvers(ViewResolverRegistry registry) {
//        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//        viewResolver.setViewClass(JstlView.class);
//        viewResolver.setPrefix(DEFAULT_JSP_PREFIX);
//        viewResolver.setSuffix(JSP_SUFFIX);
//        registry.viewResolver(viewResolver);
//    }
//}
