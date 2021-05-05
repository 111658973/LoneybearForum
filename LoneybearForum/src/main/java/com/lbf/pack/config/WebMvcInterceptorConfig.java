package com.lbf.pack.config;

import com.lbf.pack.interceptor.CustomUrlIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcInterceptorConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CustomUrlIntercepter())
                .addPathPatterns("/post/**")
                .addPathPatterns("/zone/*")
                .excludePathPatterns("/post/*/get**")
                .excludePathPatterns("/zone/**get**");
    }
}
