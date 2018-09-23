package com.duan.blogos.api;

import com.duan.blogos.annonation.TokenNotRequired;
import com.duan.blogos.api.blogger.BaseBloggerController;
import com.duan.blogos.service.dto.blogger.BloggerAccountDTO;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.common.OnlineService;
import com.duan.common.spring.verify.VerifyRule;
import com.duan.common.spring.verify.VerifyValueRule;
import com.duan.common.spring.verify.annoation.method.RequestParamValueVerify;
import com.duan.common.spring.verify.annoation.method.RequestParamsValueVerify;
import com.duan.common.spring.verify.annoation.parameter.ParamVerify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created on 2018/1/11.
 * 博主登录
 * <p>
 * 1 用户名登录
 * 2 电话号码登录
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/blogger/login")
public class BloggerLoginController extends BaseBloggerController {

    @Autowired
    private OnlineService onlineService;

    @RequestParamsValueVerify({
            @RequestParamValueVerify(param = "password", rule = VerifyValueRule.TEXT_LENGTH_NOT_LESS_THAN, value = "6"),
            @RequestParamValueVerify(param = "password", rule = VerifyValueRule.TEXT_LENGTH_NOT_GREATER_THAN, value = "10")
    })
    @PostMapping("/way=name")
    @TokenNotRequired
    public ResultModel loginWithUserName(@ParamVerify(rule = VerifyRule.NOT_BLANK)
                                         @RequestParam String userName,
                                         @RequestParam String password) {
        BloggerAccountDTO dto = new BloggerAccountDTO();
        dto.setUsername(userName);
        dto.setPassword(password);

        return onlineService.login(dto);
    }

    @TokenNotRequired
    @RequestMapping(value = "/way=phone", method = RequestMethod.POST)
    public ResultModel loginWithPhoneNumber(HttpServletRequest request,
                                            @RequestParam("phone") String phone) {

        // UPDATE: 2018/9/23 更新
        /*
        handlePhoneCheck(phone, request);

        BloggerAccountDTO account = accountService.getAccountByPhone(phone);
        if (account == null) return new ResultModel<>("", ResultModel.FAIL);

        HttpSession session = request.getSession();
        session.setAttribute(sessionProperties.getBloggerId(), account.getId());
        session.setAttribute(sessionProperties.getBloggerName(), account.getUsername());
        session.setAttribute(sessionProperties.getLoginSignal(), "login");
*/
        // 成功登录
        return ResultModel.fail();
    }

}
