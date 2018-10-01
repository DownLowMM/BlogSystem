package com.duan.blogos.service.dao;

import com.duan.blogos.service.entity.BlogLabelRela;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created on 2018/9/24.
 *
 * @author DuanJiaNing
 */
@Repository
public interface BlogLabelRelaDao extends BaseDao<BlogLabelRela> {


    /**
     * 找出指定博主的所有博文-类别对应关系
     */
    List<BlogLabelRela> listAllByBloggerIdInLabelIds(@Param("bloggerId") Long bloggerId, @Param("labelIds") List<Long> labelIds);

    /**
     * 博文的所有类别
     */
    List<BlogLabelRela> listAllByBlogId(Long blogId);

    /**
     * 批量插入
     */
    int insertBatch(@Param("relas") List<BlogLabelRela> relas);

    /**
     * 删除博文的所有标签
     */
    int deleteByBlogId(Long blogId);

    List<BlogLabelRela> listAllIdInLabelIds(List<Long> labelIds);
}
