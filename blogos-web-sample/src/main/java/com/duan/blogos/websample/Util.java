package com.duan.blogos.websample;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.duan.blogos.service.util.TokenUtil;
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

}
