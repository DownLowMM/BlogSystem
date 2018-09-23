package com.duan.blogos.util;

/**
 * Created on 2018/9/13.
 *
 * @author DuanJiaNing
 */
public class CurrentUserThreadLocal {

    private static final ThreadLocal<Long> currentUid = new ThreadLocal<>();
    private static final ThreadLocal<String> currentLocale = new ThreadLocal<>();
    private static final ThreadLocal<String> currentIp = new ThreadLocal<>();

    public static String getCurrentIp() {
        return currentIp.get();
    }

    public static void setCurrentIp(String ip) {
        currentIp.set(ip);
    }

    public static Long getCurrentUid() {
        return currentUid.get();
    }

    public static void setCurrentUid(Long uid) {
        currentUid.set(uid);
    }

    public static String getCurrentLocale() {
        return currentLocale.get();
    }

    public static void setCurrentLocale(String locale) {
        currentLocale.set(locale);
    }
}