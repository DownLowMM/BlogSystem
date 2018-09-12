package com.duan.blogos.service.exception.paramter;

import com.duan.blogos.service.exception.BaseRuntimeException;

/**
 * Created on 2017/12/22.
 * 参数格式错误
 *
 * @author DuanJiaNing
 */
public class ParameterFormatIllegalException extends BaseRuntimeException {

    public static final int code = 3;

    public ParameterFormatIllegalException() {
        super(code);
    }

    public ParameterFormatIllegalException(String message) {
        super(message, code);
    }
}
