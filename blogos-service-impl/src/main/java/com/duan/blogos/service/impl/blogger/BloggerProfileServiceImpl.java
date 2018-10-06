package com.duan.blogos.service.impl.blogger;

import com.duan.blogos.service.dao.blogger.BloggerPictureDao;
import com.duan.blogos.service.dao.blogger.BloggerProfileDao;
import com.duan.blogos.service.dto.blogger.BloggerProfileDTO;
import com.duan.blogos.service.entity.blogger.BloggerProfile;
import com.duan.blogos.service.manager.ImageManager;
import com.duan.blogos.service.service.blogger.BloggerProfileService;
import com.duan.blogos.service.util.DataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created on 2017/12/19.
 *
 * @author DuanJiaNing
 */
@Service
public class BloggerProfileServiceImpl implements BloggerProfileService {

    @Autowired
    private BloggerProfileDao profileDao;

    @Autowired
    private BloggerPictureDao pictureDao;

    @Autowired
    private ImageManager imageManager;

    @Override
    public Long insertBloggerProfile(Long bloggerId, Long avatarId, String phone, String email, String aboutMe, String intro) {

        BloggerProfile profile = new BloggerProfile();
        profile.setAboutMe(aboutMe);
        profile.setBloggerId(bloggerId);
        profile.setAvatarId(avatarId);
        profile.setEmail(email);
        profile.setIntro(intro);
        profile.setPhone(phone);
        int effect = profileDao.insert(profile);
        if (effect <= 0) return null;

        if (avatarId != null)
            imageManager.imageInsertHandle(bloggerId, avatarId);

        return profile.getId();
    }

    @Override
    public boolean updateBloggerProfile(Long bloggerId, Long avatarId, String newPhone, String newEmail,
                                        String newAboutMe, String newIntro) {

        BloggerProfile profile = profileDao.getProfileByBloggerId(bloggerId);
        if (profile == null) return false;
        Long oldAvatarId = profile.getAvatarId();

        profile.setAvatarId(avatarId);
        profile.setPhone(newPhone);
        profile.setEmail(newEmail);
        profile.setAboutMe(newAboutMe);
        profile.setIntro(newIntro);
        int effect = profileDao.update(profile);
        if (effect <= 0) return false;

        if (avatarId != null)
            imageManager.imageUpdateHandle(bloggerId, avatarId, oldAvatarId);

        return true;
    }

    @Override
    public boolean deleteBloggerProfile(Long bloggerId) {

        BloggerProfile profile = profileDao.getProfileByBloggerId(bloggerId);

        int effect = profileDao.delete(bloggerId);
        if (effect <= 0) return false;

        Long id;
        if ((id = profile.getAvatarId()) != null)
            pictureDao.updateUseCountMinus(id);

        return true;
    }

    @Override
    public BloggerProfileDTO getBloggerProfile(Long bloggerId) {
        BloggerProfile profile = profileDao.getProfileByBloggerId(bloggerId);

        return DataConverter.PO2DTO.bloggerProfile2DTO(profile);
    }

    @Override
    public BloggerProfileDTO getBloggerProfileByPhone(String phone) {
        BloggerProfile profile = profileDao.getProfileByPhone(phone);

        return DataConverter.PO2DTO.bloggerProfile2DTO(profile);
    }

}
