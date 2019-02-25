package com.duan.blogos.service.common.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Created on 2019/2/25.
 *
 * @author DuanJiaNing
 */
public class Utils {

    public static String decodeUrlBase64(String str) {
        Charset utf8 = StandardCharsets.UTF_8;
        byte[] de1 = Base64.getUrlDecoder().decode(str.getBytes(utf8));
        byte[] de2 = Base64.getDecoder().decode(de1);
        return new String(de2, utf8);
    }

    public static String encodeUrlBase64(String str) {
        Charset utf8 = StandardCharsets.UTF_8;
        byte[] en1 = Base64.getEncoder().encode(str.getBytes(utf8));
        return Base64.getUrlEncoder().encodeToString(en1);// 特殊字符
    }
}
