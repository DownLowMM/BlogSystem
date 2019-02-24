package com.duan.blogos.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.duan.blogos.service.SmsService;
import com.duan.blogos.service.common.restful.ResultModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 2018/2/18.
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/sms")
public class SMSController extends BaseCheckController {

    @Reference
    private SmsService smsService;

    /**
     * 向指定号码发送短信
     */
    @PostMapping
    public ResultModel send(
            @RequestParam String phone,
            @RequestParam String content) {

        return smsService.sendSmsTo(content, phone);
    }
}
