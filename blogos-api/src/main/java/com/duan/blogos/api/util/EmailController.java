package com.duan.blogos.api.util;

import com.alibaba.dubbo.config.annotation.Reference;
import com.duan.blogos.api.BaseCheckController;
import com.duan.blogos.service.common.restful.ResultModel;
import com.duan.blogos.service.EmailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 2018/4/7.
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/email")
public class EmailController extends BaseCheckController {

    @Reference
    private EmailService emailService;

    /**
     * 发送反馈邮件
     */
    @PostMapping("/feedback")
    public ResultModel sendFeedback(@RequestParam(required = false) Long bloggerId,
                                    @RequestParam String content,
                                    @RequestParam(required = false) String contact) {

        String subject = "feedback email";
        if (!emailService.sendFeedback(bloggerId, subject, content, contact))
            handlerOperateFail();

        return ResultModel.success();
    }

}
