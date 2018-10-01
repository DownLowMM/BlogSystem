package com.duan.blogos.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created on 2018/10/1.
 *
 * @author DuanJiaNing
 */
@Data
public class LoginResultDTO implements Serializable {

    //id
    private Long id;

    //用户名
    private String username;

    //token
    private String token;

    //注册时间
    private Timestamp registerDate;
}
