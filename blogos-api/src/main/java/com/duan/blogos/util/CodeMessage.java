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
    INVALID_TOKEN(101);

    private final int code;

    CodeMessage(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
