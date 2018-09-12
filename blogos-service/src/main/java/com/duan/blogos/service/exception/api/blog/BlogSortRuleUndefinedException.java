package com.duan.blogos.service.exception.api.blog;


import com.duan.blogos.service.exception.BaseRuntimeException;

/**
 * Created on 2017/12/20.
 * 博文排序规则未定义
 * <p>
 * 可用的排序规则定义在{@link com.duan.blogos.service.common.Rule}中
 *
 * @author DuanJiaNing
 */
public class BlogSortRuleUndefinedException extends BaseRuntimeException {

    public static final int code = 13;

    public BlogSortRuleUndefinedException(String message) {
        super(message, code);
    }

    public BlogSortRuleUndefinedException() {
        super(code);
    }
}
