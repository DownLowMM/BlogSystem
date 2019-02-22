package com.duan.blogos;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created on 2018/9/14.
 * dubbo 搭建参考 https://github.com/apache/incubator-dubbo-samples/blob/master/dubbo-samples-annotation/pom.xml
 *
 * @author DuanJiaNing
 */
@SpringBootApplication
@EnableDubbo(scanBasePackages = "com.duan.blogos.api")
public class BlogOSApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogOSApiApplication.class, args);
    }
}
