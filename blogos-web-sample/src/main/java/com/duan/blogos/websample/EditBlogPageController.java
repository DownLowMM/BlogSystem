package com.duan.blogos.websample;

import com.alibaba.dubbo.config.annotation.Reference;
import com.duan.blogos.service.dto.blog.BlogDTO;
import com.duan.blogos.service.dto.blogger.BloggerStatisticsDTO;
import com.duan.blogos.service.enums.BlogStatusEnum;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerBlogService;
import com.duan.blogos.service.service.blogger.BloggerStatisticsService;
import com.duan.blogos.websample.util.Util;
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

    @Reference
    private BloggerBlogService blogService;

    @Reference
    private BloggerStatisticsService statisticsService;

    @RequestMapping
    public ModelAndView mainPage(@RequestParam(value = "bid", required = false) Long bloggerId,
                                 @RequestParam(required = false) Long blogId) {
        ModelAndView mv = new ModelAndView();

        if (bloggerId == null || Util.getToken() == null) {
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
