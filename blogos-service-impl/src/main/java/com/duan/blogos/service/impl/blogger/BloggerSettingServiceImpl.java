package com.duan.blogos.service.impl.blogger;

import com.duan.blogos.service.dao.blogger.BloggerSettingDao;
import com.duan.blogos.service.dto.blogger.BloggerSettingDTO;
import com.duan.blogos.service.entity.blogger.BloggerSetting;
import com.duan.blogos.service.service.blogger.BloggerSettingService;
import com.duan.blogos.service.util.DataConverter;
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
    public BloggerSettingDTO getSetting(Long bloggerId) {
        BloggerSetting setting = settingDao.getSetting(bloggerId);

        return DataConverter.PO2DTO.bloggerSetting2DTO(setting);
    }

    @Override
    public boolean updateMainPageNavPos(Long bloggerId, Integer pos) {
        int effect = settingDao.updateMainPageNavPos(bloggerId, pos);
        if (effect <= 0) return false;

        return true;
    }

}
