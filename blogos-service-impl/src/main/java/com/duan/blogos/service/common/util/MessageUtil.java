package com.duan.blogos.service.common.util;

import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * Created on 2018/10/20.
 *
 * @author DuanJiaNing
 */
public class MessageUtil {

    public static String getMessage(String code) {
        return getMessage(code, null);
    }

    public static String getMessage(String code, Object... args) {
        MessageSource messageSource = SpringUtil.getBean(MessageSource.class);
        return messageSource.getMessage(code, args, Locale.getDefault());
    }
}
