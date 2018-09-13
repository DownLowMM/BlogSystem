package com.duan.blogos.service.exception;

/**
 * Created on 2018/9/13.
 *
 * @author DuanJiaNing
 */
public enum CodeMessage {


    BLOGGER_PASSWORD_INCORRECT("blogger.asswordIncorrect", 2),

    BLOGGER_UNKNOWN_ACCOUNT("blogger.unknownAccount", 2),

    COMMON_EMPTY_RESULT("common.emptyResult", 2),

    BLOG_UNKNOWN_BLOG("blog.unknownBlog", 2),

    BLOGGER_UNKNOWN_BLOGGER("blogger.unknownBlogger", 2),

    COMMON_PARAMETER_STRING_SPLIT_ILLEGAL("common.parameterStringSplitIllegal", 2),

    BLOG_BLOG_SORT_RULE_UNDEFINED("blog.blogSortRuleUndefined", 2),

    BLOG_BLOG_SORT_ORDER_UNDEFINED("blog.blogSortOrderUndefined", 2),

    COMMON_PARAMETER_ILLEGAL("common.parameterIllegal", 2),

    COMMON_OPERATE_FAIL("common.operateFail", 2),

    COMMON_UNSPECIFIED_OPERATION("common.unspecifiedOperation", 2),

    COMMON_UNKNOWN_ERROR("common.UnknownError", 2),

    COMMON_MISSING_REQUEST_PARAMETER("common.missingRequestParameter", 2),

    COMMON_UNKNOWN_PICTURE("common.unknownPicture", 2),

    COMMON_UNKNOWN_LINK("common.unknownLink", 2),

    COMMON_UNAUTHORIZED("common.unauthorized", 2),

    COMMON_UNKNOWN_CATEGORY("common.unknownCategory", 2),

    BLOGGER_NOT_LOGGED_IN("blogger.notLoggedIn", 2),

    BLOG_ILLEGAL("blog.illegal", 2),

    COMMON_DUPLICATION_DATA("common.duplicationData", 2),

    COMMON_PARAMETER_FORMAT_ILLEGAL("common.parameterFormatIllegal", 2),

    COMMON_PARAMETER_TYPE_MISMATCH("common.parameterTypeMismatch", 2),

    COMMON_PICTURE_FORMAT_ERROR("common.pictureFormatError", 2);

    private final int code;
    private final String msg;

    CodeMessage(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
