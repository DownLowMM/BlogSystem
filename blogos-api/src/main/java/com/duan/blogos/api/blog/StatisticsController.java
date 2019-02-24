package com.duan.blogos.api.blog;

import com.alibaba.dubbo.config.annotation.Reference;
import com.duan.blogos.annonation.TokenNotRequired;
import com.duan.blogos.api.BaseController;
import com.duan.blogos.service.common.dto.blog.BlogBaseStatisticsDTO;
import com.duan.blogos.service.common.dto.blog.BlogStatisticsDTO;
import com.duan.blogos.service.common.restful.ResultModel;
import com.duan.blogos.service.blog.StatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 2018/1/17.
 * 博文统计信息。
 * 博文统计信息的新增、删除由BloggerBlogController的博文新增、删除控制；更新由BlogOperateController控制。
 * <p>
 * 1 获取统计信息
 * 2 获取统计信息（简版，只获取各项信息的次数）
 *
 * @author DuanJiaNing
 */
@TokenNotRequired
@RestController("blogStatisticsController")
@RequestMapping("/blog/{blogId}/statistics")
public class StatisticsController extends BaseController {

    @Reference
    private StatisticsService statisticsService;

    /**
     * 获得博文统计信息
     */
    @GetMapping
    public ResultModel<BlogStatisticsDTO> get(@PathVariable Long blogId) {
        handleBlogStatisticsExistCheck(blogId);

        ResultModel<BlogStatisticsDTO> result = statisticsService.getBlogStatistics(blogId);
        if (result == null) handlerEmptyResult();

        return result;
    }

    /**
     * 获取统计信息（简版，只获取各项信息的次数）
     */
    @GetMapping("/count")
    public ResultModel<BlogBaseStatisticsDTO> getCount(@PathVariable Long blogId) {
        handleBlogStatisticsExistCheck(blogId);

        ResultModel<BlogBaseStatisticsDTO> statistics = statisticsService.getBlogStatisticsCount(blogId);
        if (statistics == null) handlerEmptyResult();

        return statistics;
    }

}
