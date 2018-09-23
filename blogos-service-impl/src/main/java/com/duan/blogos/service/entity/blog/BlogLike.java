package com.duan.blogos.service.entity.blog;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created on 2017/12/26.
 * 博文喜欢记录
 *
 * @author DuanJiaNing
 */
@Data
public class BlogLike implements Serializable {

    private static final long serialVersionUID = 7850020610771820655L;

    //记录id
    private Long id;

    //博文id
    private Long blogId;

    //给出喜欢的人的id
    private Long likerId;

    //喜欢时间
    private Timestamp likeDate;

}
