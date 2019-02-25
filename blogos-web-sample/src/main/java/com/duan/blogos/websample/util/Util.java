package com.duan.blogos.websample.util;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.duan.blogos.service.common.util.TokenUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Created on 2018/10/14.
 *
 * @author DuanJiaNing
 */
public class Util {

    public static String getCookie(String key) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(key)) {
                return cookie.getValue();
            }
        }

        return null;

    }

    public static String getToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("token");
        if (token == null) {
            token = request.getParameter("token");
        }

        if (token == null) {
            token = getCookie("token");
        }

        return token;
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
        return getUid(getToken());
    }

    public static String decodeBase64(String str) {
        Charset utf8 = StandardCharsets.UTF_8;
        byte[] de1 = Base64.getUrlDecoder().decode(str.getBytes(utf8));
        byte[] de2 = Base64.getDecoder().decode(de1);
        return new String(de2, utf8);
    }
}
