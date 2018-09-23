package com.duan.blogos.service.entity.blogger;

import lombok.Data;

/**
 * Created on 2018/3/26.
 *
 * @author DuanJiaNing
 */
@Data
public class BloggerSetting {

    // id
    private Long id;

    // 博主id
    private Long bloggerId;

    // 博主主页个人信息栏位置，0为左，1为右
    private Integer mainPageNavPos;
}
