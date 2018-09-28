package com.duan.blogos.api.blogger;

import com.duan.blogos.service.dto.blogger.BloggerLinkDTO;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ExceptionUtil;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerLinkService;
import com.duan.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping
    public ResultModel<List<BloggerLinkDTO>> get(@PathVariable Long bloggerId,
                                                 @RequestParam(value = "offset", required = false) Integer offset,
                                                 @RequestParam(value = "rows", required = false) Integer rows) {

        handleAccountCheck(bloggerId);

        ResultModel<List<BloggerLinkDTO>> result = bloggerLinkService.listBloggerLink(bloggerId,
                offset == null ? 0 : offset, rows == null ? -1 : rows);
        if (result == null) handlerEmptyResult();

        return result;
    }

    /**
     * 新增链接
     */
    @PostMapping
    public ResultModel add(@PathVariable Long bloggerId,
                           @RequestParam(value = "iconId", required = false) Long iconId,
                           @RequestParam("title") String title,
                           @RequestParam("url") String url,
                           @RequestParam(value = "bewrite", required = false) String bewrite) {
        handlePictureExistCheck(bloggerId, iconId);

        //检查title和url规范
        if (StringUtils.isEmpty(title) || !StringUtils.isURL(url))
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);

        Long id = bloggerLinkService.insertBloggerLink(bloggerId, iconId == null ? -1 : iconId, title, url, bewrite);
        if (id == null) handlerOperateFail();

        return new ResultModel<>(id);
    }

    /**
     * 更新链接
     */
    @PutMapping("/{linkId}")
    public ResultModel update(@PathVariable Long bloggerId,
                              @PathVariable Long linkId,
                              @RequestParam(value = "iconId", required = false) Long newIconId,
                              @RequestParam(value = "title", required = false) String newTitle,
                              @RequestParam(value = "url", required = false) String newUrl,
                              @RequestParam(value = "bewrite", required = false) String newBewrite) {

        //都为null则无需更新
        if (newIconId == null && newTitle == null && newUrl == null && newBewrite == null) {
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);
        }

        handlePictureExistCheck(bloggerId, newIconId);
        checkLinkExist(linkId);

        //检查url规范
        if (newUrl != null && !StringUtils.isURL(newUrl)) {
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);
        }

        boolean result = bloggerLinkService.updateBloggerLink(linkId, newIconId == null ? -1 : newIconId, newTitle, newUrl, newBewrite);
        if (!result) handlerOperateFail();

        return new ResultModel<>("");
    }

    /**
     * 删除链接
     */
    @DeleteMapping("/{linkId}")
    public ResultModel delete(@PathVariable Long bloggerId,
                              @PathVariable Long linkId) {

        boolean result = bloggerLinkService.deleteBloggerLink(linkId);
        if (!result) handlerOperateFail();

        return new ResultModel<>("");
    }

    // 检查链接是否存在
    private void checkLinkExist(Long linkId) {
        if (linkId == null || linkId <= 0 || !bloggerLinkService.getLinkForCheckExist(linkId)) {
            throw ExceptionUtil.get(CodeMessage.COMMON_UNKNOWN_LINK);
        }
    }

}
