package com.duan.blogos.service.service.blogger;


import com.duan.blogos.service.dto.blogger.BloggerDTO;
import com.duan.blogos.service.dto.blogger.BloggerStatisticsDTO;
import com.duan.blogos.service.restful.ResultModel;

/**
 * Created on 2018/1/17.
 * 博主统计信息服务
 *
 * @author DuanJiaNing
 */
public interface BloggerStatisticsService {

    /**
     * 获取博主统计信息
     *
     * @param bloggerId 博主id
     * @return 查询结果
     */
    ResultModel<BloggerStatisticsDTO> getBloggerStatistics(int bloggerId);

    /**
     * 根据 id 获取博主的 dto 对象
     *
     * @param ids 博主 id
     * @return 数组
     */
    BloggerDTO[] listBloggerDTO(int... ids);

}
