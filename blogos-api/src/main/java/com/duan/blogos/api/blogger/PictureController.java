package com.duan.blogos.api.blogger;

import com.duan.blogos.annonation.TokenNotRequired;
import com.duan.blogos.annonation.Uid;
import com.duan.blogos.api.BaseController;
import com.duan.blogos.service.dto.blogger.BloggerPictureDTO;
import com.duan.blogos.service.enums.BloggerPictureCategoryEnum;
import com.duan.blogos.service.restful.PageResult;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerPictureService;
import com.duan.blogos.service.service.validate.BloggerValidateService;
import com.duan.blogos.util.CodeMessage;
import com.duan.blogos.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created on 2018/1/2.
 * 相册
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/blogger/picture")
public class PictureController extends BaseController {

    @Autowired
    private BloggerPictureService bloggerPictureService;

    @Autowired
    private BloggerValidateService validateService;

    /**
     * 根据id获取图片
     */
    @GetMapping("/{pictureId}")
    @TokenNotRequired
    public ResultModel<BloggerPictureDTO> get(@PathVariable("pictureId") Long pictureId) {

        BloggerPictureDTO picture = bloggerPictureService.getPictureWithUrl(pictureId);
        if (picture == null) handlerEmptyResult();

        return ResultModel.success(picture);
    }

    /**
     * 获得多张图片
     */
    @GetMapping
    @TokenNotRequired
    public ResultModel<PageResult<BloggerPictureDTO>> list(@RequestParam Long bloggerId,
                                                           @RequestParam(required = false) Integer category,
                                                           @RequestParam(required = false) Integer pageNum,
                                                           @RequestParam(required = false) Integer pageSize) {

        int cate = -1;
        if (category != null) {

            //检查类别是否存在
            if (BloggerPictureCategoryEnum.valueOf(category) == null) {
                throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);
            }

            //检查权限
            if (validateService.checkBloggerPictureLegal(bloggerId, category)) cate = category;
            else
                throw ExceptionUtil.get(CodeMessage.COMMON_UNAUTHORIZED);
        }

        ResultModel<PageResult<BloggerPictureDTO>> result = bloggerPictureService.listBloggerPicture(bloggerId,
                cate == -1 ? null : BloggerPictureCategoryEnum.valueOf(cate), pageNum, pageSize);
        if (result == null) handlerEmptyResult();

        return result;
    }

    /**
     * 更新图片信息
     */
    @PutMapping("/{pictureId}")
    public ResultModel update(@Uid Long bloggerId,
                              @PathVariable("pictureId") Long pictureId,
                              @RequestParam(required = false) Integer category,
                              @RequestParam(required = false) String bewrite,
                              @RequestParam(required = false) String title) {

        // 检查博主是否有指定图片
        BloggerPictureDTO picture = bloggerPictureService.getPicture(pictureId);
        if (picture == null || !bloggerId.equals(picture.getBloggerId())) {
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);
        }

        if (category == null && bewrite == null && title == null) {
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);
        }

        // 更新图片类别只适用于图片管理员，普通博主没有修改类别的必要
        if (category != null) {

            //检查类别是否存在
            if (BloggerPictureCategoryEnum.valueOf(category) == null) {
                throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);
            }

            //检查权限
            if (!validateService.checkBloggerPictureLegal(bloggerId, category))
                throw ExceptionUtil.get(CodeMessage.COMMON_UNAUTHORIZED);
        }

        boolean result = bloggerPictureService.updatePicture(pictureId,
                category == null ? null : BloggerPictureCategoryEnum.valueOf(category), bewrite, title);
        if (!result) handlerOperateFail();

        return ResultModel.success();
    }

    /**
     * 从设备和数据库中删除图片
     */
    @DeleteMapping("/{pictureId}")
    public ResultModel delete(@Uid Long bloggerId,
                              @PathVariable("pictureId") Long pictureId) {

        BloggerPictureDTO picture = bloggerPictureService.getPicture(pictureId, bloggerId);
        if (picture == null) {
            throw ExceptionUtil.get(CodeMessage.COMMON_UNKNOWN_PICTURE);
        }

        //检查权限
        if (!validateService.checkBloggerPictureLegal(bloggerId, picture.getCategory()))
            throw ExceptionUtil.get(CodeMessage.COMMON_UNAUTHORIZED);

        boolean succ = bloggerPictureService.deletePicture(bloggerId, picture.getId(), true);
        if (!succ) handlerOperateFail();

        return ResultModel.success();
    }

}
