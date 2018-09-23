package com.duan.blogos.service.entity.blogger;

import lombok.Data;

import java.io.Serializable;

/**
 * Created on 2017/12/12.
 * 博主个人信息
 *
 * @author DuanJiaNing
 */
@Data
public class BloggerProfile implements Serializable {

    private static final long serialVersionUID = -1116962500544770692L;

    //id
    private Long id;

    //博主id
    private Long bloggerId;

    //博主头像
    private Long avatarId;

    //电话
    private String phone;

    //邮箱
    private String email;

    //关于我
    private String aboutMe;

    //一句话简介
    private String intro;

}
