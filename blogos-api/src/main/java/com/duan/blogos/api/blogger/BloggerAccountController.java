package com.duan.blogos.api.blogger;

import com.duan.blogos.annonation.TokenNotRequired;
import com.duan.blogos.annonation.Uid;
import com.duan.blogos.service.dto.blogger.BloggerAccountDTO;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ResultUtil;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerAccountService;
import com.duan.blogos.service.service.common.OnlineService;
import com.duan.common.util.StringUtils;
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
@RequestMapping("/blogger")
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
    public ResultModel register(@RequestParam String username,
                                @RequestParam String password) {
        handleNameCheck(username);
        handlePwdCheck(password);

        Long id = accountService.insertAccount(username, password);
        if (id == null) handlerOperateFail();

        return new ResultModel<>(id);
    }

    /**
     * 检查用户名是否存在
     */
    @GetMapping("/check=username")
    @TokenNotRequired
    public ResultModel checkUsernameUsed(@RequestParam String username) {
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
    public ResultModel checkProfileExist(@RequestParam String phone) {
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
    public ResultModel modifyUsername(@Uid Long uid, @RequestParam(value = "username") String newUserName) {
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
                                      @RequestParam(value = "old") String oldPassword,
                                      @RequestParam(value = "new") String newPassword) {
        handlePwdCheck(newPassword);

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

    // 检查用户名合法性
    private void handleNameCheck(String userName) {
        if (StringUtils.isBlank(userName) || !bloggerValidateService.checkUserName(userName))
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);
    }

    // 检查密码合法性
    private void handlePwdCheck(String password) {
        if (StringUtils.isBlank(password) || !bloggerValidateService.checkPassword(password))
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);
    }
}
