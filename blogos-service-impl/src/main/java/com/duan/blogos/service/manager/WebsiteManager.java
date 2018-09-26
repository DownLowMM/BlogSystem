package com.duan.blogos.service.manager;

import com.duan.blogos.service.dao.blogger.BloggerAccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created on 2018/5/1.
 *
 * @author DuanJiaNing
 */
@Component
public class WebsiteManager {

    @Autowired
    private BloggerAccountDao accountDao;

    /**
     * 获得网站活跃博主 id
     *
     * @param count 获取个数
     * @return id 集合
     */
    public List<Long> getActiveBloggerIds(int count) {

        // UPDATE: 2018/5/1 更新 完善策略
        return accountDao.listId(count);
    }

}
