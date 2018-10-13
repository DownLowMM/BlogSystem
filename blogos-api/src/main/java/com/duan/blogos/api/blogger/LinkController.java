package com.duan.blogos.api.blogger;

import com.duan.blogos.annonation.TokenNotRequired;
import com.duan.blogos.annonation.Uid;
import com.duan.blogos.api.BaseController;
import com.duan.blogos.service.dto.blogger.BloggerLinkDTO;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ExceptionUtil;
import com.duan.blogos.service.restful.PageResult;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerLinkService;
import com.duan.common.spring.verify.Rule;
import com.duan.common.spring.verify.annoation.parameter.ArgVerify;
import com.duan.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created on 2017/12/28.
 * 博主友情链接api
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/blogger/link")
public class LinkController extends BaseController {

    @Autowired
    private BloggerLinkService bloggerLinkService;

    /**
     * 获取博主的链接
     */
    @GetMapping
    @TokenNotRequired
    public ResultModel<PageResult<BloggerLinkDTO>> get(@RequestParam Long bloggerId,
                                                       @RequestParam(required = false) Integer pageNum,
                                                       @RequestParam(required = false) Integer pageSize) {

        handleAccountCheck(bloggerId);

        ResultModel<PageResult<BloggerLinkDTO>> result = bloggerLinkService.listBloggerLink(bloggerId, pageNum, pageSize);
        if (result == null) handlerEmptyResult();

        return result;
    }

    /**
     * 新增链接
     */
    @PostMapping
    public ResultModel add(@Uid Long bloggerId,
                           @RequestParam(required = false) Long iconId,
                           @ArgVerify(rule = Rule.NOT_BLANK)
                           @RequestParam String title,
                           @ArgVerify(rule = Rule.NOT_BLANK)
                           @RequestParam String url,
                           @RequestParam(required = false) String bewrite) {
        handlePictureExistCheck(bloggerId, iconId);

        //检查title和url规范
        if (!StringUtils.isURL(url))
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);

        Long id = bloggerLinkService.insertBloggerLink(bloggerId, iconId, title, url, bewrite);
        if (id == null) handlerOperateFail();

        return ResultModel.success(id);
    }

    /**
     * 更新链接
     */
    @PutMapping("/{linkId}")
    public ResultModel update(@Uid Long bloggerId,
                              @PathVariable Long linkId,
                              @RequestParam(required = false) Long iconId,
                              @RequestParam(required = false) String title,
                              @RequestParam(required = false) String url,
                              @RequestParam(required = false) String bewrite) {

        //都为null则无需更新
        if (StringUtils.isBlank(title) && StringUtils.isBlank(url) && StringUtils.isBlank(bewrite)) {
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);
        }

        handlePictureExistCheck(bloggerId, iconId);
        checkLinkExist(linkId);

        //检查url规范
        if (url != null && !StringUtils.isURL(url)) {
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);
        }

        boolean result = bloggerLinkService.updateBloggerLink(linkId, iconId, title, url, bewrite);
        if (!result) handlerOperateFail();

        return ResultModel.success();
    }

    /**
     * 删除链接
     */
    @DeleteMapping("/{linkId}")
    public ResultModel delete(@PathVariable Long linkId) {

        boolean result = bloggerLinkService.deleteBloggerLink(linkId);
        if (!result) handlerOperateFail();

        return ResultModel.success();
    }

    // 检查链接是否存在
    private void checkLinkExist(Long linkId) {
        if (linkId == null || linkId <= 0 || !bloggerLinkService.getLinkForCheckExist(linkId)) {
            throw ExceptionUtil.get(CodeMessage.COMMON_UNKNOWN_LINK);
        }
    }

}
