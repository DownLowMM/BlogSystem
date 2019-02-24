package com.duan.blogos.service.common.dto.blogger;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created on 2017/12/14.
 * 博主资料
 *
 * @author DuanJiaNing
 */
@Data
public class BloggerDTO implements Serializable {

    //id
    private Long id;

    //个人资料
    private BloggerProfileDTO profile;

    //博主头像（需要单独从相册中查询）
    private BloggerPictureDTO avatar;

    //用户名
    private String username;

    //注册时间
    private Timestamp registerDate;

}
