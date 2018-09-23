package com.duan.blogos.service.restful;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 2018/9/23.
 *
 * @author DuanJiaNing
 */
@Data
public class PageResult<T> implements Serializable {

    private Long total;
    private List<T> list;
    private Long totalPage;
    private Long currentPage;
    private Long pageSize;

    public PageResult() {
    }

    public PageResult(long total, List<T> list) {
        this.total = total;
        this.list = list;
    }

    public PageResult(long total, List<T> list, long totalPage) {
        this.total = total;
        this.list = list;
        this.totalPage = totalPage;
    }

    public PageResult(long total, List<T> list, long totalPage, long currentPage) {
        this.total = total;
        this.list = list;
        this.totalPage = totalPage;
        this.currentPage = currentPage;
    }

}
