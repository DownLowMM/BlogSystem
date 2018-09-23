package com.duan.blogos.service.dao.blog;

import com.duan.blogos.service.dao.BaseDao;
import com.duan.blogos.service.entity.blog.BlogStatistics;
import org.springframework.stereotype.Repository;

/**
 * Created on 2017/12/20.
 *
 * @author DuanJiaNing
 */
@Repository
public interface BlogStatisticsDao extends BaseDao<BlogStatistics> {

    /**
     * 通过唯一约束（博文id）删除记录
     *
     * @param blogId 博文id
     * @return 操作影响行数
     */
    int deleteByUnique(Long blogId);

    /**
     * 查询博文的统计信息
     *
     * @param blogId 对应博文id
     * @return 查询结果
     */
    BlogStatistics getStatistics(Long blogId);

    /**
     * 评论次数加一
     *
     * @param blogId 博文id
     * @return 执行结果
     */
    int updateCommentCountPlus(Long blogId);

    /**
     * 博文浏览次数加一
     *
     * @param blogId 博文id
     * @return 执行结果
     */
    int updateViewCountPlus(Long blogId);

    /**
     * 博主回复该博文评论的次数加一
     *
     * @param blogId 博文id
     * @return 执行结果
     */
    int updateReplyCommentCountPlus(Long blogId);

    /**
     * 博文被收藏次数加一
     *
     * @param blogId 博文id
     * @return 执行结果
     */
    int updateCollectCountPlus(Long blogId);

    /**
     * 博文举报次数加一
     *
     * @param blogId 博文id
     * @return 执行结果
     */
    int updateComplainCountPlus(Long blogId);

    /**
     * 博文被分享次数加一
     *
     * @param blogId 博文id
     * @return 执行结果
     */
    int updateShareCountPlus(Long blogId);

    /**
     * 赞赏次数加一
     *
     * @param blogId 博文id
     * @return 执行结果
     */
    int updateAdmireCountPlus(Long blogId);

    /**
     * 喜欢次数加一
     *
     * @param blogId 博文id
     * @return 执行结果
     */
    int updateLikeCountPlus(Long blogId);

    /**
     * 评论次数减一
     *
     * @param blogId 博文id
     * @return 执行结果
     */
    int updateCommentCountMinus(Long blogId);

    /**
     * 博文浏览次数减一
     *
     * @param blogId 博文id
     * @return 执行结果
     */
    int updateViewCountMinus(Long blogId);

    /**
     * 博主回复该博文评论的次数减一
     *
     * @param blogId 博文id
     * @return 执行结果
     */
    int updateReplyCommentCountMinus(Long blogId);

    /**
     * 博文被收藏次数减一
     *
     * @param blogId 博文id
     * @return 执行结果
     */
    int updateCollectCountMinus(Long blogId);

    /**
     * 博文举报次数减一
     *
     * @param blogId 博文id
     * @return 执行结果
     */
    int updateComplainCountMinus(Long blogId);

    /**
     * 博文被分享次数减一
     *
     * @param blogId 博文id
     * @return 执行结果
     */
    int updateShareCountMinus(Long blogId);

    /**
     * 赞赏次数减一
     *
     * @param blogId 博文id
     * @return 执行结果
     */
    int updateAdmireCountMinus(Long blogId);

    /**
     * 喜欢次数减一
     *
     * @param blogId 博文id
     * @return 执行结果
     */
    int updateLikeCountMinus(Long blogId);


    /**
     * 查询评论次数
     *
     * @param blogId 博文id
     * @return 查询结果
     */
    Integer getCommentCount(Long blogId);

    /**
     * 查询博文浏览次数
     *
     * @param blogId 博文id
     * @return 查询结果
     */
    Integer getViewCount(Long blogId);

    /**
     * 查询博主回复该博文评论的次数
     *
     * @param blogId 博文id
     * @return 查询结果
     */
    Integer getReplyCommentCount(Long blogId);

    /**
     * 查询博文被收藏次数
     *
     * @param blogId 博文id
     * @return 查询结果
     */
    Integer getCollectCount(Long blogId);

    /**
     * 查询博文举报次数
     *
     * @param blogId 博文id
     * @return 查询结果
     */
    Integer getComplainCount(Long blogId);

    /**
     * 查询博文被分享次数
     *
     * @param blogId 博文id
     * @return 查询结果
     */
    Integer getShareCount(Long blogId);

    /**
     * 查询赞赏次数
     *
     * @param blogId 博文id
     * @return 查询结果
     */
    Integer getAdmireCount(Long blogId);

    /**
     * 查询喜欢次数
     *
     * @param blogId 博文id
     * @return 查询结果
     */
    Integer getLikeCount(Long blogId);
}
