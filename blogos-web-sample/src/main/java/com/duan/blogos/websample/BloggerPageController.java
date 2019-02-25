package com.duan.blogos.websample;

import com.duan.blogos.service.OnlineService;
import com.duan.blogos.service.blogger.*;
import com.duan.blogos.service.common.dto.blogger.*;
import com.duan.blogos.service.common.enums.BloggerPictureCategoryEnum;
import com.duan.blogos.service.common.restful.ResultModel;
import com.duan.blogos.websample.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Created on 2018/2/5.
 * 博主主页
 *
 * @author DuanJiaNing
 */
@Controller
@RequestMapping("/{bloggerNameBase64}")
public class BloggerPageController {

    @Autowired
    private BloggerAccountService accountService;

    @Autowired
    private BloggerProfileService bloggerProfileService;

    @Autowired
    private BloggerStatisticsService statisticsService;

    @Autowired
    private BloggerPictureService bloggerPictureService;

    @Autowired
    private OnlineService onlineService;

    @Autowired
    private BloggerSettingService settingService;

    @RequestMapping("/archives")
    public ModelAndView mainPage(HttpServletRequest request,
                                 @PathVariable String bloggerNameBase64) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("blogger/main");

        String bloggerName = Util.decodeBase64(bloggerNameBase64);
        BloggerAccountDTO account = accountService.getAccount(bloggerName);
        if (account == null) {
            request.setAttribute("code", 500);
            mv.setViewName("/blogger/register");
            return mv;
        }

        mv.addObject("pageOwnerBloggerId", account.getId());
        mv.addObject("pageOwnerBloggerName", account.getUsername());

        Long ownerId = account.getId();
        BloggerProfileDTO profile = bloggerProfileService.getBloggerProfile(ownerId);
        mv.addObject("blogName", profile.getIntro());
        mv.addObject("aboutMe", profile.getAboutMe());

        BloggerPictureDTO defaultPicture = bloggerPictureService.getDefaultPicture(BloggerPictureCategoryEnum.DEFAULT_BLOGGER_AVATAR);
        mv.addObject("avatarId",
                Optional.ofNullable(profile.getAvatarId())
                        .orElse(defaultPicture == null ? null : defaultPicture.getId()));

        ResultModel<BloggerStatisticsDTO> ownerBgStat = statisticsService.getBloggerStatistics(ownerId);
        mv.addObject("ownerBgStat", ownerBgStat.getData());

        Long loginBgId;
        String token = ""; // TODO redis + token 维护会话
        if ((loginBgId = onlineService.getLoginBloggerId(token)) != null) {
            ResultModel<BloggerStatisticsDTO> loginBgStat = statisticsService.getBloggerStatistics(loginBgId);
            mv.addObject("loginBgStat", loginBgStat.getData());
        }

        BloggerSettingDTO setting = settingService.getSetting(ownerId);
        mv.addObject("setting", setting);

        return mv;
    }

}
