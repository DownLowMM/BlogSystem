package com.duan.blogos.api.blogger;

import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ResultUtil;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerCommentService;
import com.duan.blogos.service.service.validate.BlogCommentValidateService;
import com.duan.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ResultModel add(@PathVariable Long bloggerId,
                           @RequestParam("blogId") Long blogId,
                           @RequestParam("content") String content,
                           @RequestParam("listenerId") Long listenerId) {
        handleBlogExistCheck(blogId);
        handleAccountCheck(listenerId);

        if (StringUtils.isBlank(content) || !commentValidateService.checkCommentContent(content))
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);

        Long id = commentService.insertComment(blogId, bloggerId, listenerId, RIGHTFUL.getCode(), content);
        if (id == null) handlerOperateFail();

        return new ResultModel<>(id);
    }

    /**
     * 删除评论
     */
    @RequestMapping(value = "/{commentId}", method = RequestMethod.DELETE)
    public ResultModel delete(@RequestParam("blogId") Long blogId,
                              @PathVariable Long bloggerId,
                              @PathVariable Long commentId) {
        handleBlogExistCheck(blogId);

        if (!commentService.deleteComment(commentId, blogId))
            handlerOperateFail();

        return new ResultModel<>("");
    }

}
