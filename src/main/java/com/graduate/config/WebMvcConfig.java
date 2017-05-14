package com.graduate.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

import com.graduate.config.serializer.FastJsonHttpMessageConverterEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.MultipartConfigElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by konglinghai on 2016/12/6.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("index.html");
        registry.addViewController("/").setViewName("index.html");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

    }



    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(stringHttpMessageConverter());
        converters.add(fastJsonHttpMessageConverter());
    }

    @Bean
    public StringHttpMessageConverter stringHttpMessageConverter(){
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.valueOf("text/html;charset=UTF-8"));
        stringHttpMessageConverter.setSupportedMediaTypes(list);
        return stringHttpMessageConverter;
    }

    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter(){
        FastJsonHttpMessageConverterEx fastJsonHttpMessageConverter = new FastJsonHttpMessageConverterEx();
        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.APPLICATION_JSON_UTF8);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(list);
        return fastJsonHttpMessageConverter;
    }



//    @Override
//    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
//        super.configureHandlerExceptionResolvers(exceptionResolvers);
//        SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
//        Properties props = new Properties();
//        props.setProperty("java.lang.Throwable", "error/500");
//
//        resolver.setExceptionMappings(props);
//        exceptionResolvers.add(resolver);
//    }


//    @Bean(name = {"multipartResolver"})
//    public MultipartResolver multipartResolver() {
//        CommonsMultipartResolver cmr = new CommonsMultipartResolver();
//        cmr.setMaxInMemorySize(20*1024*1024);
//        return cmr;
//    }



    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .maxAge(3600);
    }




}
