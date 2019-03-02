package com.duan.blogos.websample;

import com.duan.blogos.service.OnlineService;
import com.duan.blogos.service.blogger.BloggerAccountService;
import com.duan.blogos.service.blogger.BloggerProfileService;
import com.duan.blogos.service.blogger.BloggerSettingService;
import com.duan.blogos.service.blogger.BloggerStatisticsService;
import com.duan.blogos.service.common.dto.blogger.BloggerAccountDTO;
import com.duan.blogos.service.common.dto.blogger.BloggerProfileDTO;
import com.duan.blogos.service.common.dto.blogger.BloggerSettingDTO;
import com.duan.blogos.service.common.dto.blogger.BloggerStatisticsDTO;
import com.duan.blogos.service.common.restful.ResultModel;
import com.duan.blogos.service.common.util.Utils;
import com.duan.blogos.websample.util.Util;
import com.duan.blogos.websample.vo.BloggerModel;
import com.duan.blogos.websample.vo.BloggerVO;
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
    private BloggerAccountService accountService;

    @Autowired
    private BloggerProfileService bloggerProfileService;

    @Autowired
    private BloggerStatisticsService statisticsService;

    @Autowired
    private OnlineService onlineService;

    @Autowired
    private BloggerSettingService settingService;

    public static Long getLoginBloggerId() {
        String loginBloggerId = Util.getParameter("loginBloggerId");
        return loginBloggerId == null ? null : Long.valueOf(loginBloggerId);
    }

    public static Long getPageOwnerBloggerId() {
        String pageOwnerBloggerId = Util.getParameter("pageOwnerBloggerId");
        return pageOwnerBloggerId == null ? null : Long.valueOf(pageOwnerBloggerId);
    }

    @ModelAttribute("bloggerModel")
    public BloggerModel blogger() {

        BloggerModel model = new BloggerModel();

        // 页面所有者
        Long pageOwnerBloggerId = getPageOwnerBloggerId();
        if (pageOwnerBloggerId != null) {
            PageOwnerBloggerVO ownerBloggerVO = getBlogger(new PageOwnerBloggerVO(), pageOwnerBloggerId);
            ownerBloggerVO.setNameBase64(Utils.encodeUrlBase64(ownerBloggerVO.getAccount().getUsername()));
            model.setPageOwnerBlogger(ownerBloggerVO);
        }

        // 登录博主
        Long loginBloggerId = getLoginBloggerId();
        if (loginBloggerId != null &&
                (onlineService.getLoginBloggerId(Util.getParameter("token"))).equals(loginBloggerId)) {
            LoginBloggerVO loginBloggerVO = getBlogger(new LoginBloggerVO(), loginBloggerId);
            loginBloggerVO.setLoginSignal("yes");
            loginBloggerVO.setNameBase64(loginBloggerVO.getAccount().getUsername());
            model.setLoginBlogger(loginBloggerVO);
        }

        return model;

    }

    private <T extends BloggerVO> T getBlogger(T vo, Long bid) {

        BloggerAccountDTO account = accountService.getAccountById(bid);
        BloggerProfileDTO profile = bloggerProfileService.getBloggerProfile(bid);
        ResultModel<BloggerStatisticsDTO> ownerBgStat = statisticsService.getBloggerStatistics(bid);
        BloggerSettingDTO setting = settingService.getSetting(bid);

        vo.setAccount(account);
        vo.setSetting(setting);
        vo.setProfile(profile);
        vo.setStatistics(ownerBgStat.getData());

        /*if (profile.getAvatarId() == null) {
            BloggerPictureDTO defaultPicture = bloggerPictureService
                    .getDefaultPicture(BloggerPictureCategoryEnum.DEFAULT_BLOGGER_AVATAR);

            vo.getProfile().setAvatarId(Optional.ofNullable(profile.getAvatarId())
                    .orElse(defaultPicture == null ? null : defaultPicture.getId()));
        }*/

        return vo;
    }
}
