package com.duan.blogos.api.blogger;

import com.duan.blogos.api.BaseCheckController;
import com.duan.blogos.config.SessionProperties;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created on 2017/12/28.
 * 需要博主登录
 *
 * @author DuanJiaNing
 */
public class BaseBloggerController extends BaseCheckController {

    @Autowired
    protected SessionProperties sessionProperties;

    /**
     * 检查指定博主的图片存在
     *
     * @param bloggerId 博主id
     * @param pictureId 图片id
     */
    protected void handlePictureExistCheck(Long bloggerId, Long pictureId) {
        if (pictureId != null && !bloggerValidateService.checkBloggerPictureExist(bloggerId, pictureId))
            throw ResultUtil.failException(CodeMessage.COMMON_UNKNOWN_PICTURE);
    }
}
