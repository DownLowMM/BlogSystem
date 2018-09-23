package com.duan.blogos.api.blog;

import com.duan.blogos.service.dto.blog.BlogStatisticsCountDTO;
import com.duan.blogos.service.dto.blog.BlogStatisticsDTO;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ResultUtil;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.common.BlogStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController
@RequestMapping("/blog/{blogId}/statistics")
public class BlogStatisticsController extends BaseBlogController {

    @Autowired
    private BlogStatisticsService statisticsService;

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
    public ResultModel<BlogStatisticsCountDTO> getCount(@PathVariable Long blogId) {
        handleBlogStatisticsExistCheck(blogId);

        ResultModel<BlogStatisticsCountDTO> statistics = statisticsService.getBlogStatisticsCount(blogId);
        if (statistics == null) handlerEmptyResult();

        return statistics;
    }

    // 检查博文的统计信息是否存在
    private void handleBlogStatisticsExistCheck(Long blogId) {
        if (!blogValidateService.checkBlogStatisticExist(blogId))
            throw ResultUtil.failException(CodeMessage.BLOG_UNKNOWN_BLOG);
    }


}
