package com.duan.blogos.service.common.dto.blogger;

import lombok.Data;

import java.io.Serializable;

/**
 * Created on 2018/9/13.
 *
 * @author DuanJiaNing
 */
@Data
public class BloggerProfileDTO implements Serializable {

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
