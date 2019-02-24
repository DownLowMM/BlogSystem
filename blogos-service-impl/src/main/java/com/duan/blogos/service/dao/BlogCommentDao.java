package com.duan.blogos.service.dao;

import com.duan.blogos.service.dao.BaseDao;
import com.duan.blogos.service.entity.BlogComment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created on 2017/12/25.
 *
 * @author DuanJiaNing
 */
@Repository
public interface BlogCommentDao extends BaseDao<BlogComment> {

    /**
     * 根据博文id查询评论
     *
     * @param blogId 博文id
     * @param status 博文状态
     * @return 查询结果
     */
    List<BlogComment> listCommentByBlogId(@Param("blogId") Long blogId,
                                          @Param("status") int status);

    /**
     * 根据博文id获得所有针对该博文的评论
     *
     * @param blogId 博文id
     * @return 查询结果
     */
    List<BlogComment> listAllCommentByBlogId(Long blogId);

    /**
     * 获取评论
     *
     * @param commentId 评论 id
     * @return 查询结果
     */
    BlogComment getCommentById(Long commentId);

}
