package com.duan.blogos.websample;

import com.duan.blogos.service.common.dto.blogger.BloggerBriefDTO;
import com.duan.blogos.service.common.util.Utils;
import com.duan.blogos.service.website.WebSiteStatisticsService;
import com.duan.blogos.websample.vo.BloggerBriefVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2018/2/8.
 *
 * @author DuanJiaNing
 */
@Controller
@RequestMapping("/login")
public class LoginPageController {

    @Autowired
    private WebSiteStatisticsService webSiteStatisticsService;

    @RequestMapping
    public ModelAndView loginPage(@RequestParam(value = "activeBloggerCount", required = false) Integer activeCount) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("blogger/login");

        List<BloggerBriefDTO> dtos = webSiteStatisticsService.listActiveBlogger(activeCount == null ? -1 : activeCount);
        if (!CollectionUtils.isEmpty(dtos)) {
            List<BloggerBriefVO> vos = new ArrayList<>(dtos.size());
            for (BloggerBriefDTO dto : dtos) {
                BloggerBriefVO vo = new BloggerBriefVO();
                vo.setStatistics(dto.getStatistics());
                vo.setNameBase64(Utils.encodeUrlBase64(dto.getBlogger().getUsername()));
                vo.setName(dto.getBlogger().getUsername());
                vo.setAvatarId(dto.getBlogger().getProfile().getAvatarId());
                vo.setAboutMe(dto.getBlogger().getProfile().getAboutMe());
                vo.setId(dto.getBlogger().getId());

                vos.add(vo);
            }

            mv.addObject("briefs", vos);
        }

        return mv;
    }
}
