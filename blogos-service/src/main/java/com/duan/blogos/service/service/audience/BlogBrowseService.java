package com.duan.blogos.service.service.audience;


import com.duan.blogos.service.dto.blog.BlogCommentDTO;
import com.duan.blogos.service.dto.blog.BlogMainContentDTO;
import com.duan.blogos.service.restful.ResultModel;

import java.util.List;

/**
 * Created on 2017/12/14.
 * 博文浏览服务
 * <p>
 * 1 获得博文主体信息
 * 2 获得博文评论列表
 *
 * @author DuanJiaNing
 */
public interface BlogBrowseService {

    /**
     * 获得博文主体信息
     *
     * @param blogId 博文id
     * @return 查询结果
     */
    ResultModel<BlogMainContentDTO> getBlogMainContent(int blogId);

    /**
     * 获得博文评论列表，这里获取的是审核通过的评论
     *
     * @param blogId 博文id
     * @param offset 结果集起始位置
     * @param rows   行数
     * @return 查询结果
     */
    ResultModel<List<BlogCommentDTO>> listBlogComment(int blogId, int offset, int rows);

}
