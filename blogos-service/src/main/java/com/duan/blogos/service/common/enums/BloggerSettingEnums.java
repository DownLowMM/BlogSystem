package com.duan.blogos.service.common.enums;

/**
 * Created on 2018/10/4.
 *
 * @author DuanJiaNing
 */
public class BloggerSettingEnums {

    /**
     * 博主主页导航位置
     */
    public enum MainPageNavPos {

        LEFT(0),

        RIGHT(1);

        private final Integer code;

        MainPageNavPos(Integer code) {
            this.code = code;
        }

        public Integer getCode() {
            return code;
        }
    }

}
