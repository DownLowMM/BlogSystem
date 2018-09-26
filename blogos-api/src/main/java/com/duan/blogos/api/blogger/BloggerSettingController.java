package com.duan.blogos.api.blogger;

import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created on 2017/12/29.
 * 博主个人设置api
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/blogger/{bloggerId}/setting")
public class BloggerSettingController extends BaseBloggerController {

    @Autowired
    private BloggerSettingService settingService;

    /**
     * 更新博主主页导航位置
     */
    @PutMapping("/item=mainPageNavPos")
    public ResultModel update(@PathVariable Long bloggerId,
                              @RequestParam("mainPageNavPos") Integer mainPageNavPos) {

        handleMainPageNavPosCheck(mainPageNavPos);

        boolean result = settingService.updateMainPageNavPos(bloggerId, mainPageNavPos);
        if (!result) handlerOperateFail();

        return new ResultModel<>("");
    }

}
