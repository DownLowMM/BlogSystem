package com.duan.blogos.service.impl.blogger;

import com.duan.blogos.service.dao.blogger.BloggerSettingDao;
import com.duan.blogos.service.dto.blogger.BloggerSettingDTO;
import com.duan.blogos.service.entity.blogger.BloggerSetting;
import com.duan.blogos.service.service.blogger.BloggerSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created on 2018/3/26.
 *
 * @author DuanJiaNing
 */
@Service
public class BloggerSettingServiceImpl implements BloggerSettingService {

    @Autowired
    private BloggerSettingDao settingDao;

    @Override
    public BloggerSettingDTO getSetting(int bloggerId) {
        BloggerSetting setting = settingDao.getSetting(bloggerId);

        // TODO
        return null;
    }

    @Override
    public boolean updateMainPageNavPos(int bloggerId, int pos) {
        int effect = settingDao.updateMainPageNavPos(bloggerId, pos);
        if (effect <= 0) return false;

        return true;
    }

}
