package com.duan.blogos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * Created on 2018/9/14.
 *
 * @author DuanJiaNing
 */
@SpringBootApplication
@ImportResource("classpath:spring/spring-*.xml")
public class BlogOSApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogOSApiApplication.class, args);
    }
}
