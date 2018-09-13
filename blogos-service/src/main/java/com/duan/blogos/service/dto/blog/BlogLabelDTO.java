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
public class BlogLabelDTO implements Serializable {

    //id
    private Integer id;

    //所属博主id
    private Integer bloggerId;

    //标题
    private String title;

    //创建时间
    private Timestamp createDate;


}
