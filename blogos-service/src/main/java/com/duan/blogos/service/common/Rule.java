package com.duan.blogos.service.common;

/**
 * Created on 2017/12/16.
 * 排序依据
 *
 * @author DuanJiaNing
 */
public enum Rule {

    /**
     * 评论次数
     */
    COMMENT_COUNT("评论次数", "comment_count"),

    /**
     * 博文浏览次数
     */
    VIEW_COUNT("浏览次数", "view_count"),

    /**
     * 博文字数
     */
    WORD_COUNT("字数", "word_count"),

    /**
     * 博文被收藏次数
     */
    COLLECT_COUNT("收藏次数", "collect_count"),

    /**
     * 喜欢次数
     */
    LIKE_COUNT("喜欢次数", "like_count"),

    /**
     * 最初发布日期
     */
    RELEASE_DATE("发布日期", "release_date"),

    /**
     * 博文举报次数
     */
    COMPLAIN_COUNT("举报次数", "complain_count"),

    /**
     * 博文被分享次数
     */
    SHARE_COUNT("分享次数", "share_count"),

    /**
     * 博主回复该博文次数
     */
    REPLY_COMMENT_COUNT("评论回复次数", "reply_comment_count"),

    /**
     * 赞赏次数
     */
    // UPDATE: 2018/2/12 保留但不使用
    ADMIRE_COUNT("赞赏次数", "admire_count");

    private final String title;
    private final String code;

    Rule(String title, String code) {
        this.title = title;
        this.code = code;
    }

    /**
     * 检查是否存在与给定名字对应的枚举成员
     *
     * @param name 名字必须与某个枚举成员名完全相同
     * @return 存在返回 true，否则false
     */
    public static boolean contains(String name) {
        for (Rule rule : values()) {
            if (rule.name().equals(name)) return true;
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
