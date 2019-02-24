package com.duan.blogos.service.common.dto.blogger;

import lombok.Data;

import java.io.Serializable;

/**
 * Created on 2017/12/15.
 * 博主友情链接
 *
 * @author DuanJiaNing
 */
@Data
public class BloggerLinkDTO implements Serializable {

    // id
    private Long id;

    //博主id
    private Long bloggerId;

    //图片
    private BloggerPictureDTO icon;

    //标题
    private String title;

    //url
    private String url;

    //描述
    private String bewrite;

}
