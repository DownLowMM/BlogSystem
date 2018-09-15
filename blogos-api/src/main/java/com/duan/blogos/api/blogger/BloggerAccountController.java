package com.duan.blogos.api.blogger;

import com.duan.blogos.service.dto.blogger.BloggerAccountDTO;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ResultUtil;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerAccountService;
import com.duan.blogos.util.common.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

    /**
     * 注册
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResultModel register(HttpServletRequest request,
                                @RequestParam("username") String username,
                                @RequestParam("password") String password) {
        handleNameCheck(request, username);
        handlePwdCheck(request, password);

        int id = accountService.insertAccount(username, password);
        if (id < 0) handlerOperateFail();

        return new ResultModel<>(id);
    }

    /**
     * 检查用户名是否存在
     */
    @RequestMapping(value = "/check=username", method = RequestMethod.GET)
    public ResultModel checkUsernameUsed(HttpServletRequest request,
                                         @RequestParam("username") String username) {
        handleNameCheck(request, username);

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
    @RequestMapping(value = "/check=phone", method = RequestMethod.GET)
    public ResultModel checkProfileExist(HttpServletRequest request,
                                         @RequestParam("phone") String phone) {
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
    @RequestMapping(value = "/{bloggerId}/item=name", method = RequestMethod.PUT)
    public ResultModel modifyUsername(HttpServletRequest request,
                                      @PathVariable Integer bloggerId,
                                      @RequestParam(value = "username") String newUserName) {
        handleBloggerSignInCheck(request, bloggerId);
        handleNameCheck(request, newUserName);

        boolean result = accountService.updateAccountUserName(bloggerId, newUserName);
        if (!result) handlerOperateFail();

        // 更新session信息
        HttpSession session = request.getSession();
        session.setAttribute(sessionProperties.getBloggerName(), newUserName);

        return new ResultModel<>("");
    }

    /**
     * 修改密码
     */
    @RequestMapping(value = "/{bloggerId}/item=pwd", method = RequestMethod.PUT)
    public ResultModel modifyPassword(HttpServletRequest request,
                                      @PathVariable Integer bloggerId,
                                      @RequestParam(value = "old") String oldPassword,
                                      @RequestParam(value = "new") String newPassword) {
        handleBloggerSignInCheck(request, bloggerId);
        handlePwdCheck(request, newPassword);

        boolean result = accountService.updateAccountPassword(bloggerId, oldPassword, newPassword);
        if (!result) handlerOperateFail();

        // session 失效，重新登录
        HttpSession session = request.getSession();
        session.invalidate();

        return new ResultModel<>("");
    }


    /**
     * 注销账号
     */
    @RequestMapping(value = "/{bloggerId}", method = RequestMethod.DELETE)
    public ResultModel delete(HttpServletRequest request,
                              @PathVariable Integer bloggerId) {
        handleBloggerSignInCheck(request, bloggerId);

        boolean result = accountService.deleteAccount(bloggerId);
        if (!result) handlerOperateFail();

        // session 失效
        HttpSession session = request.getSession();
        session.invalidate();

        return new ResultModel<>("");
    }

    // 检查用户名合法性
    private void handleNameCheck(HttpServletRequest request, String userName) {
        if (StringUtils.isBlank(userName) || !bloggerValidateService.checkUserName(userName))
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);
    }

    // 检查密码合法性
    private void handlePwdCheck(HttpServletRequest request, String password) {
        if (StringUtils.isBlank(password) || !bloggerValidateService.checkPassword(password))
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);
    }
}
