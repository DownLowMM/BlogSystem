package com.duan.blogos.service.exception.api.blog;


import com.duan.blogos.service.exception.BaseRuntimeException;

/**
 * Created on 2017/12/20.
 * 未知博文类别
 *
 * @author DuanJiaNing
 */
public class UnknownBlogCategoryException extends BaseRuntimeException {

    public static final int code = 7;

    public UnknownBlogCategoryException(String message) {
        super(message, code);
    }

    public UnknownBlogCategoryException() {
        super(code);
    }
}
