package com.duan.blogos.service.common.exception;

/**
 * Created on 2017/12/20.
 * 不受检查的异常，这些异常将会传递（反映）到调用端以标识输入错误和执行失败的情况。
 *
 * @author DuanJiaNing
 */
public class BlogOSException extends RuntimeException {

    /**
     * 异常对应的编号，子类需要赋一个对应自己错误的唯一值
     */
    protected final int code;

    public BlogOSException(int code) {
        this.code = code;
    }

    public BlogOSException(String message, int code) {
        super(message);
        this.code = code;
    }

    public BlogOSException(String message, int code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
