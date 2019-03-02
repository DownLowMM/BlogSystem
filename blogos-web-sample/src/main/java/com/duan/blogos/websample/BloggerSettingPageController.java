package com.duan.blogos.websample;

import com.duan.blogos.service.blogger.BloggerAccountService;
import com.duan.blogos.service.common.dto.blogger.BloggerAccountDTO;
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
@RequestMapping("/{bloggerNameBase64}/setting")
public class BloggerSettingPageController {

    @Autowired
    private BloggerAccountService accountService;

    @RequestMapping
    public ModelAndView pageSetting(HttpServletRequest request,
                                    @ModelAttribute
                                    @PathVariable String bloggerNameBase64) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/blogger/setting");

        String bloggerName = Utils.decodeUrlBase64(bloggerNameBase64);
        BloggerAccountDTO account = accountService.getAccount(bloggerName);
        if (account == null) {
            request.setAttribute("code", 500);
            mv.setViewName("/blogger/register");
            return mv;
        } else if (Util.getParameter("token") == null) {
            return new ModelAndView("redirect:/login");
        }

        return mv;
    }

}
