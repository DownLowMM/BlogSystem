package com.duan.blogos.service.dto.blog;

import lombok.Data;

import java.io.Serializable;

/**
 * Created on 2017/12/25.
 *
 * @author DuanJiaNing
 */
@Data
public class BlogStatisticsCountDTO implements Serializable {

    // 表id
    private Long id;

    //对应博文id
    private Long blogId;

    //评论次数
    private Integer commentCount;

    //博文浏览次数
    private Integer viewCount;

    //博主回复该博文评论的次数
    private Integer replyCommentCount;

    //博文被收藏次数
    private Integer collectCount;

    //博文举报次数
    private Integer complainCount;

    //博文被分享次数
    private Integer shareCount;

    //赞赏次数
    private Integer admireCount;

    //喜欢次数
    private Integer likeCount;

}
