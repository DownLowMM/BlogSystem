package com.duan.blogos.api.blogger.favorite;

import com.alibaba.dubbo.config.annotation.Reference;
import com.duan.blogos.annonation.TokenNotRequired;
import com.duan.blogos.annonation.Uid;
import com.duan.blogos.api.BaseController;
import com.duan.blogos.service.common.BlogSortRule;
import com.duan.blogos.service.common.Order;
import com.duan.blogos.service.common.Rule;
import com.duan.blogos.service.dto.blogger.FavoriteBlogListItemDTO;
import com.duan.blogos.service.restful.PageResult;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerCollectBlogService;
import com.duan.blogos.util.CodeMessage;
import com.duan.blogos.util.ExceptionUtil;
import org.springframework.web.bind.annotation.*;

/**
 * Created on 2018/1/9.
 * 博主收藏博文
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/blogger/collect")
public class CollectBlogController extends BaseController {

    @Reference
    private BloggerCollectBlogService bloggerCollectBlogService;

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
        ResultModel<PageResult<FavoriteBlogListItemDTO>> result = bloggerCollectBlogService.listCollectBlog(bloggerId,
                null, pageNum, pageSize, BlogSortRule.valueOf(sor, ord));
        if (result == null) handlerEmptyResult();

        return result;
    }

    /**
     * 修改博文收藏
     */
    @PutMapping("/{blogId}")
    public ResultModel update(@PathVariable("blogId") Long blogId,
                              @Uid Long bloggerId,
                              @RequestParam String reason) {

        boolean result = bloggerCollectBlogService.updateCollect(bloggerId, blogId, reason, null);
        if (!result) handlerOperateFail();

        return ResultModel.success();
    }

}
