package com.duan.blogos.api;

import com.duan.blogos.service.exception.BlogOSException;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ResultUtil;
import com.duan.blogos.service.restful.ResultBean;
import org.springframework.beans.TypeMismatchException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created on 2017/12/26.
 *
 * @author DuanJiaNing
 */
public class RestController {

    /**
     * 处理结果为空的情况
     */
    protected void handlerEmptyResult() {
        throw ResultUtil.failException(CodeMessage.COMMON_EMPTY_RESULT);
    }

    /**
     * 处理操作失败的情况
     */
    protected void handlerOperateFail() {
        throw ResultUtil.failException(CodeMessage.COMMON_OPERATE_FAIL);
    }

    /**
     * 处理操作失败的情况
     */
    protected void handlerOperateFail(Throwable e) {
        throw ResultUtil.failException(CodeMessage.COMMON_OPERATE_FAIL, e);
    }

    /**
     * 统一处理异常，这些异常需要通知API调用者
     */
    @ExceptionHandler(BlogOSException.class)
    @ResponseBody
    // 注解无法继承，所以子类不允许覆盖这些方法
    protected final ResultBean handleException(BlogOSException e) {
        return new ResultBean(e);
    }

    /**
     * 未进行转化的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    protected final ResultBean handleException(Throwable e) {
        BlogOSException exception = ResultUtil.failException(CodeMessage.COMMON_UNKNOWN_ERROR, e);
        return new ResultBean(exception);
    }


    /**
     * 统一处理“请求参数缺失”错误
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    protected final ResultBean handlerException(MissingServletRequestParameterException e) {
        BlogOSException exception = ResultUtil.failException(CodeMessage.COMMON_MISSING_REQUEST_PARAMETER, e);
        return new ResultBean(exception);
    }

    /**
     * 统一处理“请求参数与目标参数类型不匹配”错误
     */
    @ExceptionHandler(TypeMismatchException.class)
    @ResponseBody
    protected final ResultBean handlerException(TypeMismatchException e) {
        return new ResultBean(ResultUtil.failException(CodeMessage.COMMON_PARAMETER_TYPE_MISMATCH, e));
    }

    /**
     * 未指明操作
     */
    @RequestMapping
    protected void defaultOperation(HttpServletRequest request) {
        throw ResultUtil.failException(CodeMessage.COMMON_UNSPECIFIED_OPERATION);
    }

}
