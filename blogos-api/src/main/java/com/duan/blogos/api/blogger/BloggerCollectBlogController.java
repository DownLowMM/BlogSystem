package com.duan.blogos.api.blogger;

import com.duan.blogos.service.common.BlogSortRule;
import com.duan.blogos.service.common.Order;
import com.duan.blogos.service.common.Rule;
import com.duan.blogos.service.dto.blogger.FavouriteBlogListItemDTO;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ResultUtil;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerCollectBlogService;
import com.duan.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created on 2018/1/9.
 * 博主收藏博文
 * <p>
 * 1 收藏博文清单
 * 2 取消博文收藏
 * 3 修改博文收藏
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/blogger/{bloggerId}/collect")
public class BloggerCollectBlogController extends BaseBloggerController {

    @Autowired
    private BloggerCollectBlogService bloggerCollectBlogService;

    /**
     * 收藏博文清单
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResultModel<List<FavouriteBlogListItemDTO>> list(HttpServletRequest request,
                                                            @PathVariable("bloggerId") Integer bloggerId,
                                                            @RequestParam(value = "offset", required = false) Integer offset,
                                                            @RequestParam(value = "rows", required = false) Integer rows,
                                                            @RequestParam(value = "sort", required = false) String sort,
                                                            @RequestParam(value = "order", required = false) String order) {
        final RequestContext context = new RequestContext(request);
        handleAccountCheck(request, bloggerId);

        //检查数据合法性
        String sor = sort == null ? Rule.VIEW_COUNT.name() : sort.toUpperCase();
        String ord = order == null ? Order.DESC.name() : order.toUpperCase();
        if (!Rule.contains(sor))
            throw ResultUtil.failException(CodeMessage.BLOG_BLOG_SORT_RULE_UNDEFINED);

        if (!Order.contains(ord))
            throw ResultUtil.failException(CodeMessage.BLOG_BLOG_SORT_ORDER_UNDEFINED);

        // 查询数据
        ResultModel<List<FavouriteBlogListItemDTO>> result = bloggerCollectBlogService.listCollectBlog(bloggerId,
                -1, offset == null ? 0 : offset, rows == null ? -1 : rows,
                BlogSortRule.valueOf(sor, ord));
        if (result == null) handlerEmptyResult();

        return result;
    }

    /**
     * 修改博文收藏
     */
    @RequestMapping(value = "/{blogId}", method = RequestMethod.PUT)
    public ResultModel update(HttpServletRequest request,
                              @PathVariable("blogId") Integer blogId,
                              @PathVariable("bloggerId") Integer bloggerId,
                              @RequestParam(value = "reason", required = false) String newReason) {

        handleBloggerSignInCheck(request, bloggerId);

        if (StringUtils.isEmpty(newReason)) {
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);
        }

        boolean result = bloggerCollectBlogService.updateCollect(bloggerId, blogId, newReason, -1);
        if (!result) handlerOperateFail();

        return new ResultModel<>("");
    }


    /**
     * 统计收藏收藏量
     */
    @RequestMapping("/count")
    public ResultModel count(HttpServletRequest request,
                             @PathVariable("bloggerId") Integer bloggerId) {

        handleAccountCheck(request, bloggerId);

        return new ResultModel<>(bloggerCollectBlogService.countByBloggerId(bloggerId));
    }
}
