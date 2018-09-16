package com.duan.blogos.api.blogger;

import com.duan.base.util.common.CollectionUtils;
import com.duan.blogos.api.BaseCheckController;
import com.duan.blogos.config.SessionProperties;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * Created on 2017/12/28.
 * 该家族的大多数操作都需要博主登录
 *
 * @author DuanJiaNing
 */
public class BaseBloggerController extends BaseCheckController {

    @Autowired
    protected SessionProperties sessionProperties;

    /**
     * 检查所有参数是否都为null，在更新时这种情况下更新操作将取消
     *
     * @param objs 当且仅当这些参数全为null时抛出异常
     */
    protected void handleParamAllNullForUpdate(HttpServletRequest request, Object... objs) {
        if (CollectionUtils.isEmpty(objs))
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);

        for (Object obj : objs) {
            if (obj != null) return;
        }

        throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);
    }

    /**
     * 检查指定博主的图片存在
     *
     * @param bloggerId 博主id
     * @param pictureId 图片id
     */
    protected void handlePictureExistCheck(HttpServletRequest request, Integer bloggerId, Integer pictureId) {
        if (pictureId != null && !bloggerValidateService.checkBloggerPictureExist(bloggerId, pictureId))
            throw ResultUtil.failException(CodeMessage.COMMON_UNKNOWN_PICTURE);
    }
}
