package com.duan.blogos.service.dto.blog;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created on 2018/9/13.
 *
 * @author DuanJiaNing
 */
@Data
public class BlogCategoryDTO implements Serializable {

    //id
    private Long id;

    //类别所属博主id
    private Long bloggerId;

    //类别图标
    private Long iconId;

    //类别标题
    private String title;

    //类别描述
    private String bewrite;

    //类别创建时间
    private Timestamp createDate;

}
