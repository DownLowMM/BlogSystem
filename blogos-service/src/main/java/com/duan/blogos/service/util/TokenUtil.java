package com.duan.blogos.service.util;


/**
 * Created on 2018/5/30.
 *
 * @author DuanJiaNing
 */
public class TokenUtil {

    private static final String key = "abcdefghijklmnopqrstuvwxyz12";

    private static final String separator = ":";

    private static final String appName = "blogos";

    private static final String SUFFIX = "token";

    /**
     * 获取加密 token
     */
    public static String getToken(Long uid) {
        return AESUtil.aesEncrypt(tokenContent(uid), key);
    }

    /**
     * 获取 token key
     */
    public static String getTokenKey(Long uid) {
        return appName + separator + SUFFIX + separator + uid;
    }

    private static String tokenContent(Long uid) {
        return String.valueOf(System.currentTimeMillis()) + separator + uid;
    }

    /**
     * 解密
     */
    public static Long decode(String token) throws Exception {

        String str = new String(token.getBytes("UTF-8"), "ISO-8859-1");

        // 如果解密失败抛出异常
        String decodeStr = AESUtil.aesDecrypt(str, key);
        String[] content = decodeStr.split(separator);

        String time = content[0];
        String uid = content[1];

        return Long.valueOf(uid);

    }

}
