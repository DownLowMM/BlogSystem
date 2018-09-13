package com.duan.blogos.service.exception;

/**
 * Created on 2018/9/13.
 *
 * @author DuanJiaNing
 */
public class ResultUtil {

    public static BlogOSException failException(CodeMessage cm) {
        String msgCode = cm.getMsg();
        String msg = ""; // TODO 国际化
        return new BlogOSException(msg, cm.getCode());
    }

    public static BlogOSException failException(CodeMessage cm, Throwable e) {
        String msgCode = cm.getMsg();
        String msg = ""; // TODO 国际化
        return new BlogOSException(msg, cm.getCode(), e);
    }

}
