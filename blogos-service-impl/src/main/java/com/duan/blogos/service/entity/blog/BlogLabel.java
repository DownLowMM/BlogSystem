package com.duan.blogos.service.entity.blog;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created on 2017/12/12.
 * 博文标签
 *
 * @author DuanJiaNing
 */
@Data
public class BlogLabel implements Serializable {

    private static final long serialVersionUID = 4565919455090875775L;

    //id
    private Long id;

    //所属博主id
    private Long bloggerId;

    //标题
    private String title;

    //创建时间
    private Timestamp createDate;

}
