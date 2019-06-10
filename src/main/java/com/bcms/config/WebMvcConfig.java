package com.bcms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * @className WebMvcConfig
 * @description: WebMvcConfig配置类
 * @author: Jackie
 * @date: 2018/12/26 15:15
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        /* 登录页 */
        registry.addViewController("/").setViewName("login");
        /* 主页 */
        registry.addViewController("/index.html").setViewName("index");
    }
}
