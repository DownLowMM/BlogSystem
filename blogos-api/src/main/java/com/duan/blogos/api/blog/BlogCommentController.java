package com.duan.blogos.api.blog;

import com.duan.blogos.service.dto.blog.BlogCommentDTO;
import com.duan.blogos.service.restful.PageResult;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.audience.BlogBrowseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created on 2018/2/4.
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/blog/{blogId}/comment")
public class BlogCommentController extends BaseBlogController {

    @Autowired
    private BlogBrowseService blogBrowseService;

    /**
     * 获得博文评论列表
     */
    @GetMapping
    public ResultModel<PageResult<BlogCommentDTO>> get(@PathVariable Long blogId,
                                                       @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                       @RequestParam(value = "pageNum", required = false) Integer pageNum) {
        handleBlogExistCheck(blogId);

        ResultModel<PageResult<BlogCommentDTO>> resultModel = blogBrowseService.listBlogComment(blogId, pageSize, pageNum);
        if (resultModel == null) handlerEmptyResult();

        return resultModel;
    }

}
