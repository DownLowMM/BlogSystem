package com.duan.blogos.service.service.blogger;


import com.duan.blogos.service.common.BlogSortRule;
import com.duan.blogos.service.dto.blogger.FavouriteBlogListItemDTO;
import com.duan.blogos.service.restful.ResultModel;

import java.util.List;

/**
 * Created on 2017/12/18.
 * 博主收藏博文服务
 *
 * @author DuanJiaNing
 */
public interface BloggerCollectBlogService {


    /**
     * 获得博主收藏的博文清单
     *
     * @param bloggerId 博主id
     * @param offset    结果集起始位置
     * @param rows      行数
     * @param sortRule  排序规则，为null则不做约束
     * @return 查询结果
     */
    ResultModel<List<FavouriteBlogListItemDTO>> listCollectBlog(Long bloggerId, Long categoryId,
                                                                int offset, int rows, BlogSortRule sortRule);

    /**
     * 更新收藏信息
     *
     * @param bloggerId   博主id
     * @param blogId      博文id
     * @param newReason   新的收藏理由
     * @param newCategory 新的收藏到类别
     * @return 更新成功返回true
     */
    boolean updateCollect(Long bloggerId, Long blogId, String newReason, Long newCategory);

    /**
     * 获得博文收藏状态
     *
     * @param bloggerId 博主id
     * @param blogId    博文id
     * @return 已收藏为 true
     */
    boolean getCollectState(Long bloggerId, Long blogId);

    /**
     * 统计博主收藏量
     *
     * @param bloggerId 博文id
     * @return 查询结果
     */
    int countByBloggerId(Long bloggerId);
}
