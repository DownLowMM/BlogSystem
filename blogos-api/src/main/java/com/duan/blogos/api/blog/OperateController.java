package com.duan.blogos.api.blog;

import com.duan.blogos.api.BaseController;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ExceptionUtil;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.audience.BlogOperateService;
import com.duan.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created on 2017/12/25.
 * 读者对博文可以进行的操作
 * <p>
 * 1 分享博文
 * 2 收藏博文
 * 3 投诉博文
 * 4 喜欢博文
 * 5 取消收藏
 * 6 取消喜欢
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/blog/{blogId}")
public class OperateController extends BaseController {

    @Autowired
    private BlogOperateService operateService;

    /**
     * 分享博文
     */
    @PostMapping("/operate=share")
    public ResultModel shareBlog(@PathVariable Long blogId,
                                 @PathVariable Long bloggerId) {
        handleBlogExistCheck(blogId);

        //执行
        int count = operateService.insertShare(blogId, bloggerId);

        return new ResultModel<>(count);
    }

    /**
     * 收藏博文
     */
    @PostMapping("/operate=collect")
    public ResultModel collectBlog(@PathVariable Long blogId,
                                   @PathVariable Long bloggerId,
                                   @RequestParam(value = "reason", required = false) String reason) {

        handleBlogExistCheck(blogId);

        // 如果博文属于当前博主，收藏失败d
        if (blogValidateService.isCreatorOfBlog(bloggerId, blogId)) {
            handlerOperateFail();
        }

        //执行
        // UPDATE: 2018/1/19 更新 收藏到自己的某一类别不开发，只收藏到一个类别中
        Long id = operateService.insertCollect(blogId, bloggerId, reason, null);
        if (id == null) handlerOperateFail();

        return new ResultModel<>(id);
    }

    /**
     * 投诉博文
     */
    @PostMapping("/operate=complain")
    public ResultModel complainBlog(@PathVariable Long blogId,
                                    @PathVariable Long bloggerId,
                                    @RequestParam("content") String content) {
        if (StringUtils.isBlank(content))
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);

        handleBlogExistCheck(blogId);

        //执行
        Long id = operateService.insertComplain(blogId, bloggerId, content);
        if (id == null) handlerOperateFail();

        return new ResultModel<>(id);
    }

    /**
     * 喜欢博文
     */
    @PostMapping("/operate=like")
    public ResultModel likeBlog(@PathVariable Long blogId,
                                @PathVariable Long bloggerId) {

        handleBlogExistCheck(blogId);

        //执行
        int count = operateService.insertLike(blogId, bloggerId);

        return new ResultModel<>(count);
    }

    /**
     * 取消收藏
     */
    @DeleteMapping("/operate=collect")
    public ResultModel removeCollect(@PathVariable Long blogId,
                                     @PathVariable Long bloggerId) {

        handleBlogExistCheck(blogId);

        //执行
        boolean result = operateService.deleteCollect(bloggerId, blogId);
        if (!result) handlerOperateFail();

        return new ResultModel<>("");

    }

    /**
     * 取消喜欢
     */
    @DeleteMapping("/operate=like")
    public ResultModel removeLike(@PathVariable Long blogId,
                                  @PathVariable Long bloggerId) {
        handleBlogExistCheck(blogId);

        //执行
        boolean result = operateService.deleteLike(bloggerId, blogId);
        if (!result) handlerOperateFail();

        return new ResultModel<>("");
    }

}
