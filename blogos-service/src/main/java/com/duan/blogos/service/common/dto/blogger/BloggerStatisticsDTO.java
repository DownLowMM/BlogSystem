package com.duan.blogos.service.common.dto.blogger;

import lombok.Data;

import java.io.Serializable;

/**
 * Created on 2017/12/15.
 * 博主信息统计
 *
 * @author DuanJiaNing
 */
@Data
public class BloggerStatisticsDTO implements Serializable {

    //发表的博文数
    private Integer blogCount;

    //总字数
    private Integer wordCount;

    //收获的喜欢数
    private Integer likeCount;

    //送出的喜欢数
    private Integer likedCount;

    //创建的博文类别数
    private Integer categoryCount;

    //创建的标签数
    private Integer labelCount;

    //收藏的博文数
    private Integer collectCount;

    //文章被收藏数
    private Integer collectedCount;

    //链接数量
    private Integer linkCount;
}
