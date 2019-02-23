package com.duan.blogos.websample;

import com.alibaba.dubbo.config.annotation.Reference;
import com.duan.blogos.service.dto.blogger.BloggerAccountDTO;
import com.duan.blogos.service.dto.blogger.BloggerStatisticsDTO;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerAccountService;
import com.duan.blogos.service.service.blogger.BloggerStatisticsService;
import com.duan.blogos.service.service.common.OnlineService;
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
@RequestMapping("/{pageOwnerBloggerName}/blog/favourite")
public class FavouriteBlogPageController {

    @Reference
    private BloggerAccountService accountService;

    @Reference
    private OnlineService onlineService;

    @Reference
    private BloggerStatisticsService statisticsService;

    @RequestMapping("/like")
    public ModelAndView pageLike(HttpServletRequest request,
                                 @ModelAttribute
                                 @PathVariable String pageOwnerBloggerName) {
        ModelAndView mv = new ModelAndView();
        setCommon(mv, request, pageOwnerBloggerName);

        mv.addObject("type", "like");
        return mv;
    }

    @RequestMapping("/collect")
    public ModelAndView pageCollect(HttpServletRequest request,
                                    @ModelAttribute
                                    @PathVariable String pageOwnerBloggerName) {
        ModelAndView mv = new ModelAndView();
        setCommon(mv, request, pageOwnerBloggerName);

        mv.addObject("type", "collect");
        return mv;
    }

    private void setCommon(ModelAndView mv, HttpServletRequest request, String bloggerName) {
        mv.setViewName("/blogger/favourite_blog");

        // 登陆博主 id
        String token = ""; // TODO redis + token 维护会话
        Long loginBloggerId = onlineService.getLoginBloggerId(token);
        ResultModel<BloggerStatisticsDTO> loginBgStat = statisticsService.getBloggerStatistics(loginBloggerId);
        mv.addObject("loginBgStat", loginBgStat.getData());

        BloggerAccountDTO account = accountService.getAccount(bloggerName);
        mv.addObject("bloggerId", account.getId());
        mv.addObject("bloggerName", account.getUsername());

    }

}
