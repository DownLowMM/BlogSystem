package com.duan.blogos.service.impl.validate;

import com.duan.blogos.service.config.preference.WebsiteProperties;
import com.duan.blogos.service.dao.blog.BlogCategoryDao;
import com.duan.blogos.service.dao.blogger.BloggerAccountDao;
import com.duan.blogos.service.dao.blogger.BloggerPictureDao;
import com.duan.blogos.service.entity.blogger.BloggerPicture;
import com.duan.blogos.service.enums.BloggerPictureCategoryEnum;
import com.duan.blogos.service.service.validate.BloggerValidateService;
import com.duan.blogos.util.common.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public boolean checkAccountExist(int id) {
        return accountDao.getAccountById(id) != null;
    }

    @Override
    public boolean checkBloggerBlogCategoryExist(int bloggerId, int categoryId) {
        return categoryDao.getCategory(bloggerId, categoryId) != null;
    }

    @Override
    public boolean checkBloggerPictureLegal(int bloggerId, int category) {
        int pictureManagerId = websiteProperties.getManagerId();

        // 图片管理者可以操作任何类别图片,非图片管理者不能操作系统默认图片
        return bloggerId == pictureManagerId || !BloggerPictureCategoryEnum.isDefaultPictureCategory(category);
    }

    @Override
    public boolean checkBloggerSignIn(Integer bloggerId) {

        // TODO redis 实现

        // 检查当前登录否
//        HttpSession session = request.getSession();
//        Object obj = session.getAttribute(propertiesManager.getSessionNameOfBloggerId());
//        return bloggerId.equals(obj);

        return true;
    }

    @Override
    public boolean checkBloggerPictureExist(int bloggerId, int pictureId) {
        BloggerPicture picture = pictureDao.getPictureById(pictureId);
        if (picture != null && Integer.valueOf(bloggerId).equals(picture.getBloggerId()))
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
    public boolean checkMainPageNavPos(int mainPageNavPos) {
        return mainPageNavPos == 0 || mainPageNavPos == 1;
    }
}
