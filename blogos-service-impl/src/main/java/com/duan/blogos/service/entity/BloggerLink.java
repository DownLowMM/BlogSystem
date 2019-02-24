package com.duan.blogos.service.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created on 2017/12/12.
 * 博主链接
 *
 * @author DuanJiaNing
 */
@Data
public class BloggerLink implements Serializable {

    private static final long serialVersionUID = -6606102132213390615L;

    // id
    private Long id;

    //博主id
    private Long bloggerId;

    //图片id
    private Long iconId;

    //标题
    private String title;

    //url
    private String url;

    //描述
    private String bewrite;

}
