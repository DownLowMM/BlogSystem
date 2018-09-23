package com.duan.blogos.service.dao.blogger;

import com.duan.blogos.service.dao.BaseDao;
import com.duan.blogos.service.entity.blogger.BloggerSetting;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created on 2018/3/26.
 *
 * @author DuanJiaNing
 */
@Repository
public interface BloggerSettingDao extends BaseDao<BloggerSetting> {

    /**
     * 查询博主设置
     *
     * @param bloggerId 博主id
     * @return 查询结果
     */
    BloggerSetting getSetting(Long bloggerId);

    /**
     * 更新博主主页个人信息栏位置
     *
     * @param bloggerId 博主id
     * @param pos       0为左，1为右
     * @return 影响行数
     */
    int updateMainPageNavPos(@Param("bloggerId") Long bloggerId,
                             @Param("pos") int pos);
}
