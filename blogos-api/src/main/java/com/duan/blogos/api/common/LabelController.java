package com.duan.blogos.api.common;

import com.duan.blogos.api.BaseCheckController;
import com.duan.blogos.service.dto.blog.BlogLabelDTO;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
public class LabelController extends BaseCheckController {

    @Autowired
    private BloggerLabelService bloggerLabelService;

    /**
     * 查看所有标签
     */
    @GetMapping
    public ResultModel<List<BlogLabelDTO>> get(@RequestParam(value = "offset", required = false) Integer offset,
                                               @RequestParam(value = "rows", required = false) Integer rows) {

        int os = offset == null || offset < 0 ? 0 : offset;
        int rs = rows == null || rows < 0 ? 10 : rows;
        ResultModel<List<BlogLabelDTO>> resultModel = bloggerLabelService.listLabel(os, rs);
        if (resultModel == null) handlerEmptyResult();

        return resultModel;
    }


    /**
     * 获取指定标签
     */
    @GetMapping("/{labelId}")
    public ResultModel<BlogLabelDTO> getLabel(@PathVariable("labelId") Long labelId) {

        BlogLabelDTO label = bloggerLabelService.getLabel(labelId);
        if (label == null) handlerEmptyResult();

        return new ResultModel<>(label);
    }

}
