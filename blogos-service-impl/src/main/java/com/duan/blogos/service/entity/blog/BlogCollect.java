package com.duan.blogos.service.entity.blog;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created on 2017/12/12.
 * 收藏博文
 *
 * @author DuanJiaNing
 */
@Data
public class BlogCollect implements Serializable {

    private static final long serialVersionUID = 2508868745231893082L;

    // 记录id
    private Long id;

    // 博文id
    private Long blogId;

    //收藏者id
    private Long collectorId;

    //收藏的理由
    private String reason;

    //收藏时间
    private Timestamp collectDate;

    //收藏到自己的哪一个类别之下
    private Long categoryId;

}
