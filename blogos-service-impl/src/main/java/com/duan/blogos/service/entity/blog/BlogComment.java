package com.duan.blogos.service.entity.blog;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created on 2017/12/12.
 * 博文评论
 *
 * @author DuanJiaNing
 */
@Data
public class BlogComment implements Serializable {

    private static final long serialVersionUID = -7031768607524908823L;

    //id
    private Long id;

    //博文id
    private Long blogId;

    //评论者id
    private Long spokesmanId;

    //被评论者id
    private Long listenerId;

    //评论内容
    private String content;

    //评论时间
    private Timestamp releaseDate;

    //评论状态
    private Integer state;

}
