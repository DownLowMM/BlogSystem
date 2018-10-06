package com.duan.blogos.api.blogger;

import com.duan.blogos.annonation.TokenNotRequired;
import com.duan.blogos.annonation.Uid;
import com.duan.blogos.api.BaseController;
import com.duan.blogos.service.dto.blogger.BloggerProfileDTO;
import com.duan.blogos.service.enums.BloggerPictureCategoryEnum;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ExceptionUtil;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerPictureService;
import com.duan.blogos.service.service.blogger.BloggerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

/**
 * Created on 2017/12/29.
 * 博主个人资料api
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/blogger/profile")
public class ProfileController extends BaseController {

    @Autowired
    private BloggerProfileService bloggerProfileService;

    @Autowired
    private BloggerPictureService bloggerPictureService;

    /**
     * 获取资料
     */
    @GetMapping
    @TokenNotRequired
    public ResultModel<BloggerProfileDTO> get(@RequestParam Long bloggerId) {
        handleAccountCheck(bloggerId);

        BloggerProfileDTO profile = bloggerProfileService.getBloggerProfile(bloggerId);
        if (profile == null) handlerEmptyResult();

        return ResultModel.success(profile);

    }

    /**
     * 新增资料
     */
    @PostMapping
    public ResultModel add(@Uid Long bloggerId,
                           @RequestParam(required = false) Long avatarId,
                           @RequestParam(required = false) String phone,
                           @RequestParam(required = false) String email,
                           @RequestParam(required = false) String aboutMe,
                           @RequestParam(required = false) String intro) {
        handlePictureExistCheck(bloggerId, avatarId);

        handlePhoneAndEmailCheck(phone, email);
        Long id = bloggerProfileService.insertBloggerProfile(bloggerId, avatarId,
                phone, email, aboutMe, intro);
        if (id == null) handlerOperateFail();

        return ResultModel.success(id);
    }

    /**
     * 更新资料
     */
    @PutMapping
    public ResultModel update(@Uid Long bloggerId,
                              @RequestParam(required = false) Long avatarId,
                              @RequestParam(required = false) String phone,
                              @RequestParam(required = false) String email,
                              @RequestParam(required = false) String aboutMe,
                              @RequestParam(required = false) String intro) {

        if (phone == null && email == null && aboutMe == null && intro == null) {
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);
        }

        handlePictureExistCheck(bloggerId, avatarId);

        handlePhoneAndEmailCheck(phone, email);
        boolean result = bloggerProfileService.updateBloggerProfile(bloggerId, avatarId, phone, email, aboutMe, intro);
        if (!result) handlerOperateFail();

        return ResultModel.success();
    }


    /**
     * 删除资料
     */
    @DeleteMapping
    public ResultModel delete(@Uid Long bloggerId) {

        boolean result = bloggerProfileService.deleteBloggerProfile(bloggerId);
        if (!result) handlerOperateFail();

        return ResultModel.success();
    }

    /**
     * TODO 更新头像
     */
    @PostMapping("/avatar")
    public ResultModel updateAvatar(@Uid Long bloggerId,
                                    @RequestParam(value = "avatarBaseUrlData") String base64urlData) {
        handleImageBase64Check(base64urlData);

        // 保存图片
        String base = base64urlData.replaceFirst("^data:image/(png|jpg);base64,", "");
        byte[] bs = Base64.getDecoder().decode(base);
        Long id = bloggerPictureService.insertPicture(bs, bloggerId, "once-avatar-" + bloggerId + ".png", "", BloggerPictureCategoryEnum.PUBLIC, "");
        if (id == null) handlerOperateFail();

        boolean res = bloggerProfileService.updateBloggerProfile(bloggerId, id, null, null, null, null);
        if (!res) handlerOperateFail();

        return ResultModel.success(id);
    }

}
