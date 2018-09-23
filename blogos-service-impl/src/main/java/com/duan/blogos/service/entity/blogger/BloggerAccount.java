package com.duan.blogos.service.entity.blogger;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created on 2017/12/12.
 * 博主账户
 *
 * @author DuanJiaNing
 */
@Data
public class BloggerAccount implements Serializable {

    //id
    private Long id;

    //用户名
    private String username;

    //密码
    private String password;

    //注册时间
    private Timestamp registerDate;

}
