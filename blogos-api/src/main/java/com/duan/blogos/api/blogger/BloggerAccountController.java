package com.duan.blogos.api.blogger;

import com.duan.blogos.annonation.TokenNotRequired;
import com.duan.blogos.annonation.Uid;
import com.duan.blogos.service.dto.blogger.BloggerAccountDTO;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ResultUtil;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerAccountService;
import com.duan.blogos.service.service.common.OnlineService;
import com.duan.common.spring.verify.Rule;
import com.duan.common.spring.verify.annoation.parameter.ArgVerify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created on 2018/1/17.
 * 博主账号api
 * <p>
 * 1 注册账号
 * 2 修改用户名
 * 3 修改密码
 * 4 注销账号
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/blogger/account")
public class BloggerAccountController extends BaseBloggerController {

    @Autowired
    private BloggerAccountService accountService;

    @Autowired
    private OnlineService onlineService;

    /**
     * 注册
     */
    @PostMapping
    @TokenNotRequired
    public ResultModel register(
            @ArgVerify(rule = Rule.NOT_BLANK)
            @RequestParam String username,
            @ArgVerify(rule = Rule.NOT_BLANK)
            @RequestParam String password) {

        handleNameCheck(username);
        if (!bloggerValidateService.checkPassword(password)) {
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);
        }

        Long id = accountService.insertAccount(username, password);
        if (id == null) handlerOperateFail();

        return new ResultModel<>(id);
    }

    /**
     * 检查用户名是否存在
     */
    @GetMapping("/check=username")
    @TokenNotRequired
    public ResultModel checkUsernameUsed(
            @ArgVerify(rule = Rule.NOT_BLANK)
            @RequestParam String username) {
        handleNameCheck(username);

        BloggerAccountDTO account = accountService.getAccount(username);
        if (account != null) {
            return new ResultModel(ResultUtil.failException(CodeMessage.COMMON_DUPLICATION_DATA));
        } else {
            return new ResultModel<>("");
        }

    }

    /**
     * 检查电话号码是否存在
     */
    @GetMapping("/check=phone")
    @TokenNotRequired
    public ResultModel checkProfileExist(
            @ArgVerify(rule = Rule.NOT_BLANK)
            @RequestParam String phone) {

        BloggerAccountDTO account = accountService.getAccountByPhone(phone);

        if (account != null) {
            return new ResultModel(ResultUtil.failException(CodeMessage.COMMON_DUPLICATION_DATA));
        } else {
            return new ResultModel<>("");
        }

    }

    /**
     * 修改用户名
     */
    @PutMapping("/item=name")
    public ResultModel modifyUsername(@Uid Long uid,
                                      @ArgVerify(rule = Rule.NOT_BLANK)
                                      @RequestParam(value = "username") String newUserName) {
        handleNameCheck(newUserName);

        boolean result = accountService.updateAccountUserName(uid, newUserName);
        if (!result) handlerOperateFail();

        return new ResultModel<>("");
    }

    /**
     * 修改密码
     */
    @PutMapping("/item=pwd")
    public ResultModel modifyPassword(@Uid Long uid,
                                      @ArgVerify(rule = Rule.NOT_BLANK)
                                      @RequestParam(value = "old") String oldPassword,
                                      @ArgVerify(rule = Rule.NOT_BLANK)
                                      @RequestParam(value = "new") String newPassword) {

        if (!bloggerValidateService.checkPassword(newPassword)) {
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);
        }

        boolean result = accountService.updateAccountPassword(uid, oldPassword, newPassword);
        if (!result) handlerOperateFail();

        return new ResultModel<>("");
    }

    /**
     * 注销账号
     */
    @DeleteMapping
    public ResultModel delete(@Uid Long uid) {

        onlineService.logout(uid);

        boolean result = accountService.deleteAccount(uid);
        if (!result) handlerOperateFail();

        return new ResultModel<>("");
    }

}
