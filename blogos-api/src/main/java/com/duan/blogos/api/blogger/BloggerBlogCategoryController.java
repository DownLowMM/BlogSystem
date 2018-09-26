package com.duan.blogos.api.blogger;

import com.duan.blogos.annonation.TokenNotRequired;
import com.duan.blogos.annonation.Uid;
import com.duan.blogos.service.dto.blogger.BloggerCategoryDTO;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ResultUtil;
import com.duan.blogos.service.restful.PageResult;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerCategoryService;
import com.duan.common.spring.verify.Rule;
import com.duan.common.spring.verify.annoation.parameter.ArgVerify;
import com.duan.common.util.CheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Created on 2018/1/11.
 * 博主博文类别
 * <p>
 * 1 查看所有类别
 * 2 查看指定类别
 * 3 增加类别
 * 4 修改类别
 * 5 删除类别
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/blogger/category")
public class BloggerBlogCategoryController extends BaseBloggerController {

    @Autowired
    private BloggerCategoryService bloggerCategoryService;

    /**
     * 查看所有类别
     */
    @GetMapping
    @TokenNotRequired
    public ResultModel<PageResult<BloggerCategoryDTO>> list(
            @RequestParam Long bloggerId,
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
            @RequestParam Long bloggerId,
            @PathVariable Long categoryId) {

        handleAccountCheck(bloggerId);
        handleCategoryExistCheck(bloggerId, categoryId);

        BloggerCategoryDTO dto = bloggerCategoryService.getCategory(bloggerId, categoryId);
        if (dto == null) handlerOperateFail();

        return new ResultModel<>(dto);
    }


    /**
     * 增加类别
     */
    @PostMapping
    public ResultModel add(@Uid Long bloggerId,
                           @RequestParam(value = "iconId", required = false) Long iconId,
                           @ArgVerify(rule = Rule.NOT_BLANK)
                           @RequestParam String title,
                           @RequestParam(value = "bewrite", required = false) String bewrite) {

        handlePictureExistCheck(bloggerId, iconId);

        if (StringUtils.isEmpty(title))
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);

        Long id = bloggerCategoryService.insertBlogCategory(bloggerId, iconId, title, bewrite);
        if (id == null) handlerOperateFail();

        return new ResultModel<>(id);
    }

    /**
     * 修改类别
     */
    @PutMapping("/{categoryId}")
    public ResultModel update(@Uid Long bloggerId,
                              @PathVariable Long categoryId,
                              @RequestParam(value = "iconId", required = false) Long newIconId,
                              @RequestParam(value = "title", required = false) String newTitle,
                              @RequestParam(value = "bewrite", required = false) String newBewrite) {

        if (CheckUtil.isAllNull(newIconId, newTitle, newBewrite)) {
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);
        }

        handleCategoryExistCheck(bloggerId, categoryId);
        handlePictureExistCheck(bloggerId, newIconId);

        if (!bloggerCategoryService.updateBlogCategory(bloggerId, categoryId, newIconId, newTitle, newBewrite))
            handlerOperateFail();

        return new ResultModel<>("");
    }

    /**
     * 删除类别，类别被删除后该类别下的所有博文将被移动到指定类别，不指定将移动到默认类别。
     * 不能同时删除类别下的所有文章，删除博文通过博文api操控。
     */
    @DeleteMapping("/{categoryId}")
    public ResultModel delete(@Uid Long bloggerId,
                              @PathVariable Long categoryId,
                              @RequestParam(value = "newCategoryId", required = false) Long newCategoryId) {

        handleCategoryExistCheck(bloggerId, categoryId);

        Long cate = null;
        if (newCategoryId != null) {

            //检查删除类别和原博文移动到类别是否相同
            if (newCategoryId.equals(categoryId))
                throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);

            //检查新类别
            handleCategoryExistCheck(bloggerId, newCategoryId);

            cate = newCategoryId;
        }

        if (!bloggerCategoryService.deleteCategoryAndMoveBlogsTo(bloggerId, categoryId, cate))
            handlerOperateFail();

        return new ResultModel<>("");
    }


}
