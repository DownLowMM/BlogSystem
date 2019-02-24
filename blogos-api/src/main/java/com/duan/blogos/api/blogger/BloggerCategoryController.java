package com.duan.blogos.api.blogger;

import com.alibaba.dubbo.config.annotation.Reference;
import com.duan.blogos.annonation.TokenNotRequired;
import com.duan.blogos.api.BaseController;
import com.duan.blogos.service.blogger.BloggerCategoryService;
import com.duan.blogos.service.common.dto.blogger.BloggerCategoryDTO;
import com.duan.blogos.service.common.restful.PageResult;
import com.duan.blogos.service.common.restful.ResultModel;
import com.duan.blogos.util.CodeMessage;
import com.duan.blogos.util.ExceptionUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Created on 2018/1/11.
 * 博主博文类别
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/blogger/{bloggerId}/category")
public class BloggerCategoryController extends BaseController {

    @Reference
    private BloggerCategoryService bloggerCategoryService;

    /**
     * 查看所有类别
     */
    @GetMapping
    @TokenNotRequired
    public ResultModel<PageResult<BloggerCategoryDTO>> list(
            @PathVariable Long bloggerId,
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize) {
        handleAccountCheck(bloggerId);

        ResultModel<PageResult<BloggerCategoryDTO>> result = bloggerCategoryService.listBlogCategory(bloggerId,
                pageNum, pageSize);
        if (result == null) handlerEmptyResult();

        return result;
    }


    /**
     * 查看指定类别
     */
    @GetMapping("/{categoryId}")
    @TokenNotRequired
    public ResultModel<BloggerCategoryDTO> get(
            @PathVariable Long bloggerId,
            @PathVariable Long categoryId) {

        handleAccountCheck(bloggerId);
        handleCategoryExistCheck(bloggerId, categoryId);

        BloggerCategoryDTO dto = bloggerCategoryService.getCategory(bloggerId, categoryId);
        if (dto == null) handlerOperateFail();

        return ResultModel.success(dto);
    }


    /**
     * 增加类别
     */
    @PostMapping
    public ResultModel add(@PathVariable Long bloggerId,
                           @RequestParam(required = false) Long iconId,
                           @RequestParam String title,
                           @RequestParam(required = false) String bewrite) {

        handlePictureExistCheck(bloggerId, iconId);

        if (StringUtils.isEmpty(title))
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);

        Long id = bloggerCategoryService.insertBlogCategory(bloggerId, iconId, title, bewrite);
        if (id == null) handlerOperateFail();

        return ResultModel.success(id);
    }

    /**
     * 修改类别
     */
    @PutMapping("/{categoryId}")
    public ResultModel update(@PathVariable Long bloggerId,
                              @PathVariable Long categoryId,
                              @RequestParam(value = "iconId", required = false) Long newIconId,
                              @RequestParam(value = "title", required = false) String newTitle,
                              @RequestParam(value = "bewrite", required = false) String newBewrite) {

        /*if (CheckUtil.isAllNull(newIconId, newTitle, newBewrite)) {
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);
        }*/

        handleCategoryExistCheck(bloggerId, categoryId);
        handlePictureExistCheck(bloggerId, newIconId);

        if (!bloggerCategoryService.updateBlogCategory(bloggerId, categoryId, newIconId, newTitle, newBewrite))
            handlerOperateFail();

        return ResultModel.success();
    }

    /**
     * 删除类别，类别被删除后该类别下的所有博文将被移动到指定类别，不指定将移动到默认类别。
     * 不能同时删除类别下的所有文章，删除博文通过博文api操控。
     */
    @DeleteMapping("/{categoryId}")
    public ResultModel delete(@PathVariable Long bloggerId,
                              @PathVariable Long categoryId,
                              @RequestParam(required = false) Long newCategoryId) {

        handleCategoryExistCheck(bloggerId, categoryId);

        Long cate = null;
        if (newCategoryId != null) {

            //检查删除类别和原博文移动到类别是否相同
            if (newCategoryId.equals(categoryId))
                throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);

            //检查新类别
            handleCategoryExistCheck(bloggerId, newCategoryId);

            cate = newCategoryId;
        }

        if (!bloggerCategoryService.deleteCategoryAndMoveBlogsTo(bloggerId, categoryId, cate))
            handlerOperateFail();

        return ResultModel.success();
    }


}
