package com.security.freemarker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MVCConfig extends WebMvcConfigurerAdapter {
    /**
     * 页面跳转请求注册
     */
    public void addViewControllers(ViewControllerRegistry registry) {
        //默认页面
        registry.addViewController("/").setViewName("forward:/login");
        //登录页面url
        //registry.addViewController("/login").setViewName("login");
        //登录成功之后跳转的页面，如果需要传入一些资源参数进页面可以写到controller里
        //registry.addViewController("/main").setViewName("main");
    }
    /**
     * 静态资源配置
     */
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
        super.addResourceHandlers(registry);
    }
}
