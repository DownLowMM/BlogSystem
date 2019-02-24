package com.duan.blogos.service;


import com.duan.blogos.service.common.dto.blog.BlogSortRuleDTO;
import com.duan.blogos.service.common.restful.ResultModel;

import java.util.List;

/**
 * Created on 2018/2/12.
 *
 * @author DuanJiaNing
 */
public interface BlogSortRuleService {

    /**
     * 获得所有的排序规则
     *
     * @return 结果
     */
    ResultModel<List<BlogSortRuleDTO>> listSortRule();

    /**
     * 获得排序顺序
     *
     * @return 结果
     */
    ResultModel<List<BlogSortRuleDTO>> listSortOrder();

}
