package com.duan.blogos.websample;

import com.duan.blogos.service.blogger.BloggerBlogService;
import com.duan.blogos.service.blogger.BloggerStatisticsService;
import com.duan.blogos.service.common.dto.blog.BlogDTO;
import com.duan.blogos.service.common.dto.blogger.BloggerStatisticsDTO;
import com.duan.blogos.service.common.enums.BlogStatusEnum;
import com.duan.blogos.service.common.restful.ResultModel;
import com.duan.blogos.websample.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created on 2018/2/13.
 *
 * @author DuanJiaNing
 */
@Controller
@RequestMapping("/edit_blog")
public class EditBlogPageController {

    @Autowired
    private BloggerBlogService blogService;

    @Autowired
    private BloggerStatisticsService statisticsService;

    @RequestMapping
    public ModelAndView mainPage(@RequestParam(value = "bid", required = false) Long bloggerId,
                                 @RequestParam(required = false) Long blogId) {
        ModelAndView mv = new ModelAndView();

        if (bloggerId == null || Util.getParameter("token") == null) {
            return new ModelAndView("redirect:/login");
        } else {
            if (blogId != null) {
                ResultModel<BlogDTO> blog = blogService.getBlog(blogId);
                BlogDTO data = blog.getData();
                mv.addObject("blogId", blogId);
                mv.addObject("categoryId", data.getCategoryIds());
                mv.addObject("labelIds", data.getLabelIds());
                mv.addObject("blogTitle", data.getTitle());
                mv.addObject("blogSummary", data.getSummary());
                if (data.getState().equals(BlogStatusEnum.PRIVATE.getCode())) {
                    mv.addObject("blogIsPrivate", true);
                }
                mv.addObject("blogContentMd", Util.stringToUnicode(data.getContentMd()));
            }

            ResultModel<BloggerStatisticsDTO> loginBgStat = statisticsService.getBloggerStatistics(bloggerId);
            mv.addObject("loginBgStat", loginBgStat.getData());

            mv.setViewName("/blogger/edit_blog");
        }

        return mv;
    }

}
