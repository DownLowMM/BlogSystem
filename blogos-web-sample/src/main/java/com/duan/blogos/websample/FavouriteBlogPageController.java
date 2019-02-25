package com.duan.blogos.websample;

import com.duan.blogos.service.OnlineService;
import com.duan.blogos.service.blogger.BloggerAccountService;
import com.duan.blogos.service.blogger.BloggerStatisticsService;
import com.duan.blogos.service.common.dto.blogger.BloggerAccountDTO;
import com.duan.blogos.service.common.dto.blogger.BloggerStatisticsDTO;
import com.duan.blogos.service.common.restful.ResultModel;
import com.duan.blogos.service.common.util.Utils;
import com.duan.blogos.websample.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created on 2018/3/7.
 *
 * @author DuanJiaNing
 */
@Controller
@RequestMapping("/{pageOwnerBloggerNameBase64}/blog/favourite")
public class FavouriteBlogPageController {

    @Autowired
    private BloggerAccountService accountService;

    @Autowired
    private OnlineService onlineService;

    @Autowired
    private BloggerStatisticsService statisticsService;

    @RequestMapping("/like")
    public ModelAndView pageLike(HttpServletRequest request,
                                 @ModelAttribute
                                 @PathVariable String pageOwnerBloggerNameBase64) {
        ModelAndView mv = new ModelAndView();
        setCommon(mv, request, Utils.decodeUrlBase64(pageOwnerBloggerNameBase64));

        mv.addObject("type", "like");
        return mv;
    }

    @RequestMapping("/collect")
    public ModelAndView pageCollect(HttpServletRequest request,
                                    @ModelAttribute
                                    @PathVariable String pageOwnerBloggerNameBase64) {
        ModelAndView mv = new ModelAndView();
        setCommon(mv, request, Utils.decodeUrlBase64(pageOwnerBloggerNameBase64));

        mv.addObject("type", "collect");
        return mv;
    }

    private void setCommon(ModelAndView mv, HttpServletRequest request, String bloggerName) {
        mv.setViewName("/blogger/favourite_blog");

        // 登陆博主 id
        Long loginBloggerId = onlineService.getLoginBloggerId(Util.getToken());
        if (loginBloggerId != null) {
            ResultModel<BloggerStatisticsDTO> loginBgStat = statisticsService.getBloggerStatistics(loginBloggerId);
            mv.addObject("loginBgStat", loginBgStat.getData());
        }

        BloggerAccountDTO account = accountService.getAccount(bloggerName);
        mv.addObject("bloggerId", account.getId());
        mv.addObject("bloggerName", account.getUsername());

    }

}
