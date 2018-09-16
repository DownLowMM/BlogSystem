package com.duan.blogos.api.blogger;

import com.duan.base.util.common.StringUtils;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ResultUtil;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerCommentService;
import com.duan.blogos.service.service.validate.BlogCommentValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.duan.blogos.service.enums.BlogCommentStatusEnum.RIGHTFUL;

/**
 * Created on 2018/3/13.
 *
 * @author DuanJiaNing
 */

@RestController
@RequestMapping("/blogger/{bloggerId}/comment")
public class BloggerCommentController extends BaseBloggerController {

    @Autowired
    private BlogCommentValidateService commentValidateService;

    @Autowired
    private BloggerCommentService commentService;

    /**
     * 新增评论
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResultModel add(HttpServletRequest request,
                           @PathVariable Integer bloggerId,
                           @RequestParam("blogId") Integer blogId,
                           @RequestParam("content") String content,
                           @RequestParam("listenerId") Integer listenerId) {
        handleBloggerSignInCheck(request, bloggerId);
        handleBlogExistCheck(request, blogId);
        handleBloggerExist(request, listenerId);

        if (StringUtils.isBlank(content) || !commentValidateService.checkCommentContent(content))
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);

        int id = commentService.insertComment(blogId, bloggerId, listenerId, RIGHTFUL.getCode(), content);
        if (id < 0) handlerOperateFail();

        return new ResultModel<>(id);
    }

    /**
     * 删除评论
     */
    @RequestMapping(value = "/{commentId}", method = RequestMethod.DELETE)
    public ResultModel delete(HttpServletRequest request,
                              @RequestParam("blogId") Integer blogId,
                              @PathVariable Integer bloggerId,
                              @PathVariable Integer commentId) {
        handleBloggerSignInCheck(request, bloggerId);
        handleBlogExistCheck(request, blogId);

        if (!commentService.deleteComment(commentId, blogId))
            handlerOperateFail();

        return new ResultModel<>("");
    }

    private void handleBloggerExist(HttpServletRequest request, Integer bloggerId) {
        if (!bloggerValidateService.checkAccountExist(bloggerId)) {
            throw ResultUtil.failException(CodeMessage.BLOGGER_UNKNOWN_BLOGGER);
        }
    }

}
