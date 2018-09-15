package com.duan.blogos.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created on 2018/9/15.
 *
 * @author DuanJiaNing
 */
@Data
@ConfigurationProperties(prefix = "preference.website.session")
@Configuration
public class SessionProperties {

    /**
     * 保存在session属性中的博主id对应的名字
     */
    private String bloggerId;

    /**
     * 保存在session属性中的博主用户名对应的名字
     */
    private String bloggerName;

    /**
     * 保存在session属性中的博主登录标识，有值（任意值）就表示已登录
     */
    private String loginSignal;

    /**
     * 保存在session属性中的错误信息对应的名字
     */
    private String errorMsg;

    /**
     * 页面所属博主id
     */
    private String ownerId;

    /**
     * 页面所属博主name
     */
    private String ownerName;
}
