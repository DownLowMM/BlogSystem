package com.duan.blogos.service.service.common;

import com.duan.blogos.service.dto.blogger.BloggerAccountDTO;
import com.duan.blogos.service.restful.ResultModel;

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
    ResultModel login(BloggerAccountDTO account);

    /**
     * 登出
     */
    ResultModel logout(Long uid);
}
