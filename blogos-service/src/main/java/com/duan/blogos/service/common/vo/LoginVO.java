package com.duan.blogos.service.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created on 2018/10/1.
 *
 * @author DuanJiaNing
 */
@Data
public class LoginVO implements Serializable {

    //用户名
    private String username;

    //密码
    private String password;

}
