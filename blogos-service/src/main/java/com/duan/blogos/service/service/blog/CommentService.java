package com.duan.blogos.service.service.blog;


import com.duan.blogos.service.dto.blog.BlogCommentDTO;
import com.duan.blogos.service.restful.PageResult;
import com.duan.blogos.service.restful.ResultModel;

/**
 * Created on 2017/12/14.
 * 博文评论服务
 *
 * @author DuanJiaNing
 */
public interface CommentService {

    /**
     * 获得博文评论列表，这里获取的是审核通过的评论
     *
     * @param blogId 博文id
     * @return 查询结果
     */
    ResultModel<PageResult<BlogCommentDTO>> listBlogComment(Long blogId, Integer pageSize, Integer pageNum);

}
