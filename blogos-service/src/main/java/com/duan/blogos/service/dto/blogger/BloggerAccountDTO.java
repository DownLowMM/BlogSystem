package com.duan.blogos.service.dto.blogger;

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

    //id 和前端 json 交换数据时 long 会丢失精度，用 String
    private Long id;

    //用户名
    private String username;

    //密码
    private String password;

    //注册时间
    private Timestamp registerDate;

}
