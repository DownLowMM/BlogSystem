package com.duan.blogos.service.dto.blogger;

import com.duan.blogos.service.dto.blog.BlogCategoryDTO;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created on 2017/12/19.
 * 博主博文列表项，用于博主查看自己发布的文章列表
 *
 * @author DuanJiaNing
 */
@Data
public class BlogListItemDTO implements Serializable {

    //博文id
    private Long id;

    //博文所属类别
    private BlogCategoryDTO[] categories;

    //状态
    private Integer state;

    //博文标题
    private String title;

    //博文摘要
    private String summary;

    //首次发布日期
    private Timestamp releaseDate;

    //最近修改日期
    private Timestamp nearestModifyDate;

    //评论次数
    private Integer commentCount;

    //博文浏览次数
    private Integer viewCount;

    //博文被收藏次数
    private Integer collectCount;

    //喜欢次数
    private Integer likeCount;

    //字数
    private Integer wordCount;

    //投诉次数
    private Integer complainCount;

}
