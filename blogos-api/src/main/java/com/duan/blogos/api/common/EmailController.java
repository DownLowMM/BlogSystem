package com.duan.blogos.api.common;

import com.duan.blogos.api.BaseCheckController;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.common.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private EmailService emailService;

    /**
     * 发送反馈邮件
     */
    @PostMapping("/feedback")
    public ResultModel sendFeedback(@RequestParam(value = "bloggerId", required = false) Long bloggerId,
                                    @RequestParam("content") String content,
                                    @RequestParam(value = "contact", required = false) String contact) {

//        String subject = new RequestContext(request).getMessage("common.feedbackTitle");
        String subject = ""; // TODO
        if (!emailService.sendFeedback(bloggerId == null ? -1 : bloggerId, subject, content, contact))
            handlerOperateFail();

        return new ResultModel<>("");
    }

}
