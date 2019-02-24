package com.duan.blogos.service.common.dto.blogger;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created on 2018/9/13.
 *
 * @author DuanJiaNing
 */
@Data
public class BloggerAccountDTO implements Serializable {

    //id
    private Long id;

    //用户名
    private String username;

    //token
    private String token;

    //注册时间
    private Timestamp registerDate;

}
