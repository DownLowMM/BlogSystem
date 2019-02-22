package com.duan.blogos.util;

/**
 * Created on 2019/2/22.
 *
 * @author DuanJiaNing
 */
public class Util {

    public static boolean isArrayEmpty(int[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isArrayEmpty(Object[] arr) {
        return arr == null || arr.length == 0;
    }

}
