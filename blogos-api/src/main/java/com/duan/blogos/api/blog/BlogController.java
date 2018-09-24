package com.duan.blogos.api.blog;

import com.duan.blogos.service.common.BlogSortRule;
import com.duan.blogos.service.common.Order;
import com.duan.blogos.service.common.Rule;
import com.duan.blogos.service.dto.blog.BlogListItemDTO;
import com.duan.blogos.service.dto.blog.BlogMainContentDTO;
import com.duan.blogos.service.enums.BlogStatusEnum;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ResultUtil;
import com.duan.blogos.service.restful.PageResult;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.BlogFilterService;
import com.duan.blogos.service.service.audience.BlogBrowseService;
import com.duan.common.util.CollectionUtils;
import com.duan.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created on 2018/2/4.
 * <p>
 * 1. 检索指定博主的博文列表
 * 2. 获得博文主体内容
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/blog")
public class BlogController extends BaseBlogController {

    @Autowired
    private BlogFilterService blogFilterService;

    @Autowired
    private BlogBrowseService blogBrowseService;

    /**
     * 检索指定博主的博文列表
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResultModel<PageResult<BlogListItemDTO>> list(HttpServletRequest request,
                                                         @RequestParam("bloggerId") Long bloggerId,
                                                         @RequestParam(value = "cids", required = false) String categoryIds,
                                                         @RequestParam(value = "lids", required = false) String labelIds,
                                                         @RequestParam(value = "kword", required = false) String keyWord,
                                                         @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                         @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                         @RequestParam(value = "sort", required = false) String sort,
                                                         @RequestParam(value = "order", required = false) String order) {
        handleAccountCheck(bloggerId);

        //检查数据合法性
        String sor = StringUtils.isBlank(sort) ? Rule.VIEW_COUNT.name() : sort.toUpperCase();
        String ord = StringUtils.isBlank(order) ? Order.DESC.name() : order.toUpperCase();
        handleSortRuleCheck(request, sor, ord);

        String ch = ",";
        Long[] cids = StringUtils.longStringDistinctToArray(categoryIds, ch);
        Long[] lids = StringUtils.longStringDistinctToArray(labelIds, ch);
        //检查博文类别和标签
        handleCategoryAndLabelCheck(request, bloggerId, cids, lids);

        //执行数据查询
        BlogSortRule rule = new BlogSortRule(Rule.valueOf(sor), Order.valueOf(ord));
        return blogFilterService.listFilterAll(cids, lids, keyWord, bloggerId, pageNum, pageSize, rule, BlogStatusEnum.PUBLIC);
    }

    /**
     * 获得博文主体内容
     */
    @RequestMapping(value = "/{blogId}", method = RequestMethod.GET)
    public ResultModel<BlogMainContentDTO> get(HttpServletRequest request,
                                               @PathVariable Long blogId) {
        handleBlogExistCheck(blogId);

        ResultModel<BlogMainContentDTO> mainContent = blogBrowseService.getBlogMainContent(blogId);
        if (mainContent == null) handlerEmptyResult();

        return mainContent;
    }

    // 检查类别和标签
    private void handleCategoryAndLabelCheck(HttpServletRequest request, Long bloggerId, Long[] cids, Long[] lids) {

        if (!CollectionUtils.isEmpty(cids)) {
            for (Long id : cids) {
                if (!bloggerValidateService.checkBloggerBlogCategoryExist(bloggerId, id))
                    throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);
            }
        }

        if (!CollectionUtils.isEmpty(lids)) {
            for (Long id : lids) {
                if (!blogValidateService.checkLabelsExist(id))
                    throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);
            }
        }

    }

    // 检查排序规则
    private void handleSortRuleCheck(HttpServletRequest request, String sort, String order) {

        if (sort != null && !Rule.contains(sort)) {
            throw ResultUtil.failException(CodeMessage.BLOG_BLOG_SORT_RULE_UNDEFINED);
        }

        if (order != null && !Order.contains(order)) {
            throw ResultUtil.failException(CodeMessage.BLOG_BLOG_SORT_ORDER_UNDEFINED);
        }
    }
}
