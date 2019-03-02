package com.duan.blogos.websample;

import com.duan.blogos.service.OnlineService;
import com.duan.blogos.service.common.util.Utils;
import com.duan.blogos.websample.manager.ModelDataManager;
import com.duan.blogos.websample.util.Util;
import com.duan.blogos.websample.vo.ApplicationModel;
import com.duan.blogos.websample.vo.BloggerModel;
import com.duan.blogos.websample.vo.LoginBloggerVO;
import com.duan.blogos.websample.vo.PageOwnerBloggerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Created on 2019/3/2.
 *
 * @author DuanJiaNing
 */
@ControllerAdvice
public class MyControllerAdvice {

    @Autowired
    private ModelDataManager modelDataManager;

    @Autowired
    private OnlineService onlineService;

    public static Long getLoginBloggerId() {
        String loginBloggerId = Util.getParameter("loginBloggerId");
        return loginBloggerId == null ? null : Long.valueOf(loginBloggerId);
    }

    public static Long getPageOwnerBloggerId() {
        String pageOwnerBloggerId = Util.getParameter("pageOwnerBloggerId");
        return pageOwnerBloggerId == null ? null : Long.valueOf(pageOwnerBloggerId);
    }

    @ModelAttribute("applicationModel")
    public ApplicationModel applicationModel() {
        return modelDataManager.getApplicationModel();
    }


    @ModelAttribute("bloggerModel")
    public BloggerModel blogger() {

        BloggerModel model = new BloggerModel();

        // 页面所有者
        Long pageOwnerBloggerId = getPageOwnerBloggerId();
        if (pageOwnerBloggerId != null) {
            PageOwnerBloggerVO ownerBloggerVO = modelDataManager.getBlogger(new PageOwnerBloggerVO(), pageOwnerBloggerId);
            ownerBloggerVO.setNameBase64(Utils.encodeUrlBase64(ownerBloggerVO.getAccount().getUsername()));
            model.setPageOwnerBlogger(ownerBloggerVO);
        }

        // 登录博主
        Long loginBloggerId = getLoginBloggerId();
        if (loginBloggerId == null) {
            loginBloggerId = onlineService.getLoginBloggerId(Util.getParameter("token"));
        }

        if (loginBloggerId != null) {
            LoginBloggerVO loginBloggerVO = modelDataManager.getBlogger(new LoginBloggerVO(), loginBloggerId);
            loginBloggerVO.setLoginSignal("yes");
            loginBloggerVO.setNameBase64(loginBloggerVO.getAccount().getUsername());
            model.setLoginBlogger(loginBloggerVO);
        }

        // 博主直接登到自己的主页
        if (loginBloggerId != null && pageOwnerBloggerId == null) {
            PageOwnerBloggerVO ownerBloggerVO = modelDataManager.getBlogger(new PageOwnerBloggerVO(), loginBloggerId);
            ownerBloggerVO.setNameBase64(Utils.encodeUrlBase64(ownerBloggerVO.getAccount().getUsername()));
            model.setPageOwnerBlogger(ownerBloggerVO);
        }

        return model;

    }

}
