package com.duan.blogos.api.blogger;

import com.alibaba.dubbo.config.annotation.Reference;
import com.duan.blogos.annonation.TokenNotRequired;
import com.duan.blogos.annonation.Uid;
import com.duan.blogos.api.BaseController;
import com.duan.blogos.service.dto.blogger.BloggerAccountDTO;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerAccountService;
import com.duan.blogos.service.service.common.OnlineService;
import com.duan.blogos.service.vo.LoginVO;
import com.duan.blogos.util.CodeMessage;
import com.duan.blogos.util.ExceptionUtil;
import com.duan.common.spring.verify.Rule;
import com.duan.common.spring.verify.annoation.parameter.ArgVerify;
import org.springframework.web.bind.annotation.*;

/**
 * Created on 2018/1/17.
 * 博主账号api
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/blogger")
public class BloggerController extends BaseController {

    @Reference
    private BloggerAccountService accountService;

    @Reference
    private OnlineService onlineService;

    @PostMapping("/login/way=name")
    @TokenNotRequired
    public ResultModel loginWithUserName(@ArgVerify(rule = Rule.NOT_BLANK)
                                         @RequestParam String username,
                                         @ArgVerify(rule = Rule.NOT_BLANK)
                                         @RequestParam String password) {
        LoginVO vo = new LoginVO();
        vo.setUsername(username);
        vo.setPassword(password);

        return onlineService.login(vo);
    }

    @TokenNotRequired
    @RequestMapping(value = "/login/way=phone", method = RequestMethod.POST)
    public ResultModel loginWithPhoneNumber(@RequestParam("phone") String phone) {

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

    /**
     * 登出
     */
    @GetMapping("/logout")
    public ResultModel logout(@Uid Long uid) {
        return onlineService.logout(uid);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    @TokenNotRequired
    public ResultModel register(
            @ArgVerify(rule = Rule.NOT_BLANK)
            @RequestParam String username,
            @ArgVerify(rule = Rule.NOT_BLANK)
            @RequestParam String password) {

        handleNameCheck(username);
        if (!bloggerValidateService.checkPassword(password)) {
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);
        }

        Long id = accountService.insertAccount(username, password);
        if (id == null) handlerOperateFail();

        return ResultModel.success(id);
    }

    /**
     * 检查用户名是否存在
     */
    @GetMapping("/account/check=username")
    @TokenNotRequired
    public ResultModel checkUsernameUsed(
            @ArgVerify(rule = Rule.NOT_BLANK)
            @RequestParam String username) {
        handleNameCheck(username);

        BloggerAccountDTO account = accountService.getAccount(username);
        if (account != null) {
            return ResultModel.fail("username has been occupied", CodeMessage.COMMON_DUPLICATION_DATA.getCode());
        } else {
            return ResultModel.success();
        }

    }

    /**
     * 检查电话号码是否存在
     */
    @GetMapping("/account/check=phone")
    @TokenNotRequired
    public ResultModel checkProfileExist(
            @ArgVerify(rule = Rule.NOT_BLANK)
            @RequestParam String phone) {

        BloggerAccountDTO account = accountService.getAccountByPhone(phone);

        if (account != null) {
            return ResultModel.fail("phone number has been occupied", CodeMessage.COMMON_DUPLICATION_DATA.getCode());
        } else {
            return ResultModel.success();
        }

    }

    /**
     * 修改用户名
     */
    @PutMapping("/account/item=name")
    public ResultModel modifyUsername(@Uid Long uid,
                                      @ArgVerify(rule = Rule.NOT_BLANK)
                                      @RequestParam String username) {
        handleNameCheck(username);

        boolean result = accountService.updateAccountUserName(uid, username);
        if (!result) handlerOperateFail();

        return ResultModel.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/account/item=pwd")
    public ResultModel modifyPassword(@Uid Long uid,
                                      @ArgVerify(rule = Rule.NOT_BLANK)
                                      @RequestParam(value = "old") String oldPassword,
                                      @ArgVerify(rule = Rule.NOT_BLANK)
                                      @RequestParam(value = "new") String newPassword) {

        if (!bloggerValidateService.checkPassword(newPassword)) {
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);
        }

        boolean result = accountService.updateAccountPassword(uid, oldPassword, newPassword);
        if (!result) handlerOperateFail();

        return ResultModel.success();
    }

    /**
     * 注销账号
     */
    @DeleteMapping("/account")
    public ResultModel delete(@Uid Long uid) {

        onlineService.logout(uid);

        boolean result = accountService.deleteAccount(uid);
        if (!result) handlerOperateFail();

        return ResultModel.success();
    }

}
