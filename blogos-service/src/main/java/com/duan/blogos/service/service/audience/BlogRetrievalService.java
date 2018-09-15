package com.duan.blogos.service.service.audience;


import com.duan.blogos.service.dto.blog.BlogListItemDTO;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.BlogFilter;

import java.util.List;

/**
 * Created on 2017/12/14.
 * 博文检索并排序服务
 * <p>
 * 1 全限定检索
 * 2 关键字检索
 * 3 类别检索
 * 4 标签检索
 *
 * @author DuanJiaNing
 */
public interface BlogRetrievalService extends BlogFilter<ResultModel<List<BlogListItemDTO>>> {

}
