package com.duan.blogos.api;

import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ResultUtil;
import com.duan.blogos.service.service.validate.BlogValidateService;
import com.duan.blogos.service.service.validate.BloggerValidateService;
import org.springframework.beans.factory.annotation.Autowired;

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
    protected void handleAccountCheck(Long bloggerId) {
        if (bloggerId == null || bloggerId <= 0 || !bloggerValidateService.checkAccountExist(bloggerId)) {
            throw ResultUtil.failException(CodeMessage.BLOGGER_UNKNOWN_BLOGGER);
        }
    }

    /**
     * 检查博文是否存在,不存在直接抛出异常
     */
    protected void handleBlogExistCheck(Long blogId) {
        if (blogId == null || blogId <= 0 || !blogValidateService.checkBlogExist(blogId)) {
            throw ResultUtil.failException(CodeMessage.BLOG_UNKNOWN_BLOG);
        }
    }

}
