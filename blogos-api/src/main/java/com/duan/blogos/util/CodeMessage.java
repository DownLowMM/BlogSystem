package com.duan.blogos.util;

/**
 * Created on 2018/9/13.
 *
 * @author DuanJiaNing
 */
public enum CodeMessage {

    INVALID_REQUEST("invalid.request", 100),

    INVALID_TOKEN("invalid.token", 101);

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
