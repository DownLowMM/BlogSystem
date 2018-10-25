package com.duan.blogos;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created on 2018/9/14.
 *
 * @author DuanJiaNing
 */
@SpringBootApplication
@EnableDubboConfiguration
public class BlogOSApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogOSApiApplication.class, args);
    }
}
