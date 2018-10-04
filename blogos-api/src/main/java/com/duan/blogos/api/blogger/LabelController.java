package com.duan.blogos.api.blogger;

import com.duan.blogos.annonation.TokenNotRequired;
import com.duan.blogos.annonation.Uid;
import com.duan.blogos.api.BaseController;
import com.duan.blogos.service.dto.blog.BlogLabelDTO;
import com.duan.blogos.service.restful.PageResult;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerLabelService;
import com.duan.common.spring.verify.Rule;
import com.duan.common.spring.verify.annoation.parameter.ArgVerify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created on 2018/1/12.
 * 博文标签API，标签的使用不限定博主，即只要标签存在，任何博主都可以使用
 * <p>
 * 1 查看所有标签
 * 2 查看指定标签
 * 3 修改标签
 * 4 删除标签
 * 5 增加标签
 * 6 获取指定博主创建的标签
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/label")
public class LabelController extends BaseController {

    @Autowired
    private BloggerLabelService bloggerLabelService;

    /**
     * 查看所有标签
     */
    @GetMapping("/all")
    @TokenNotRequired
    public ResultModel<PageResult<BlogLabelDTO>> get(@RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                     @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        ResultModel<PageResult<BlogLabelDTO>> resultModel = bloggerLabelService.listLabel(pageNum, pageSize);
        if (resultModel == null) handlerEmptyResult();

        return resultModel;
    }

    /**
     * 获取指定标签
     */
    @GetMapping("/{labelId}")
    @TokenNotRequired
    public ResultModel<BlogLabelDTO> getLabel(@PathVariable("labelId") Long labelId) {

        BlogLabelDTO label = bloggerLabelService.getLabel(labelId);
        if (label == null) handlerEmptyResult();

        return ResultModel.success(label);
    }

    /**
     * 获取指定博主创建的标签
     */
    @GetMapping
    @TokenNotRequired
    public ResultModel<PageResult<BlogLabelDTO>> list(@RequestParam Long bloggerId,
                                                      @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                      @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        handleAccountCheck(bloggerId);

        ResultModel<PageResult<BlogLabelDTO>> result = bloggerLabelService.listLabelByBlogger(bloggerId, pageNum, pageSize);
        if (result == null) handlerEmptyResult();

        return result;
    }

    /**
     * 新增标签
     */
    @PostMapping
    public ResultModel add(@Uid Long bloggerId,
                           @ArgVerify(rule = Rule.NOT_BLANK)
                           @RequestParam("title") String title) {

        Long id = bloggerLabelService.insertLabel(bloggerId, title);
        if (id == null) handlerOperateFail();

        return ResultModel.success(id);
    }

    /**
     * 修改标签
     */
    @PutMapping("/{labelId}")
    public ResultModel update(@Uid Long bloggerId, @PathVariable Long labelId,
                              @ArgVerify(rule = Rule.NOT_BLANK)
                              @RequestParam("title") String newTitle) {

        boolean result = bloggerLabelService.updateLabel(labelId, bloggerId, newTitle);
        if (!result) handlerOperateFail();

        return ResultModel.success();
    }

    /**
     * 删除标签
     */
    @DeleteMapping("/{labelId}")
    public ResultModel delete(@Uid Long bloggerId, @PathVariable Long labelId) {
        handleAccountCheck(bloggerId);
        boolean result = bloggerLabelService.deleteLabel(bloggerId, labelId);
        if (!result) handlerOperateFail();

        return ResultModel.success();
    }

}
