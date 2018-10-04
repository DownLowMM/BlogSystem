package com.duan.blogos.api.blogger;

import com.duan.blogos.api.BaseController;
import com.duan.blogos.service.dto.blogger.BloggerPictureDTO;
import com.duan.blogos.service.enums.BloggerPictureCategoryEnum;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ExceptionUtil;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerPictureService;
import com.duan.blogos.service.service.validate.BloggerValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created on 2018/1/2.
 * 相册
 * <p>
 * 1 根据id获取图片
 * 2 获得多张图片
 * 3 更新图片信息
 * 4 从设备和数据库中删除图片
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/blogger/{bloggerId}/picture")
public class PictureController extends BaseController {

    @Autowired
    private BloggerPictureService bloggerPictureService;

    @Autowired
    private BloggerValidateService validateService;

    /**
     * 根据id获取图片
     */
    @GetMapping("/{pictureId}")
    public ResultModel<BloggerPictureDTO> get(@PathVariable("bloggerId") Long bloggerId,
                                              @PathVariable("pictureId") Long pictureId) {

        if (pictureId == null)
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);

        BloggerPictureDTO picture = bloggerPictureService.getPicture(pictureId, bloggerId);
        if (picture == null) handlerEmptyResult();

        return ResultModel.success(picture);
    }

    /**
     * 获得多张图片
     */
    @GetMapping
    public ResultModel<List<BloggerPictureDTO>> list(@PathVariable("bloggerId") Long bloggerId,
                                                     @RequestParam(value = "category", required = false) Integer category,
                                                     @RequestParam(value = "offset", required = false) Integer offset,
                                                     @RequestParam(value = "rows", required = false) Integer rows) {

        int cate;
        if (category != null) {

            //检查类别是否存在
            if (BloggerPictureCategoryEnum.valueOf(category) == null) {
                throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);
            }

            //检查权限
            if (validateService.checkBloggerPictureLegal(bloggerId, category)) cate = category;
            else
                throw ExceptionUtil.get(CodeMessage.COMMON_UNAUTHORIZED);
        } else cate = -1;

        ResultModel<List<BloggerPictureDTO>> result = bloggerPictureService.listBloggerPicture(bloggerId,
                cate == -1 ? null : BloggerPictureCategoryEnum.valueOf(cate),
                offset == null ? 0 : offset, rows == null ? -1 : rows);
        if (result == null) handlerEmptyResult();

        return result;
    }

    /**
     * 更新图片信息
     */
    @PutMapping("/{pictureId}")
    public ResultModel update(@PathVariable("bloggerId") Long bloggerId,
                              @PathVariable("pictureId") Long pictureId,
                              @RequestParam(value = "category", required = false) Integer newCategory,
                              @RequestParam(value = "bewrite", required = false) String newBeWrite,
                              @RequestParam(value = "title", required = false) String newTitle) {

        // 检查博主是否有指定图片
        BloggerPictureDTO picture = bloggerPictureService.getPicture(pictureId);
        if (picture == null || !bloggerId.equals(picture.getBloggerId())) {
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);
        }

        if (newCategory == null && newBeWrite == null && newTitle == null) {
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);
        }

        // 更新图片类别只适用于图片管理员，普通博主没有修改类别的必要
        if (newCategory != null) {

            //检查类别是否存在
            if (BloggerPictureCategoryEnum.valueOf(newCategory) == null) {
                throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);
            }

            //检查权限
            if (!validateService.checkBloggerPictureLegal(bloggerId, newCategory))
                throw ExceptionUtil.get(CodeMessage.COMMON_UNAUTHORIZED);
        }

        boolean result = bloggerPictureService.updatePicture(pictureId,
                newCategory == null ? null : BloggerPictureCategoryEnum.valueOf(newCategory), newBeWrite, newTitle);
        if (!result) handlerOperateFail();

        return ResultModel.success();
    }

    /**
     * 从设备和数据库中删除图片
     */
    @DeleteMapping("/{pictureId}")
    @ResponseBody
    public ResultModel delete(@PathVariable("bloggerId") Long bloggerId,
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
