package com.duan.blogos.service.common.enums;

import java.io.Serializable;

/**
 * Created on 2017/12/16.
 * 排序顺序
 *
 * @author DuanJiaNing
 */
public enum Order implements Serializable {

    /**
     * 升序
     */
    ASC("升序", "asc"),

    /**
     * 降序
     */
    DESC("降序", "desc");

    private final String title;
    private final String code;

    Order(String title, String code) {
        this.title = title;
        this.code = code;
    }

    public static boolean contains(String name) {
        for (Order order : values()) {
            if (order.name().equals(name)) return true;
        }

        return false;
    }

    public String title() {
        return title;
    }

    public String getCode() {
        return code;
    }
}
