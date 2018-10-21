package com.duan.blogos.api.blogger;

import com.alibaba.dubbo.config.annotation.Reference;
import com.duan.blogos.annonation.Uid;
import com.duan.blogos.api.BaseController;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerCommentService;
import com.duan.blogos.service.service.validate.BlogCommentValidateService;
import com.duan.blogos.util.CodeMessage;
import com.duan.blogos.util.ExceptionUtil;
import com.duan.common.spring.verify.Rule;
import com.duan.common.spring.verify.annoation.parameter.ArgVerify;
import org.springframework.web.bind.annotation.*;

import static com.duan.blogos.service.enums.BlogCommentStatusEnum.RIGHTFUL;

/**
 * Created on 2018/3/13.
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/blogger/comment")
public class CommentController extends BaseController {

    @Reference
    private BlogCommentValidateService commentValidateService;

    @Reference
    private BloggerCommentService commentService;

    /**
     * 新增评论
     */
    @PostMapping
    public ResultModel add(@Uid Long bloggerId,
                           @RequestParam("blogId") Long blogId,
                           @ArgVerify(rule = Rule.NOT_BLANK)
                           @RequestParam("content") String content,
                           @RequestParam("listenerId") Long listenerId) {
        handleBlogExistCheck(blogId);
        handleAccountCheck(listenerId);

        if (!commentValidateService.checkCommentContent(content))
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);

        Long id = commentService.insertComment(blogId, bloggerId, listenerId, RIGHTFUL.getCode(), content);
        if (id == null) handlerOperateFail();

        return ResultModel.success(id);
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/{commentId}")
    public ResultModel delete(@Uid Long bloggerId,
                              @PathVariable Long commentId) {
        if (!commentService.deleteComment(commentId, bloggerId))
            handlerOperateFail();

        return ResultModel.success();
    }

}
