package com.duan.blogos.service;

import com.duan.blogos.service.common.restful.ResultModel;
import com.duan.blogos.service.common.vo.LoginVO;

/**
 * Created on 2018/9/14.
 *
 * @author DuanJiaNing
 */
public interface OnlineService {

    /**
     * 获得登录博主id，未登录返回 null
     */
    Long getLoginBloggerId(String token);

    /**
     * 登入
     */
    ResultModel login(LoginVO account);

    /**
     * 登出
     */
    ResultModel logout(Long uid);
}
