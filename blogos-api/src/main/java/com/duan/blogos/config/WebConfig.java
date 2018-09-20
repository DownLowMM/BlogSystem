package com.duan.blogos.config;

import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.common.spring.verify.ServletInvocableHandlerMethodArgumentVerify;
import org.springframework.boot.autoconfigure.web.WebMvcRegistrationsAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

/**
 * Created on 2018/9/14.
 *
 * @author DuanJiaNing
 */
@Configuration
public class WebConfig extends WebMvcRegistrationsAdapter {

    @Override
    public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
        return new RequestMappingHandlerAdapter() {
            @Override
            protected ServletInvocableHandlerMethod createInvocableHandlerMethod(HandlerMethod handlerMethod) {
                return new ServletInvocableHandlerMethodArgumentVerify(handlerMethod) {
                    @Override
                    protected Object warpResultFail(ServletInvocableHandlerMethodArgumentVerify.HandleResult result) {

                        String str = String.format("Argument [%s] verify fail, because [%s]",
                                result.parameter.getParameterName(), result.rule);
                        return new ResultModel(str, CodeMessage.COMMON_PARAMETER_ILLEGAL.getCode());
                    }
                };
            }
        };
    }

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("UTF-8");
        multipartResolver.setMaxUploadSize(10485760000L);
        multipartResolver.setMaxInMemorySize(40960);

        return multipartResolver;
    }

    /**
     * 跨域设置
     */
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");

        return corsConfiguration;
    }

    /**
     * 跨域过滤器
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }

}
