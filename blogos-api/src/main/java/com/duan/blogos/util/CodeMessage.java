package com.duan.blogos.util;

/**
 * Created on 2018/9/13.
 *
 * @author DuanJiaNing
 */
public enum CodeMessage {

    /**
     * token 缺失
     */
    TOKEN_REQUIRED(100),

    /**
     * 无效 token
     */
    INVALID_TOKEN(101),

    /**
     * 未知错误
     */
    COMMON_UNKNOWN_ERROR(5010),

    /**
     * 请求参数缺失
     */
    COMMON_MISSING_REQUEST_PARAMETER(5011),

    /**
     * 图片不存在
     */
    COMMON_UNKNOWN_PICTURE(5012),

    /**
     * 链接不存在
     */
    COMMON_UNKNOWN_LINK(5013),

    /**
     * 未授权
     */
    COMMON_UNAUTHORIZED(5014),

    /**
     * 未知类别
     */
    COMMON_UNKNOWN_CATEGORY(5015),

    /**
     * 未登录
     */
    BLOGGER_NOT_LOGGED_IN(5016),

    /**
     * 博文内容违规
     */
    BLOG_ILLEGAL(5017),

    /**
     * 重复数据
     */
    COMMON_DUPLICATION_DATA(5018),

    /**
     * 参数格式不正确
     */
    COMMON_PARAMETER_FORMAT_ILLEGAL(5019),

    /**
     * 参数类型不正确
     */
    COMMON_PARAMETER_TYPE_MISMATCH(5020),

    /**
     * 图片格式错误
     */
    COMMON_PICTURE_FORMAT_ERROR(5021),

    /**
     * 账号不存在
     */
    BLOGGER_UNKNOWN_ACCOUNT(5022),

    /**
     * 密码错误
     */
    BLOGGER_PASSWORD_INCORRECT(5023),

// info -------------------------------------------------------------------------------------------------//

    /**
     * 用户反馈
     */
    COMMON_FEEDBACK_TITLE(3000),

    /**
     * 博主不存在
     */
    BLOGGER_UNKNOWN_BLOGGER(5001),

    /**
     * 博文不存在
     */
    BLOG_UNKNOWN_BLOG(5002),

    /**
     * 博文排序规则未定义
     */
    BLOG_BLOG_SORT_RULE_UNDEFINED(5003),

    /**
     * 排序顺序未定义
     */
    BLOG_BLOG_SORT_ORDER_UNDEFINED(5004),

    /**
     * 字符串参数未按指定字符间隔
     */
    COMMON_PARAMETER_STRING_SPLIT_ILLEGAL(5005),

    /**
     * 数据是空的
     */
    COMMON_EMPTY_RESULT(5006),

    /**
     * 操作失败
     */
    COMMON_OPERATE_FAIL(5007),

    /**
     * 参数不正确
     */
    COMMON_PARAMETER_ILLEGAL(5008),

    /**
     * 未指明操作
     */
    COMMON_UNSPECIFIED_OPERATION(5009);

    private final int code;

    CodeMessage(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
