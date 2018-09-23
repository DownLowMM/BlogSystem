package com.duan.blogos.api.common;

import com.duan.blogos.api.BaseCheckController;
import com.duan.blogos.service.dto.blogger.BloggerPictureDTO;
import com.duan.blogos.service.enums.BloggerPictureCategoryEnum;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ResultUtil;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerPictureService;
import com.duan.blogos.service.service.validate.BloggerValidateService;
import com.duan.common.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created on 2018/1/2.
 * 图片上传、下载、删除
 * <p>
 * 1 输出图片
 * 2 上传图片
 * 3 从设备和数据库中删除图片
 * <p>
 * 参加ImageController#constructPictureUrl
 *
 * @author DuanJiaNing
 */
@Controller
@RequestMapping("/image/{bloggerId}")
public class ImageController extends BaseCheckController {

    @Autowired
    private BloggerPictureService bloggerPictureService;

    @Autowired
    private BloggerValidateService validateService;

    /**
     * 输出公开图片，这些图片无需验证登录，如果数据库不存在指定图片，则返回默认图片
     */
    @RequestMapping(value = "/type=public/{imageId}", method = RequestMethod.GET)
    public void getBlogPicture(HttpServletRequest request, HttpServletResponse response,
                               @PathVariable("bloggerId") Long bloggerId,
                               @PathVariable("imageId") Long imageId,
                               @RequestParam(value = "default", required = false) Integer category) {
        handleAccountCheck(bloggerId);

        // 检查default是否为默认类别
        if (category != null)
            handleBlogCategoryDefaultCheck(request, category);

        BloggerPictureDTO picture = bloggerPictureService.getPicture(imageId, bloggerId);

        // 如果图片是私有的，不能访问
        if (picture != null && picture.getCategory().equals(BloggerPictureCategoryEnum.PRIVATE.getCode()))
            throw ResultUtil.failException(CodeMessage.COMMON_UNAUTHORIZED);

        BloggerPictureDTO backupPicture = bloggerPictureService.getDefaultPicture(
                category == null ? BloggerPictureCategoryEnum.DEFAULT_PICTURE
                        : BloggerPictureCategoryEnum.valueOf(category)); //如果目标图片不存在，返回指定类别的默认图片

        // 输出图片
        outPutPicture(picture, backupPicture, request, response);

    }


    /**
     * 获取博主的私有图片（任意图片），这些图片需要验证登录
     */
    @RequestMapping(value = "/type=private/{imageId}", method = RequestMethod.GET)
    public void getBloggerPicture(HttpServletRequest request, HttpServletResponse response,
                                  @PathVariable("bloggerId") Long bloggerId,
                                  @PathVariable("imageId") Long imageId,
                                  @RequestParam(value = "default", required = false) Integer category) {

        // 检查默认图片类别是否为默认类别
        if (category != null)
            handleBlogCategoryDefaultCheck(request, category);

        BloggerPictureDTO picture = bloggerPictureService.getPicture(imageId, bloggerId);
        BloggerPictureDTO backupPicture = bloggerPictureService.getDefaultPicture(
                category == null ? BloggerPictureCategoryEnum.DEFAULT_PICTURE
                        : BloggerPictureCategoryEnum.valueOf(category)); //如果目标图片不存在，返回指定类别的默认图片

        // 输出图片
        outPutPicture(picture, backupPicture, request, response);

    }

    /**
     * 上传图片
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResultModel upload(MultipartHttpServletRequest request,
                              @PathVariable("bloggerId") Long bloggerId,
                              @RequestParam(value = "category", required = false) Integer category,
                              @RequestParam(value = "bewrite", required = false) String bewrite,
                              @RequestParam(value = "title", required = false) String title) {

        MultipartFile file = request.getFile("image");// 与页面input的name相同
        Long id;
        if (ImageUtils.isImageFile(fileTrans(file))) {

            // 默认上传到私有目录
            int cate = category == null ? BloggerPictureCategoryEnum.PRIVATE.getCode() : category;

            // 普通用户没有指定图片类别的必要
            //检查博主权限
            if (!validateService.checkBloggerPictureLegal(bloggerId, cate)) {
                throw ResultUtil.failException(CodeMessage.COMMON_UNAUTHORIZED);
            }

            id = bloggerPictureService.insertPicture(fileTrans(file), bloggerId, bewrite, BloggerPictureCategoryEnum.valueOf(cate),
                    title);
            if (id == null) handlerOperateFail();
        } else {
            throw ResultUtil.failException(CodeMessage.COMMON_PICTURE_FORMAT_ERROR);
        }

        return new ResultModel<>(id);
    }

    private com.duan.common.util.MultipartFile fileTrans(MultipartFile file) {
        return null;
    }

    // 检查默认图片类别是否为默认类别
    private void handleBlogCategoryDefaultCheck(HttpServletRequest request, int category) {
        if (!BloggerPictureCategoryEnum.isDefaultPictureCategory(category))
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);
    }


    // 输出图片
    private void outPutPicture(BloggerPictureDTO picture, BloggerPictureDTO backupPicture,
                               HttpServletRequest request, HttpServletResponse response) {
        try (ServletOutputStream os = response.getOutputStream()) {
            String path = picture == null ? backupPicture.getPath() : picture.getPath();
            File image = new File(path);
            if (!image.exists()) handlerOperateFail();

            String type = ImageUtils.getImageMimeType(image.getName());
            if (type == null) handlerOperateFail();

            response.setContentType("image/" + type);

            BufferedImage read = ImageIO.read(image);
            ImageIO.write(read, type, os);
        } catch (IOException e) {
            e.printStackTrace();
            handlerOperateFail(e);
        }
    }

}
