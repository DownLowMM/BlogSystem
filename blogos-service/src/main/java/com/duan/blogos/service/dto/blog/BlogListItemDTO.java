package com.duan.blogos.service.dto.blog;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created on 2017/12/16.
 * 博文列表项
 * 博主的博文以列表项的形式将每一篇博文的主要信息展现给读者
 *
 * @author DuanJiaNing
 */
@Data
public class BlogListItemDTO implements Serializable {

    //博文id
    private Long id;

    //博文所属类别
    private BlogCategoryDTO[] categories;

    //博文标签
    private BlogLabelDTO[] labels;

    //状态
    private Integer state;

    //博文标题
    private String title;

    //博文摘要
    private String summary;

    //博文图片
    private String img;

    //首次发布日期
    private Timestamp releaseDate;

    //最近修改日期
    private Timestamp nearestModifyDate;

    // 统计数据
    private BlogBaseStatisticsDTO statistics;

}
