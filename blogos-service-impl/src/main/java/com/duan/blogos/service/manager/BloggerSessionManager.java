package com.duan.blogos.service.manager;

import com.duan.blogos.service.properties.BloggerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created on 2018/3/13.
 *
 * @author DuanJiaNing
 */
@Component
public class BloggerSessionManager {

    @Autowired
    private BloggerProperties bloggerProperties;

    /**
     * 获得登录博主id，未登录返回-1
     */
    public int getLoginBloggerId(String token) {

//        HttpSession session = request.getSession();
//        Object obj = session.getAttribute(bloggerProperties.getSessionNameOfBloggerId());
//        return obj == null ? -1 : (Integer) obj;

        // TODO redis + token 维护会话
        return -1;
    }

}
