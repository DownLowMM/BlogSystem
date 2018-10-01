package com.duan.blogos.service.dao;

import com.duan.blogos.service.entity.BlogCategoryRela;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created on 2018/9/24.
 *
 * @author DuanJiaNing
 */
@Repository
public interface BlogCategoryRelaDao extends BaseDao<BlogCategoryRela> {

    /**
     * 找出指定博主的所有博文-类别对应关系
     */
    List<BlogCategoryRela> listAllByBloggerIdInCategoryIds(@Param("bloggerId") Long bloggerId,
                                                           @Param("categoryIds") List<Long> categoryIds);

    /**
     * 博文的所有类别
     */
    List<BlogCategoryRela> listAllByBlogId(Long blogId);

    /**
     * 批量插入
     */
    int insertBatch(@Param("relas") List<BlogCategoryRela> relas);

    /**
     * 删除博文的所有类别信息
     */
    int deleteByBlogId(Long blogId);

    /**
     * 查询博主指定类别下的所有博文
     */
    List<BlogCategoryRela> listAllByBloggerIdAndCategoryId(@Param("bloggerId") Long bloggerId, @Param("categoryId") Long categoryId);

    List<BlogCategoryRela> listAllInCategoryIds(List<Long> categoryIds);
}
