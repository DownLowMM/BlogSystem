package com.duan.blogos.service.impl.common;

import com.alibaba.dubbo.config.annotation.Service;
import com.duan.blogos.service.common.Order;
import com.duan.blogos.service.common.Rule;
import com.duan.blogos.service.dto.blog.BlogSortRuleDTO;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.common.BlogSortRuleService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 2018/2/12.
 *
 * @author DuanJiaNing
 */
@Service
public class BlogSortRuleServiceImpl implements BlogSortRuleService {

    @Override
    public ResultModel<List<BlogSortRuleDTO>> listSortRule() {

        List<BlogSortRuleDTO> list = new ArrayList<>();
        Arrays.stream(Rule.values()).forEach(rule -> list.add(new BlogSortRuleDTO(rule.name().toLowerCase(), rule.title())));

        return ResultModel.success(list);
    }

    @Override
    public ResultModel<List<BlogSortRuleDTO>> listSortOrder() {

        List<BlogSortRuleDTO> list = new ArrayList<>();
        Arrays.stream(Order.values()).forEach(order -> list.add(new BlogSortRuleDTO(order.name().toLowerCase(), order.title())));

        return ResultModel.success(list);
    }
}
