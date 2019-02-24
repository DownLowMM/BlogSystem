package com.duan.blogos.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.duan.blogos.annonation.TokenNotRequired;
import com.duan.blogos.service.blogger.BloggerLabelService;
import com.duan.blogos.service.common.dto.blog.BlogLabelDTO;
import com.duan.blogos.service.common.restful.PageResult;
import com.duan.blogos.service.common.restful.ResultModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 2019/2/24.
 *
 * @author DuanJiaNing
 */
@RestController
public class CommonController extends BaseController {

    @Reference
    private BloggerLabelService bloggerLabelService;

    /**
     * 查看所有标签
     */
    @GetMapping("/label/all")
    @TokenNotRequired
    public ResultModel<PageResult<BlogLabelDTO>> get(@RequestParam(required = false) Integer pageNum,
                                                     @RequestParam(required = false) Integer pageSize) {

        ResultModel<PageResult<BlogLabelDTO>> resultModel = bloggerLabelService.listLabel(pageNum, pageSize);
        if (resultModel == null) handlerEmptyResult();

        return resultModel;
    }

    /**
     * 获取指定标签
     */
    @GetMapping("/label/{labelId}")
    @TokenNotRequired
    public ResultModel<BlogLabelDTO> getLabel(@PathVariable("labelId") Long labelId) {

        BlogLabelDTO label = bloggerLabelService.getLabel(labelId);
        if (label == null) handlerEmptyResult();

        return ResultModel.success(label);
    }

}
