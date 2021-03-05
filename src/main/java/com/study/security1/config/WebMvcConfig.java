package com.study.security1.config;

import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /*
    Mustache 관련 설정.
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        MustacheViewResolver resolver = new MustacheViewResolver();
        resolver.setCharset("UTF-8");
        resolver.setContentType("text/html;charset=UTF-8");
        //classpath: 까지가 project default 경로.
        resolver.setPrefix("classpath:/templates/");
        //이녀석 덕분에 .html파일도 mustache가 mustache파일로 인식함.
        resolver.setSuffix(".html");

        registry.viewResolver(resolver);
    }
}
