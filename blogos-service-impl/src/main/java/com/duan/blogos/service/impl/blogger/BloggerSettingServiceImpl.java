package com.duan.blogos.service.impl.blogger;

import com.alibaba.dubbo.config.annotation.Service;
import com.duan.blogos.service.dao.BloggerSettingDao;
import com.duan.blogos.service.common.dto.blogger.BloggerSettingDTO;
import com.duan.blogos.service.entity.BloggerSetting;
import com.duan.blogos.service.blogger.BloggerSettingService;
import com.duan.blogos.service.common.util.DataConverter;
import org.springframework.beans.factory.annotation.Autowired;

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
