package com.duan.blogos.service.common.dto.blogger;

import lombok.Data;

import java.io.Serializable;

/**
 * Created on 2018/9/13.
 *
 * @author DuanJiaNing
 */
@Data
public class BloggerSettingDTO implements Serializable {

    // id
    private Long id;

    // 博主id
    private Long bloggerId;

    // 博主主页个人信息栏位置，0为左，1为右
    private Integer mainPageNavPos;

}
