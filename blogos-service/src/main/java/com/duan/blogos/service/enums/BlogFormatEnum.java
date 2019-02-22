package com.duan.blogos.service.enums;


/**
 * Created on 2018/4/5.
 * 博文格式
 *
 * @author DuanJiaNing
 */
public enum BlogFormatEnum {

    MD(0),

    HTML(1);

    private final int code;

    BlogFormatEnum(int code) {
        this.code = code;
    }

    public static boolean contains(String type) {
        if (type == null) return false;

        return type.equalsIgnoreCase(MD.name()) || type.equalsIgnoreCase(HTML.name());
    }

    public static BlogFormatEnum get(String type) {
        if (contains(type))
            return BlogFormatEnum.valueOf(type.toUpperCase());
        else return null;
    }

    public int getCode() {
        return code;
    }

}
