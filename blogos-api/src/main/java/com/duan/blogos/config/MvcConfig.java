package com.duan.blogos.config;

import com.duan.blogos.config.interceptor.IpInterceptor;
import com.duan.blogos.config.interceptor.TokenInterceptor;
import com.duan.blogos.config.resolver.CurrentUserHandlerMethodArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Created on 2018/9/23.
 *
 * @author DuanJiaNing
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //   registry.addInterceptor(new ApiSignCheckIntercept());
        registry.addInterceptor(new IpInterceptor());
        registry.addInterceptor(new TokenInterceptor());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new CurrentUserHandlerMethodArgumentResolver());
    }


}
