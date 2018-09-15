package com.duan.blogos.api.blog;

import com.duan.blogos.service.common.BlogSortRule;
import com.duan.blogos.service.common.Order;
import com.duan.blogos.service.common.Rule;
import com.duan.blogos.service.dto.blog.BlogListItemDTO;
import com.duan.blogos.service.dto.blog.BlogMainContentDTO;
import com.duan.blogos.service.enums.BlogStatusEnum;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ResultUtil;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.audience.BlogBrowseService;
import com.duan.blogos.service.service.audience.BlogRetrievalService;
import com.duan.blogos.util.common.CollectionUtils;
import com.duan.blogos.util.common.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    private BlogRetrievalService retrievalService;

    @Autowired
    private BlogBrowseService blogBrowseService;

    /**
     * 检索指定博主的博文列表
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResultModel<List<BlogListItemDTO>> list(HttpServletRequest request,
                                                   @RequestParam("bloggerId") Integer bloggerId,
                                                   @RequestParam(value = "cids", required = false) String categoryIds,
                                                   @RequestParam(value = "lids", required = false) String labelIds,
                                                   @RequestParam(value = "kword", required = false) String keyWord,
                                                   @RequestParam(value = "offset", required = false) Integer offset,
                                                   @RequestParam(value = "rows", required = false) Integer rows,
                                                   @RequestParam(value = "sort", required = false) String sort,
                                                   @RequestParam(value = "order", required = false) String order) {
        handleAccountCheck(request, bloggerId);

        //检查数据合法性
        String sor = StringUtils.isBlank(sort) ? Rule.VIEW_COUNT.name() : sort.toUpperCase();
        String ord = StringUtils.isBlank(order) ? Order.DESC.name() : order.toUpperCase();
        handleSortRuleCheck(request, sor, ord);

        String ch = ",";
        int[] cids = StringUtils.intStringDistinctToArray(categoryIds, ch);
        int[] lids = StringUtils.intStringDistinctToArray(labelIds, ch);
        //检查博文类别和标签
        handleCategoryAndLabelCheck(request, bloggerId, cids, lids);

        //执行数据查询
        BlogSortRule rule = new BlogSortRule(Rule.valueOf(sor), Order.valueOf(ord));
        ResultModel<List<BlogListItemDTO>> listResultModel = retrievalService.listFilterAll(cids, lids, keyWord, bloggerId,
                offset == null ? 0 : offset, rows == null ? 0 : rows, rule, BlogStatusEnum.PUBLIC);

        if (listResultModel == null) handlerEmptyResult();

        return listResultModel;
    }

    /**
     * 获得检索结果数量，只有在发起一次检索后才能获得正确的值
     */
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public ResultModel getCount() {
        return new ResultModel<>(retrievalService.getFilterCount());
    }

    /**
     * 获得博文主体内容
     */
    @RequestMapping(value = "/{blogId}", method = RequestMethod.GET)
    public ResultModel<BlogMainContentDTO> get(HttpServletRequest request,
                                               @PathVariable Integer blogId) {
        handleBlogExistCheck(request, blogId);

        ResultModel<BlogMainContentDTO> mainContent = blogBrowseService.getBlogMainContent(blogId);
        if (mainContent == null) handlerEmptyResult();

        return mainContent;
    }

    // 检查类别和标签
    private void handleCategoryAndLabelCheck(HttpServletRequest request, int bloggerId, int[] cids, int[] lids) {

        if (!CollectionUtils.isEmpty(cids)) {
            for (int id : cids) {
                if (!bloggerValidateService.checkBloggerBlogCategoryExist(bloggerId, id))
                    throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);
            }
        }

        if (!CollectionUtils.isEmpty(lids)) {
            for (int id : lids) {
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
