package com.duan.blogos.websample.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created on 2019/3/2.
 *
 * @author DuanJiaNing
 */
@Data
public class BloggerModel implements Serializable {

    private PageOwnerBloggerVO pageOwnerBlogger;

    private LoginBloggerVO loginBlogger;

}
