package com.duan.blogos.service.common.restful;

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

    private String msg = "success";
    private int code = SUCCESS;
    private T data;

    public ResultModel() {
    }

    /**
     * 返回成功的构造函数
     */
    private ResultModel(T data) {
        this.data = data;
    }

    /**
     * 自定义信息和代码
     */
    private ResultModel(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public static ResultModel success() {
        return new ResultModel();
    }

    public static <T> ResultModel<T> success(T data) {
        ResultModel<T> model = new ResultModel<>(data);
        model.setCode(SUCCESS);
        return model;
    }

    public static ResultModel fail() {
        ResultModel model = new ResultModel("fail", FAIL);
        return model;
    }

    public static ResultModel fail(String msg, int code) {
        ResultModel model = new ResultModel(msg, code);
        return model;
    }

}
