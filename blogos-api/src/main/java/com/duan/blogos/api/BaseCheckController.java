package com.duan.blogos.api;

import com.duan.blogos.service.common.Order;
import com.duan.blogos.service.common.Rule;
import com.duan.blogos.service.enums.BloggerPictureCategoryEnum;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ExceptionUtil;
import com.duan.blogos.service.service.validate.BlogValidateService;
import com.duan.blogos.service.service.validate.BloggerValidateService;
import com.duan.common.util.CollectionUtils;
import com.duan.common.util.StringUtils;
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
     * 检查博主是否存在
     */
    protected void handleAccountCheck(Long bloggerId) {
        if (bloggerId == null || bloggerId <= 0 || !bloggerValidateService.checkAccountExist(bloggerId)) {
            throw ExceptionUtil.get(CodeMessage.BLOGGER_UNKNOWN_BLOGGER);
        }
    }

    /**
     * 检查博文是否存在
     */
    protected void handleBlogExistCheck(Long blogId) {
        if (blogId == null || blogId <= 0 || !blogValidateService.checkBlogExist(blogId)) {
            throw ExceptionUtil.get(CodeMessage.BLOG_UNKNOWN_BLOG);
        }
    }

    /**
     * 检查指定博主是否拥有图片
     */
    protected void handlePictureExistCheck(Long bloggerId, Long pictureId) {
        if (pictureId != null && !bloggerValidateService.checkBloggerPictureExist(bloggerId, pictureId))
            throw ExceptionUtil.get(CodeMessage.COMMON_UNKNOWN_PICTURE);
    }

    /**
     * 检查用户名合法性
     */
    protected void handleNameCheck(String userName) {
        if (StringUtils.isBlank(userName) || !bloggerValidateService.checkUserName(userName))
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);
    }

    /**
     * 检查博主是否拥有指定类别和标签
     */
    protected void handleCategoryAndLabelCheck(Long bloggerId, Long[] cids, Long[] lids) {

        if (!CollectionUtils.isEmpty(cids)) {
            for (Long id : cids) {
                if (!bloggerValidateService.checkBloggerBlogCategoryExist(bloggerId, id))
                    throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);
            }
        }

        if (!CollectionUtils.isEmpty(lids)) {
            for (Long id : lids) {
                if (!blogValidateService.checkLabelsExist(id))
                    throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);
            }
        }

    }

    /**
     * 检查排序规则
     */
    protected void handleSortRuleCheck(String sort, String order) {

        if (sort != null && !Rule.contains(sort)) {
            throw ExceptionUtil.get(CodeMessage.BLOG_BLOG_SORT_RULE_UNDEFINED);
        }

        if (order != null && !Order.contains(order)) {
            throw ExceptionUtil.get(CodeMessage.BLOG_BLOG_SORT_ORDER_UNDEFINED);
        }
    }

    /**
     * 检查博文的统计信息是否存在
     */
    protected void handleBlogStatisticsExistCheck(Long blogId) {
        if (!blogValidateService.checkBlogStatisticExist(blogId))
            throw ExceptionUtil.get(CodeMessage.BLOG_UNKNOWN_BLOG);
    }

    /**
     * 检查博文是否存在，且博文是否属于指定博主
     */
    protected void handleBlogExistAndCreatorCheck(Long bloggerId, Long blogId) {
        if (!blogValidateService.isCreatorOfBlog(bloggerId, blogId))
            throw ExceptionUtil.get(CodeMessage.BLOG_UNKNOWN_BLOG);
    }

    /**
     * 博文内容检查
     */
    protected void handleBlogContentCheck(String title, String content, String contentMd, String summary, String keyWords) {
        if (!blogValidateService.verifyBlog(title, content, contentMd, summary, keyWords))
            throw ExceptionUtil.get(CodeMessage.BLOG_ILLEGAL);
    }

    /**
     * 检查指定博主是否有指定的博文类别
     */
    protected void handleCategoryExistCheck(Long bloggerId, Long categoryId) {
        if (!bloggerValidateService.checkBloggerBlogCategoryExist(bloggerId, categoryId))
            throw ExceptionUtil.get(CodeMessage.COMMON_UNKNOWN_CATEGORY);
    }

    /**
     * 检查 base64url 图片格式
     */
    protected void handleImageBase64Check(String base64urlData) {
        if (!base64urlData.contains("data:image") || !base64urlData.contains("base64")) {
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_FORMAT_ILLEGAL);
        }

    }

    /**
     * 检查手机和邮箱格式
     */
    protected void handlePhoneAndEmailCheck(String phone, String email) {
        if (phone != null && !StringUtils.isPhone(phone))
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_FORMAT_ILLEGAL);

        if (email != null && !StringUtils.isEmail(email))
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_FORMAT_ILLEGAL);
    }

    /**
     * 检查主要导航位置枚举是否存在
     */
    protected void handleMainPageNavPosCheck(Integer mainPageNavPos) {
        if (mainPageNavPos == null || !bloggerValidateService.checkMainPageNavPos(mainPageNavPos)) {
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);
        }
    }

    /**
     * 检查图片类别是否为默认类别
     */
    protected void handleBlogCategoryDefaultCheck(int category) {
        if (!BloggerPictureCategoryEnum.isDefaultPictureCategory(category))
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);
    }

}
