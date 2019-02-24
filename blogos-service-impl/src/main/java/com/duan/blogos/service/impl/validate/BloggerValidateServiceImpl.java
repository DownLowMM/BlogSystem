package com.duan.blogos.service.impl.validate;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.duan.blogos.service.config.preference.WebsiteProperties;
import com.duan.blogos.service.dao.BlogCategoryDao;
import com.duan.blogos.service.dao.BloggerAccountDao;
import com.duan.blogos.service.dao.BloggerPictureDao;
import com.duan.blogos.service.entity.BloggerPicture;
import com.duan.blogos.service.common.enums.BloggerPictureCategoryEnum;
import com.duan.blogos.service.common.enums.BloggerSettingEnums;
import com.duan.blogos.service.validate.BloggerValidateService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created on 2018/9/13.
 *
 * @author DuanJiaNing
 */
@Service
public class BloggerValidateServiceImpl implements BloggerValidateService {

    @Autowired
    private BloggerAccountDao accountDao;

    @Autowired
    private BloggerPictureDao pictureDao;

    @Autowired
    private BlogCategoryDao categoryDao;

    @Autowired
    private WebsiteProperties websiteProperties;

    @Override
    public boolean checkAccountExist(Long id) {
        return accountDao.getAccountById(id) != null;
    }

    @Override
    public boolean checkBloggerBlogCategoryExist(Long bloggerId, Long categoryId) {
        return categoryDao.getCategory(categoryId) != null;
    }

    @Override
    public boolean checkBloggerPictureLegal(Long bloggerId, Integer category) {
        Long pictureManagerId = websiteProperties.getManagerId();

        // 图片管理者可以操作任何类别图片,非图片管理者不能操作系统默认图片
        return bloggerId.equals(pictureManagerId) || !BloggerPictureCategoryEnum.isDefaultPictureCategory(category);
    }

    @Override
    public boolean checkBloggerPictureExist(Long bloggerId, Long pictureId) {
        BloggerPicture picture = pictureDao.getPictureById(pictureId);
        if (picture != null && bloggerId.equals(picture.getBloggerId()))
            return true;

        return false;
    }

    @Override
    public boolean checkUserName(String username) {
        if (StringUtils.isBlank(username)) return false;

        // UPDATE: 2018/2/2 更新 当前版本对用户名（如格式）不做限制
        return true;
    }

    @Override
    public boolean checkPassword(String password) {
        if (StringUtils.isBlank(password)) return false;

        String regex = "^(?:(?=.*[A-z])(?=.*[0-9])).{6,12}$";
        return password.matches(regex);
    }

    @Override
    public boolean checkMainPageNavPos(Integer mainPageNavPos) {
        for (BloggerSettingEnums.MainPageNavPos pos : BloggerSettingEnums.MainPageNavPos.values()) {
            if (pos.getCode().equals(mainPageNavPos))
                return true;
        }

        return false;
    }
}
