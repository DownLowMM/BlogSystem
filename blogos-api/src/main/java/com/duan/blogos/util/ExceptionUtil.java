package com.duan.blogos.util;

import com.duan.blogos.service.exception.BlogOSException;

/**
 * Created on 2018/9/13.
 *
 * @author DuanJiaNing
 */
public class ExceptionUtil {

    public static BlogOSException get(CodeMessage cm) {
        String msg = MessageUtil.getMessage(String.valueOf(cm.getCode()));
        return new BlogOSException(msg, cm.getCode());
    }

    public static BlogOSException get(CodeMessage cm, Throwable e) {
        String msg = MessageUtil.getMessage(String.valueOf(cm.getCode()));
        return new BlogOSException(msg, cm.getCode(), e);
    }

}
