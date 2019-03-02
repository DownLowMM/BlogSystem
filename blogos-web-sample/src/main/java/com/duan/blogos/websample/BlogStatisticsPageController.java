package com.duan.blogos.websample;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created on 2018/3/30.
 *
 * @author DuanJiaNing
 */
@Controller
@RequestMapping("/{bloggerNameBase64}/blog-statistics")
public class BlogStatisticsPageController {

    @RequestMapping
    public ModelAndView page(@ModelAttribute("blogId") Integer blogId) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/blogger/blog_statistics");

        return mv;
    }
}
