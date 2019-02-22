package com.duan.blogos.service;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ImportResource;

/**
 * Created on 2018/9/14.
 * dubbo 搭建参考 https://github.com/apache/incubator-dubbo-samples/blob/master/dubbo-samples-annotation/pom.xml
 * @author DuanJiaNing
 */
@SpringBootApplication
@EntityScan("com.duan.blogos.service.entity")
@MapperScan("com.duan.blogos.service.dao")
@ImportResource("classpath:spring/spring-*.xml")
@EnableDubbo(scanBasePackages = "com.duan.blogos.service.impl")
public class BlogOSServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogOSServiceApplication.class, args);

    }

}
