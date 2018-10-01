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

    private Integer total;
    private List<T> list;
    private Integer totalPage;
    private Integer currentPage;
    private Integer pageSize;

    public PageResult() {
    }

    public PageResult(int total, List<T> list) {
        this.total = total;
        this.list = list;
    }

    public PageResult(int total, List<T> list, int totalPage) {
        this.total = total;
        this.list = list;
        this.totalPage = totalPage;
    }

    public PageResult(int total, List<T> list, int totalPage, int currentPage) {
        this.total = total;
        this.list = list;
        this.totalPage = totalPage;
        this.currentPage = currentPage;
    }

}
