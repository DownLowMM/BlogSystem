package com.duan.blogos.service.service.common;

import com.duan.blogos.service.restful.ResultModel;

/**
 * Created on 2018/9/15.
 *
 * @author DuanJiaNing
 */
public interface SmsService {

    ResultModel sendSmsTo(String content, String phone);

}
