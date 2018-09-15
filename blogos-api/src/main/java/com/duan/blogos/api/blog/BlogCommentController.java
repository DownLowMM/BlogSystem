package com.duan.blogos.api.blog;

import com.duan.blogos.service.dto.blog.BlogCommentDTO;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.audience.BlogBrowseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    @RequestMapping(method = RequestMethod.GET)
    public ResultModel<List<BlogCommentDTO>> get(HttpServletRequest request,
                                                 @PathVariable Integer blogId,
                                                 @RequestParam(value = "offset", required = false) Integer offset,
                                                 @RequestParam(value = "rows", required = false) Integer rows) {
        handleBlogExistCheck(request, blogId);

        ResultModel<List<BlogCommentDTO>> resultModel = blogBrowseService.listBlogComment(blogId,
                offset == null ? -1 : offset,
                rows == null ? -1 : rows);
        if (resultModel == null) handlerEmptyResult();

        return resultModel;
    }

}
