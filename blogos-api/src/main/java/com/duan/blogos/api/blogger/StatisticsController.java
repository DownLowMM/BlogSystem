package com.duan.blogos.api.blogger;

import com.duan.blogos.annonation.TokenNotRequired;
import com.duan.blogos.api.BaseController;
import com.duan.blogos.service.dto.blogger.BloggerStatisticsDTO;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 2018/2/11.
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/blogger/statistic")
public class StatisticsController extends BaseController {

    @Autowired
    private BloggerStatisticsService statisticsService;

    @GetMapping
    @TokenNotRequired
    public ResultModel<BloggerStatisticsDTO> get(@RequestParam Long bloggerId) {

        handleAccountCheck(bloggerId);

        ResultModel<BloggerStatisticsDTO> statistics = statisticsService.getBloggerStatistics(bloggerId);
        if (statistics == null) handlerOperateFail();

        return statistics;
    }

}
