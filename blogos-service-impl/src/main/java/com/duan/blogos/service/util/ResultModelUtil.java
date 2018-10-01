package com.duan.blogos.service.util;

import com.duan.blogos.service.restful.PageResult;
import com.duan.blogos.service.restful.ResultModel;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created on 2018/10/1.
 *
 * @author DuanJiaNing
 */
public class ResultModelUtil {

    public static <R> ResultModel<PageResult<R>> pageResult(PageInfo<?> pageInfo, List<R> result) {
        PageResult<R> pageResult = new PageResult<>();
        pageResult.setList(result);
        pageResult.setTotal(Math.toIntExact(pageInfo.getTotal()));
        pageResult.setTotalPage(pageInfo.getPages());
        pageResult.setCurrentPage(pageInfo.getPageNum());
        pageResult.setPageSize(pageInfo.getPageSize());

        return new ResultModel<>(pageResult);
    }


    public static <R> PageResult<R> page(PageInfo<?> pageInfo, List<R> result) {
        PageResult<R> pageResult = new PageResult<>();
        pageResult.setList(result);
        pageResult.setTotal(Math.toIntExact(pageInfo.getTotal()));
        pageResult.setTotalPage(pageInfo.getPages());
        pageResult.setCurrentPage(pageInfo.getPageNum());
        pageResult.setPageSize(pageInfo.getPageSize());

        return pageResult;
    }


}
