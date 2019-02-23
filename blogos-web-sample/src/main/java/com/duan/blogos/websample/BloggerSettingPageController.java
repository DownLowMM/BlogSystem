package com.duan.blogos.websample;

import com.alibaba.dubbo.config.annotation.Reference;
import com.duan.blogos.service.dto.blogger.BloggerAccountDTO;
import com.duan.blogos.service.dto.blogger.BloggerPictureDTO;
import com.duan.blogos.service.dto.blogger.BloggerProfileDTO;
import com.duan.blogos.service.dto.blogger.BloggerSettingDTO;
import com.duan.blogos.service.enums.BloggerPictureCategoryEnum;
import com.duan.blogos.service.service.blogger.BloggerAccountService;
import com.duan.blogos.service.service.blogger.BloggerPictureService;
import com.duan.blogos.service.service.blogger.BloggerProfileService;
import com.duan.blogos.service.service.blogger.BloggerSettingService;
import com.duan.blogos.websample.util.Util;
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
@RequestMapping("/{bloggerName}/setting")
public class BloggerSettingPageController {

    @Reference
    private BloggerAccountService accountService;

    @Reference
    private BloggerProfileService profileService;

    @Reference
    private BloggerPictureService pictureService;

    @Reference
    private BloggerSettingService settingService;

    @RequestMapping
    public ModelAndView pageSetting(HttpServletRequest request,
                                    @ModelAttribute
                                    @PathVariable String bloggerName) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/blogger/setting");

        BloggerAccountDTO account = accountService.getAccount(bloggerName);
        if (account == null) {
            request.setAttribute("code", 500);
            mv.setViewName("/blogger/register");
            return mv;
        } else if (Util.getToken() == null) {
            return new ModelAndView("redirect:/login");
        }

        Long bloggerId = Util.getUid();
        BloggerProfileDTO profile = profileService.getBloggerProfile(bloggerId);
        if (profile.getAvatarId() == null) {
            BloggerPictureDTO picture = pictureService.getDefaultPicture(BloggerPictureCategoryEnum.DEFAULT_BLOGGER_AVATAR);
            profile.setAvatarId(picture.getId());
        }
        mv.addObject("profile", profile);

        BloggerSettingDTO setting = settingService.getSetting(bloggerId);
        mv.addObject("setting", setting);

        return mv;
    }

}
