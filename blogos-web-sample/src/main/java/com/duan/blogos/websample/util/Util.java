package com.duan.blogos.websample.util;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.duan.blogos.service.common.util.TokenUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on 2018/10/14.
 *
 * @author DuanJiaNing
 */
public class Util {

    public static HttpServletRequest getServletRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getRequest();
    }

    public static HttpServletResponse getServletResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getResponse();
    }

    public static String getCookie(String key) {
        HttpServletRequest request = getServletRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    return cookie.getValue();
                }
            }
        }

        return null;

    }

    public static String getParameter(String name) {
        HttpServletRequest request = getServletRequest();
        String value = request.getHeader(name);

        if (value == null) {
            value = request.getParameter(name);
        }

        if (value == null) {
            value = getCookie(name);
        }

        return value;
    }

    public static String stringToUnicode(String str) {
        if (StringUtils.isBlank(str)) return "";

        StringBuilder builder = new StringBuilder();
        for (char ch : str.toCharArray()) {
            int ich = (int) ch;
            String hex = Integer.toHexString(ich);
            // 形如 \\u3e 或 \\ua 或 \\ua23 的浏览器 html 无法解析
            if (hex.length() == 2) hex = "00" + hex;
            else if (hex.length() == 1) hex = "000" + hex;
            else if (hex.length() == 3) hex = "0" + hex;

            builder.append("\\u").append(hex);
        }

        return builder.toString();
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
        return getUid(getParameter("token"));
    }

}
