package com.duan.blogos.service.restful;

import com.duan.blogos.service.exception.BlogOSException;
import lombok.Data;

import java.io.Serializable;

/**
 * Created on 2017/12/13.
 * restful 风格 API 返回结果固定结构
 *
 * @author DuanJiaNing
 */

@Data
public class ResultModel<T> implements Serializable {

    /**
     * 结果状态为成功
     */
    public static final int SUCCESS = 200;

    /**
     * 结果状态为失败
     */
    public static final int FAIL = 500;
    public int code = SUCCESS;
    private String msg = "success";
    private T data;

    public ResultModel() {
    }

    /**
     * 返回成功的构造函数
     */
    public ResultModel(T data) {
        this.data = data;
    }

    /**
     * 返回错误（获取数据错误）的构造函数
     */
    public ResultModel(BlogOSException e) {
        this.msg = e.getMessage();
        this.code = e.getCode();
    }

    /**
     * 自定义信息和代码
     */
    public ResultModel(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public static ResultModel success() {
        return new ResultModel();
    }

    public static <T> ResultModel success(T data) {
        ResultModel<T> model = new ResultModel<>();
        model.setData(data);
        model.setCode(SUCCESS);
        return model;
    }

    public static ResultModel fail() {
        ResultModel model = new ResultModel("fail", FAIL);
        return model;
    }

}
