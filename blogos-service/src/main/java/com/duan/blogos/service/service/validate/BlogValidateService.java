package com.duan.blogos.service.service.validate;

/**
 * Created on 2018/9/13.
 *
 * @author DuanJiaNing
 */
public interface BlogValidateService {

    /**
     * 检查博文是否存在
     *
     * @param blogId 博文id
     * @return 博文存在返回true
     */
    boolean checkBlogExist(int blogId);

    /**
     * 检查标签是否存在
     *
     * @param labelId 标签id
     * @return 存在返回true
     */
    boolean checkLabelsExist(int labelId);

    /**
     * 检查博主是否为当前博文的创作者
     *
     * @param bloggerId 博主id
     * @param blogId    博文id
     * @return 是返回true
     */
    boolean isCreatorOfBlog(int bloggerId, int blogId);

    /**
     * 检验博文是否合法
     *
     * @param title    博文标题
     * @param content  博文内容
     * @param summary  摘要
     * @param keyWords 关键字
     * @return 合法返回true
     */
    boolean verifyBlog(String title, String content, String contentMd, String summary, String keyWords);

    /**
     * 检查目标博文状态是否允许，一般用户只允许在“公开”，“私有”，“回收站”之间切换。
     *
     * @param status 状态值
     * @return 允许返回true
     */
    boolean isBlogStatusAllow(int status);

    /**
     * 检测博主是否有指定博文的统计信息记录
     *
     * @param bloggerId 博主id
     * @param blogId    博文id
     * @return 有返回true
     */
    boolean isCreatorOfBlogStatistic(int bloggerId, int blogId);

    /**
     * 检查博文的统计信息是否存在
     *
     * @param blogId 博文id
     * @return 存在返回true
     */
    boolean checkBlogStatisticExist(int blogId);

}
