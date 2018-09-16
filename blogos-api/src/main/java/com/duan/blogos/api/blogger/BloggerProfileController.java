package com.duan.blogos.api.blogger;

import com.duan.base.util.common.StringUtils;
import com.duan.blogos.service.dto.blogger.BloggerProfileDTO;
import com.duan.blogos.service.enums.BloggerPictureCategoryEnum;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ResultUtil;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerPictureService;
import com.duan.blogos.service.service.blogger.BloggerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

/**
 * Created on 2017/12/29.
 * 博主个人资料api
 * <p>
 * 1 获取资料
 * 2 新增资料
 * 3 更新资料
 * 4 删除资料
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/blogger/{bloggerId}/profile")
public class BloggerProfileController extends BaseBloggerController {

    @Autowired
    private BloggerProfileService bloggerProfileService;

    @Autowired
    private BloggerPictureService bloggerPictureService;

    /**
     * 获取资料
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResultModel<BloggerProfileDTO> get(HttpServletRequest request,
                                              @PathVariable Integer bloggerId) {
        handleAccountCheck(request, bloggerId);

        BloggerProfileDTO profile = bloggerProfileService.getBloggerProfile(bloggerId);
        if (profile == null) handlerEmptyResult();

        return new ResultModel<>(profile);
    }

    /**
     * 新增资料
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResultModel add(HttpServletRequest request,
                           @PathVariable Integer bloggerId,
                           @RequestParam(value = "avatarId", required = false) Integer avatarId,
                           @RequestParam(value = "phone", required = false) String phone,
                           @RequestParam(value = "email", required = false) String email,
                           @RequestParam(value = "aboutMe", required = false) String aboutMe,
                           @RequestParam(value = "intro", required = false) String intro) {
        handleBloggerSignInCheck(request, bloggerId);
        handlePictureExistCheck(request, bloggerId, avatarId);

        handleParamsCheck(phone, email, request);
        int id = bloggerProfileService.insertBloggerProfile(bloggerId, avatarId == null || avatarId <= 0 ? -1 : avatarId,
                phone, email, aboutMe, intro);
        if (id <= 0) handlerOperateFail();

        return new ResultModel<>(id);
    }

    /**
     * 更新资料
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResultModel update(HttpServletRequest request,
                              @PathVariable Integer bloggerId,
                              @RequestParam(value = "avatarId", required = false) Integer avatarId,
                              @RequestParam(value = "phone", required = false) String phone,
                              @RequestParam(value = "email", required = false) String email,
                              @RequestParam(value = "aboutMe", required = false) String aboutMe,
                              @RequestParam(value = "intro", required = false) String intro) {

        if (phone == null && email == null && aboutMe == null && intro == null) {
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);
        }

        handleBloggerSignInCheck(request, bloggerId);
        handlePictureExistCheck(request, bloggerId, avatarId);

        handleParamsCheck(phone, email, request);
        int av = avatarId == null || avatarId <= 0 ? -1 : avatarId;
        boolean result = bloggerProfileService.updateBloggerProfile(bloggerId, av, phone, email, aboutMe, intro);
        if (!result) handlerOperateFail();

        return new ResultModel<>("");
    }


    /**
     * 删除资料
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResultModel delete(HttpServletRequest request,
                              @PathVariable Integer bloggerId) {
        handleBloggerSignInCheck(request, bloggerId);

        boolean result = bloggerProfileService.deleteBloggerProfile(bloggerId);
        if (!result) handlerOperateFail();

        return new ResultModel<>("");
    }

    /**
     * 更新头像
     */
    @RequestMapping(value = "/avatar", method = RequestMethod.POST)
    public ResultModel updateAvatar(HttpServletRequest request,
                                    @PathVariable Integer bloggerId,
                                    @RequestParam(value = "avatarBaseUrlData") String base64urlData) {
        handleImageBase64Check(request, base64urlData);
        handleBloggerSignInCheck(request, bloggerId);

        // 保存图片
        String base = base64urlData.replaceFirst("^data:image/(png|jpg);base64,", "");
        byte[] bs = Base64.getDecoder().decode(base);
        int id = bloggerPictureService.insertPicture(bs, bloggerId, "once-avatar-" + bloggerId + ".png", "", BloggerPictureCategoryEnum.PUBLIC, "");
        if (id <= 0) handlerOperateFail();

        boolean res = bloggerProfileService.updateBloggerProfile(bloggerId, id, null, null, null, null);
        if (!res) handlerOperateFail();

        return new ResultModel<>(id);
    }

    private void handleImageBase64Check(HttpServletRequest request, String base64urlData) {

        if (!base64urlData.contains("data:image") || !base64urlData.contains("base64")) {
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_FORMAT_ILLEGAL);
        }

    }

    private void handleParamsCheck(String phone, String email, HttpServletRequest request) {
        RequestContext context = new RequestContext(request);
        if (phone != null && !StringUtils.isPhone(phone))
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_FORMAT_ILLEGAL);

        if (email != null && !StringUtils.isEmail(email))
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_FORMAT_ILLEGAL);
    }

}
