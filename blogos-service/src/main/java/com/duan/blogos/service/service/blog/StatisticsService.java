package com.duan.blogos.service.service.blog;


import com.duan.blogos.service.dto.blog.BlogBaseStatisticsDTO;
import com.duan.blogos.service.dto.blog.BlogStatisticsDTO;
import com.duan.blogos.service.restful.ResultModel;

/**
 * Created on 2017/12/18.
 *
 * @author DuanJiaNing
 */
public interface StatisticsService {

    /**
     * 获取博文统计信息
     *
     * @param blogId 博文id
     * @return 查询结果
     */
    ResultModel<BlogStatisticsDTO> getBlogStatistics(Long blogId);

    /**
     * 获取博文统计信息（只获取数据量）
     *
     * @param blogId 博文id
     * @return 查询结果
     */
    ResultModel<BlogBaseStatisticsDTO> getBlogStatisticsCount(Long blogId);

}

