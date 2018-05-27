package com.duan.blogos.service.service.impl.website;

import com.duan.blogos.dao.dto.blogger.BloggerBriefDTO;
import com.duan.blogos.dao.dto.blogger.BloggerDTO;
import com.duan.blogos.dao.dto.blogger.BloggerStatisticsDTO;
import com.duan.blogos.service.manager.DataFillingManager;
import com.duan.blogos.service.manager.WebsiteManager;
import com.duan.blogos.service.restful.ResultBean;
import com.duan.blogos.service.service.blogger.BloggerStatisticsService;
import com.duan.blogos.service.service.website.WebSiteStatisticsService;
import com.duan.blogos.util.common.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2018/5/1.
 *
 * @author DuanJiaNing
 */
@Service
public class WebSiteStatisticsServiceImpl implements WebSiteStatisticsService {

    @Autowired
    private BloggerStatisticsService statisticsService;

    @Autowired
    private WebsiteManager websiteManager;

    @Autowired
    private DataFillingManager fillingManager;

    @Override
    public List<BloggerBriefDTO> listActiveBlogger(int count) {

        int[] ids = websiteManager.getActiveBloggerIds(count);
        BloggerDTO[] bloggerDTOS = statisticsService.listBloggerDTO(ids);
        if (CollectionUtils.isEmpty(bloggerDTOS)) return null;

        List<BloggerBriefDTO> dtos = new ArrayList<>();
        for (BloggerDTO blogger : bloggerDTOS) {
            ResultBean<BloggerStatisticsDTO> statistics = statisticsService.getBloggerStatistics(blogger.getId());
            final BloggerBriefDTO dto = fillingManager.bloggerTobrief(blogger, statistics.getData());
            dtos.add(dto);
        }

        return dtos;
    }

}
