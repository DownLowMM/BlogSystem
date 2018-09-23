package com.duan.blogos.service.impl.blogger;

import com.duan.blogos.service.dao.blogger.BloggerSettingDao;
import com.duan.blogos.service.dto.blogger.BloggerSettingDTO;
import com.duan.blogos.service.entity.blogger.BloggerSetting;
import com.duan.blogos.service.manager.DataFillingManager;
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

    @Autowired
    private DataFillingManager dataFillingManager;

    @Override
    public BloggerSettingDTO getSetting(Long bloggerId) {
        BloggerSetting setting = settingDao.getSetting(bloggerId);

        return dataFillingManager.bloggerSetting2DTO(setting);
    }

    @Override
    public boolean updateMainPageNavPos(Long bloggerId, int pos) {
        int effect = settingDao.updateMainPageNavPos(bloggerId, pos);
        if (effect <= 0) return false;

        return true;
    }

}
