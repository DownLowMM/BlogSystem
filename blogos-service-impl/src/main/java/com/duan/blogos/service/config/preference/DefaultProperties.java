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
@ConfigurationProperties(prefix = "preference.default")
@Configuration
public class DefaultProperties {

    /**
     * 读者默认请求博主博文列表时默认返回的条目数量
     */
    private Integer blogCount;

    /**
     * 默认返回的博主博文评论数量
     */
    private Integer commentCount;

    /**
     * 博主友情链接默认请求条数
     */
    private Integer linkCount;

    /**
     * 博主相册图片默认请求数量
     */
    private Integer pictureCount;

    /**
     * 博主收藏博文默认请求条数
     */
    private Integer collectCount;

    /**
     * 博主收藏博文默认请求条数
     */
    private Integer categoryCount;

    /**
     * 博文标签默认请求条数
     */
    private Integer labelCount;

    /**
     * 默认收藏到博主的该类别博文下
     */
    private Integer blogCategoryId;

}
