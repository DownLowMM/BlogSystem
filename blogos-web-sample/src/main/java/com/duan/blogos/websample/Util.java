package com.duan.blogos.websample;

import com.duan.blogos.service.util.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created on 2018/10/14.
 *
 * @author DuanJiaNing
 */
public class Util {

    public static String getToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("token");
        if (token == null) {
            token = request.getParameter("token");
        }

        return token;
    }

    public static Long getUid(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }

        try {
            return TokenUtil.decode(token);
        } catch (Exception e) {
            return null;
        }
    }

    public static Long getUid() {
        return getUid(getToken());
    }

}
