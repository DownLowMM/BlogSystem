package com.duan.blogos.service;

import com.duan.blogos.service.common.restful.ResultModel;

/**
 * Created on 2018/9/15.
 *
 * @author DuanJiaNing
 */
public interface SmsService {

    ResultModel sendSmsTo(String content, String phone);

}
