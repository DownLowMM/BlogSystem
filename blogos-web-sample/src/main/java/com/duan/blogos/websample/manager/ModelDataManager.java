package com.duan.blogos.websample.manager;

import com.duan.blogos.service.blogger.BloggerAccountService;
import com.duan.blogos.service.blogger.BloggerProfileService;
import com.duan.blogos.service.blogger.BloggerSettingService;
import com.duan.blogos.service.blogger.BloggerStatisticsService;
import com.duan.blogos.service.common.dto.blogger.BloggerAccountDTO;
import com.duan.blogos.service.common.dto.blogger.BloggerProfileDTO;
import com.duan.blogos.service.common.dto.blogger.BloggerSettingDTO;
import com.duan.blogos.service.common.dto.blogger.BloggerStatisticsDTO;
import com.duan.blogos.service.common.restful.ResultModel;
import com.duan.blogos.websample.vo.ApplicationModel;
import com.duan.blogos.websample.vo.BloggerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created on 2019/3/2.
 *
 * @author DuanJiaNing
 */
@Component
public class ModelDataManager {

    @Autowired
    private BloggerAccountService accountService;

    @Autowired
    private BloggerProfileService bloggerProfileService;

    @Autowired
    private BloggerStatisticsService statisticsService;

    @Autowired
    private BloggerSettingService settingService;

    public <T extends BloggerVO> T getBlogger(T vo, Long bid) {

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

    public ApplicationModel getApplicationModel() {
        ApplicationModel applicationModel = new ApplicationModel();
        applicationModel.setImageServerHost("http://127.0.0.1:7070");
        return applicationModel;
    }
}
