package com.duan.blogos.service.dto.blogger;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created on 2017/12/15.
 * 博主创建的类别
 *
 * @author DuanJiaNing
 */
@Data
public class BloggerCategoryDTO implements Serializable {

    //id
    private Long id;

    //类别所属博主id
    private Long bloggerId;

    // 类别对应的博文数量
    private Integer count;

    //类别标题
    private String title;

    //类别描述
    private String bewrite;

    //类别创建时间
    private Timestamp createDate;

    //类别对应图标
    private BloggerPictureDTO icon;

}
