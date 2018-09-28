package com.duan.blogos.service.exception;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;

/**
 * Created on 2018/9/13.
 *
 * @author DuanJiaNing
 */
public class ExceptionUtil {


    public static BlogOSException get(CodeMessage cm) {
        HttpServletRequest request = getRequest();
        String msg = "unknown error";
        if (request != null) {
            msg = new RequestContext(getRequest()).getMessage(String.valueOf(cm.getCode()));
        }

        return new BlogOSException(msg, cm.getCode());
    }

    public static BlogOSException get(CodeMessage cm, Throwable e) {
        HttpServletRequest request = getRequest();
        String msg = "unknown error";
        if (request != null) {
            msg = new RequestContext(getRequest()).getMessage(String.valueOf(cm.getCode()));
        }

        return new BlogOSException(msg, cm.getCode(), e);
    }

    private static HttpServletRequest getRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) attributes)
                    .getRequest();
        }

        return null;
    }

}
