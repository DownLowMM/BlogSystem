package com.duan.blogos.api;

import com.duan.blogos.annonation.Uid;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.common.OnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 2018/2/17.
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/blogger/logout")
public class BloggerLogoutController extends BaseCheckController {

    @Autowired
    private OnlineService onlineService;

    @GetMapping
    public ResultModel logout(@Uid Long uid) {
        return onlineService.logout(uid);
    }

}
