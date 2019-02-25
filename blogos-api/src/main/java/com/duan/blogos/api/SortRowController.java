package com.duan.blogos.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.duan.blogos.service.BlogSortRuleService;
import com.duan.blogos.service.common.dto.blog.BlogSortRuleDTO;
import com.duan.blogos.service.common.restful.ResultModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created on 2018/2/12.
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/sort-rule")
public class SortRowController extends BaseCheckController {

    @Reference
    private BlogSortRuleService sortRuleService;

    /**
     * 获得排序规则
     */
    @GetMapping("/rule")
    public ResultModel<List<BlogSortRuleDTO>> list() {

        ResultModel<List<BlogSortRuleDTO>> result = sortRuleService.listSortRule();
        if (result == null)
            return handlerEmptyResult();

        return result;
    }

    /**
     * 1
     * 获得排序顺序
     */
    @GetMapping("/order")
    public ResultModel<List<BlogSortRuleDTO>> listOrder() {

        ResultModel<List<BlogSortRuleDTO>> result = sortRuleService.listSortOrder();
        if (result == null)
            return handlerEmptyResult();

        return result;
    }
}
