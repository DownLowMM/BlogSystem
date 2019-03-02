package com.duan.blogos.websample;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created on 2018/3/7.
 *
 * @author DuanJiaNing
 */
@Controller
@RequestMapping("/{pageOwnerBloggerNameBase64}/blog/favourite")
public class FavouriteBlogPageController {

    @RequestMapping("/like")
    public ModelAndView pageLike() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/blogger/favourite_blog");

        mv.addObject("type", "like");
        return mv;
    }

    @RequestMapping("/collect")
    public ModelAndView pageCollect() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/blogger/favourite_blog");

        mv.addObject("type", "collect");
        return mv;
    }
}
