package com.duan.blogos.api.util;

import com.duan.blogos.api.BaseCheckController;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.common.SmsService;
import com.duan.common.spring.verify.Rule;
import com.duan.common.spring.verify.ValueRule;
import com.duan.common.spring.verify.annoation.ArgVerifyComposite;
import com.duan.common.spring.verify.annoation.method.RequestArgValueVerify;
import com.duan.common.spring.verify.annoation.method.RequestArgVerify;
import com.duan.common.spring.verify.annoation.method.RequestArgsVerifyComposite;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SmsService smsService;

    /**
     * 向指定号码发送短信
     */
    @PostMapping
    @RequestArgsVerifyComposite({
            @ArgVerifyComposite(valueVerify = @RequestArgValueVerify(param = "phone", rule = ValueRule.TEXT_LENGTH_GREATER_THAN, value = "11")),
            @ArgVerifyComposite(valueVerify = @RequestArgValueVerify(param = "phone", rule = ValueRule.TEXT_LENGTH_NOT_GREATER_THAN, value = "15")),
            @ArgVerifyComposite(@RequestArgVerify(param = "content", rule = Rule.NOT_BLANK))
    })
    public ResultModel send(
            @RequestParam("phone") String phone,
            @RequestParam("content") String content) {

        return smsService.sendSmsTo(content, phone);
    }
}
