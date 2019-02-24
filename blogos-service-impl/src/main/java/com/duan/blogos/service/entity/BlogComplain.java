package com.duan.blogos.service.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created on 2017/12/14.
 * 博文投诉
 *
 * @author DuanJiaNing
 */
@Data
public class BlogComplain implements Serializable {

    private static final long serialVersionUID = -2651098932919577290L;

    //表id
    private Long id;

    //投诉的博文id
    private Long blogId;

    //投诉者id
    private Long complainerId;

    //投诉内容
    private String content;

    //投诉时间
    private Timestamp time;
}
