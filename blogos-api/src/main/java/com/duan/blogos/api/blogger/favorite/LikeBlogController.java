package com.duan.blogos.api.blogger.favorite;

import com.alibaba.dubbo.config.annotation.Reference;
import com.duan.blogos.annonation.TokenNotRequired;
import com.duan.blogos.api.BaseController;
import com.duan.blogos.service.blogger.BloggerLikeBlogService;
import com.duan.blogos.service.common.dto.blogger.FavoriteBlogListItemDTO;
import com.duan.blogos.service.common.enums.BlogSortRule;
import com.duan.blogos.service.common.enums.Order;
import com.duan.blogos.service.common.enums.Rule;
import com.duan.blogos.service.common.restful.PageResult;
import com.duan.blogos.service.common.restful.ResultModel;
import com.duan.blogos.util.CodeMessage;
import com.duan.blogos.util.ExceptionUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 2018/1/9.
 * 博主收藏博文
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/blogger/like")
public class LikeBlogController extends BaseController {

    @Reference
    private BloggerLikeBlogService likeBlogService;

    /**
     * 收藏博文清单
     */
    @GetMapping
    @TokenNotRequired
    public ResultModel<PageResult<FavoriteBlogListItemDTO>> list(@RequestParam Long bloggerId,
                                                                 @RequestParam(required = false) Integer pageNum,
                                                                 @RequestParam(required = false) Integer pageSize,
                                                                 @RequestParam(required = false) String sort,
                                                                 @RequestParam(required = false) String order) {
        handleAccountCheck(bloggerId);

        //检查数据合法性
        String sor = sort == null ? Rule.VIEW_COUNT.name() : sort.toUpperCase();
        String ord = order == null ? Order.DESC.name() : order.toUpperCase();
        if (!Rule.contains(sor))
            throw ExceptionUtil.get(CodeMessage.BLOG_BLOG_SORT_RULE_UNDEFINED);

        if (!Order.contains(ord))
            throw ExceptionUtil.get(CodeMessage.BLOG_BLOG_SORT_ORDER_UNDEFINED);

        // 查询数据
        ResultModel<PageResult<FavoriteBlogListItemDTO>> result = likeBlogService.listLikeBlog(bloggerId,
                pageNum, pageSize, BlogSortRule.valueOf(sor, ord));
        if (result == null)
            return handlerEmptyResult();

        return result;
    }

}
