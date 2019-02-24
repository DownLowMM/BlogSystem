package com.duan.blogos.api.blog;

import com.alibaba.dubbo.config.annotation.Reference;
import com.duan.blogos.annonation.TokenNotRequired;
import com.duan.blogos.api.BaseCheckController;
import com.duan.blogos.service.common.dto.blog.BlogCommentDTO;
import com.duan.blogos.service.common.restful.PageResult;
import com.duan.blogos.service.common.restful.ResultModel;
import com.duan.blogos.service.blog.CommentService;
import org.springframework.web.bind.annotation.*;

/**
 * Created on 2018/2/4.
 *
 * @author DuanJiaNing
 */
@TokenNotRequired
@RestController("blogCommentController")
@RequestMapping("/blog/{blogId}/comment")
public class CommentController extends BaseCheckController {

    @Reference
    private CommentService commentService;

    /**
     * 获得博文评论列表
     */
    @GetMapping
    public ResultModel<PageResult<BlogCommentDTO>> get(@PathVariable Long blogId,
                                                       @RequestParam(required = false) Integer pageSize,
                                                       @RequestParam(required = false) Integer pageNum) {
        handleBlogExistCheck(blogId);

        ResultModel<PageResult<BlogCommentDTO>> resultModel = commentService.listBlogComment(blogId, pageSize, pageNum);
        if (resultModel == null) handlerEmptyResult();

        return resultModel;
    }

}
