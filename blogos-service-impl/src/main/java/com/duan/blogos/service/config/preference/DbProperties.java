package com.duan.blogos.service.config.preference;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created on 2017/12/21.
 * 数据库配置参数
 *
 * @author DuanJiaNing
 */
@Data
@ConfigurationProperties(prefix = "preference.db")
@Configuration
public class DbProperties {

    /**
     * 数据库数字间隔字符
     */
    private String stringFiledSplitCharacterForNumber;

    /**
     * 数据库字符串间隔字符
     */
    private String stringFiledSplitCharacterForString;

}
