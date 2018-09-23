package com.duan.blogos.api;

import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ResultUtil;
import com.duan.blogos.service.service.validate.BlogValidateService;
import com.duan.blogos.service.service.validate.BloggerValidateService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * Created on 2018/2/4.
 *
 * @author DuanJiaNing
 */
public class BaseCheckController extends RestController {

    @Autowired
    protected BlogValidateService blogValidateService;

    @Autowired
    protected BloggerValidateService bloggerValidateService;

    /**
     * 检查博主是否存在，不存在直接抛出异常
     */
    protected void handleAccountCheck(HttpServletRequest request, Integer bloggerId) {
        if (bloggerId == null || bloggerId <= 0 || !bloggerValidateService.checkAccountExist(bloggerId)) {
            throw ResultUtil.failException(CodeMessage.BLOGGER_UNKNOWN_BLOGGER);
        }
    }

    /**
     * 先检查博主是否存在，后检查检查博主是否登录
     * <p>
     * 在API中，一些获取数据的操作是不需要博主登录的，但类似于修改，删除，新增以及关键数据的操纵需要验证身份。
     * <p>
     * 如果验证不通过将直接抛出运行时异常。
     *
     * @param bloggerId 博主id
     */
    protected void handleBloggerSignInCheck(HttpServletRequest request, Integer bloggerId) {
        handleAccountCheck(request, bloggerId);

        // 检查当前登录否
        if (!bloggerValidateService.checkBloggerSignIn(bloggerId))
            throw ResultUtil.failException(CodeMessage.BLOGGER_NOT_LOGGED_IN);
    }

    /**
     * 检查博文是否存在,不存在直接抛出异常
     */
    protected void handleBlogExistCheck(Integer blogId) {
        if (blogId == null || blogId <= 0 || !blogValidateService.checkBlogExist(blogId)) {
            throw ResultUtil.failException(CodeMessage.BLOG_UNKNOWN_BLOG);
        }
    }

}
