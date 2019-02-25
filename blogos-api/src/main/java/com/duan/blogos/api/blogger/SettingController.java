package com.duan.blogos.api.blogger;

import com.alibaba.dubbo.config.annotation.Reference;
import com.duan.blogos.annonation.Uid;
import com.duan.blogos.api.BaseController;
import com.duan.blogos.service.blogger.BloggerSettingService;
import com.duan.blogos.service.common.restful.ResultModel;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 2017/12/29.
 * 博主个人设置api
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/blogger/setting")
public class SettingController extends BaseController {

    @Reference
    private BloggerSettingService settingService;

    /**
     * 更新博主主页导航位置
     */
    @PutMapping("/item=mainPageNavPos")
    public ResultModel update(@Uid Long bloggerId,
                              @RequestParam Integer mainPageNavPos) {

        handleMainPageNavPosCheck(mainPageNavPos);

        boolean result = settingService.updateMainPageNavPos(bloggerId, mainPageNavPos);
        if (!result)
            return handlerOperateFail();

        return ResultModel.success();
    }

}
