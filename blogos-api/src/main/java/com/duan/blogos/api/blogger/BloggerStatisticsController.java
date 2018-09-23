package com.duan.blogos.api.blogger;

import com.duan.blogos.service.dto.blogger.BloggerStatisticsDTO;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created on 2018/2/11.
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/blogger/{bloggerId}/statistic")
public class BloggerStatisticsController extends BaseBloggerController {

    @Autowired
    private BloggerStatisticsService statisticsService;

    @RequestMapping(method = RequestMethod.GET)
    public ResultModel<BloggerStatisticsDTO> get(HttpServletRequest request,
                                                 @PathVariable Integer bloggerId) {

        handleAccountCheck(bloggerId);

        ResultModel<BloggerStatisticsDTO> statistics = statisticsService.getBloggerStatistics(bloggerId);
        if (statistics == null) handlerOperateFail();

        return statistics;
    }

}
