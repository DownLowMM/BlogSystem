package com.duan.blogos.api.blogger;

import com.duan.blogos.service.dto.blogger.BloggerLinkDTO;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ResultUtil;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerLinkService;
import com.duan.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created on 2017/12/28.
 * 博主友情链接api
 * <p>
 * 1 获取链接
 * 2 新增链接
 * 3 更新链接
 * 4 删除链接
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/blogger/{bloggerId}/link")
public class BloggerLinkController extends BaseBloggerController {

    @Autowired
    private BloggerLinkService bloggerLinkService;

    /**
     * 获取链接
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResultModel<List<BloggerLinkDTO>> get(HttpServletRequest request,
                                                 @PathVariable Integer bloggerId,
                                                 @RequestParam(value = "offset", required = false) Integer offset,
                                                 @RequestParam(value = "rows", required = false) Integer rows) {

        handleAccountCheck(request, bloggerId);

        ResultModel<List<BloggerLinkDTO>> result = bloggerLinkService.listBloggerLink(bloggerId,
                offset == null ? 0 : offset, rows == null ? -1 : rows);
        if (result == null) handlerEmptyResult();

        return result;
    }

    /**
     * 新增链接
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResultModel add(HttpServletRequest request,
                           @PathVariable Integer bloggerId,
                           @RequestParam(value = "iconId", required = false) Integer iconId,
                           @RequestParam("title") String title,
                           @RequestParam("url") String url,
                           @RequestParam(value = "bewrite", required = false) String bewrite) {
        handleBloggerSignInCheck(request, bloggerId);
        handlePictureExistCheck(request, bloggerId, iconId);

        //检查title和url规范
        if (StringUtils.isEmpty(title) || !StringUtils.isURL(url))
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);

        int id = bloggerLinkService.insertBloggerLink(bloggerId, iconId == null ? -1 : iconId, title, url, bewrite);
        if (id <= 0) handlerOperateFail();

        return new ResultModel<>(id);
    }

    /**
     * 更新链接
     */
    @RequestMapping(value = "/{linkId}", method = RequestMethod.PUT)
    public ResultModel update(HttpServletRequest request,
                              @PathVariable Integer bloggerId,
                              @PathVariable Integer linkId,
                              @RequestParam(value = "iconId", required = false) Integer newIconId,
                              @RequestParam(value = "title", required = false) String newTitle,
                              @RequestParam(value = "url", required = false) String newUrl,
                              @RequestParam(value = "bewrite", required = false) String newBewrite) {
        RequestContext context = new RequestContext(request);

        //都为null则无需更新
        if (newIconId == null && newTitle == null && newUrl == null && newBewrite == null) {
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);
        }

        handleBloggerSignInCheck(request, bloggerId);
        handlePictureExistCheck(request, bloggerId, newIconId);
        checkLinkExist(linkId, context);

        //检查url规范
        if (newUrl != null && !StringUtils.isURL(newUrl)) {
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);
        }

        boolean result = bloggerLinkService.updateBloggerLink(linkId, newIconId == null ? -1 : newIconId, newTitle, newUrl, newBewrite);
        if (!result) handlerOperateFail();

        return new ResultModel<>("");
    }

    /**
     * 删除链接
     */
    @RequestMapping(value = "/{linkId}", method = RequestMethod.DELETE)
    public ResultModel delete(HttpServletRequest request,
                              @PathVariable Integer bloggerId,
                              @PathVariable Integer linkId) {
        handleBloggerSignInCheck(request, bloggerId);
        RequestContext context = new RequestContext(request);
        checkLinkExist(linkId, context);

        boolean result = bloggerLinkService.deleteBloggerLink(linkId);
        if (!result) handlerOperateFail();

        return new ResultModel<>("");
    }


    //检查链接是否存在
    private void checkLinkExist(Integer linkId, RequestContext context) {
        if (linkId == null || linkId <= 0 || !bloggerLinkService.getLinkForCheckExist(linkId)) {
            throw ResultUtil.failException(CodeMessage.COMMON_UNKNOWN_LINK);
        }
    }

}
