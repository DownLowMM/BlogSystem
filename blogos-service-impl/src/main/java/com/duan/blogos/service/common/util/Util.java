package com.duan.blogos.service.common.util;

import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Created on 2019/2/22.
 *
 * @author DuanJiaNing
 */
public class Util {

    /**
     * 将字符串不可逆转化为 sha 字节数组
     *
     * @param str 字符串
     * @return 转化结果
     */
    public static byte[] toSha(String str) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance("SHA");
        sha.update(str.getBytes());
        return sha.digest();
    }

    public static boolean isArrayEmpty(Object arr[]) {
        return arr == null || arr.length == 0;
    }

    /**
     * 对象数组拼接为字符串
     */
    public static String arrayToString(Object[] arr, String join) {
        if (arr == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        for (Object obj : arr) {
            builder.append(obj).append(join);
        }

        String r = builder.toString();
        int len = join.length();
        int sum = r.length();
        return r.substring(0, sum - len);
    }

    public static String[] stringArrayToArray(String sour, String regex) {
        return StringUtils.isEmpty(sour) ? null : sour.split(regex);
    }

    public static String encodeBase64(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    public static String decodeBase64(byte[] bytes) {
        return new String(Base64.getDecoder().decode(bytes), StandardCharsets.UTF_8);
    }
}
