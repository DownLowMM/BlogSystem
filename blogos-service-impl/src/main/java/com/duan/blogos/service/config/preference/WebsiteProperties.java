package com.duan.blogos.service.config.preference;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created on 2018/9/15.
 *
 * @author DuanJiaNing
 */
@Data
@ConfigurationProperties(prefix = "preference.website")
@Configuration
public class WebsiteProperties {

    /**
     * # 拥有唯一图片管理权限的博主的id
     */
    private Integer managerId;

    /**
     * # 网站管理者的邮箱（反馈邮件发送到的邮箱）
     */
    private String managerEmail;

    /**
     * # 默认的博主主页个人信息栏位置，0在左，1在右
     */
    private Integer pageNavPos;

    /**
     * # 配置了smtp 的邮件发送者
     */
    private String senderMail;

    /**
     * # 配置了smtp 的邮件发送者的授权码
     */
    private String senderMailPassword;

    /**
     * # 默认获取活跃博主数
     */
    private Integer activeBloggerCount;

    /**
     * 站点域名
     */
    private String addr;

    /**
     * 默认的url请求参数的间隔字符
     * 如url中传递多个博文类别id时：1,2,3,8 这里间隔字符为","
     */
    private String urlConditionSplitCharacter;
}
