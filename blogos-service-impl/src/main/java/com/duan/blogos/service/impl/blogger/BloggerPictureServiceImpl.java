package com.duan.blogos.service.impl.blogger;

import com.duan.blogos.service.config.preference.DefaultProperties;
import com.duan.blogos.service.config.preference.WebsiteProperties;
import com.duan.blogos.service.dao.blogger.BloggerPictureDao;
import com.duan.blogos.service.dto.blogger.BloggerPictureDTO;
import com.duan.blogos.service.entity.blogger.BloggerPicture;
import com.duan.blogos.service.enums.BloggerPictureCategoryEnum;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ExceptionUtil;
import com.duan.blogos.service.manager.DataFillingManager;
import com.duan.blogos.service.manager.ImageManager;
import com.duan.blogos.service.manager.StringConstructorManager;
import com.duan.blogos.service.restful.PageResult;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerPictureService;
import com.duan.blogos.service.util.ResultModelUtil;
import com.duan.blogos.service.vo.FileVO;
import com.duan.common.util.CollectionUtils;
import com.duan.common.util.ImageUtils;
import com.duan.common.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.duan.blogos.service.enums.BloggerPictureCategoryEnum.DEFAULT_PICTURE;

/**
 * Created on 2017/12/19.
 *
 * @author DuanJiaNing
 */
@Service
public class BloggerPictureServiceImpl implements BloggerPictureService {

    @Autowired
    private StringConstructorManager stringConstructorManager;

    @Autowired
    private BloggerPictureDao pictureDao;

    @Autowired
    private ImageManager imageManager;

    @Autowired
    private WebsiteProperties websiteProperties;

    @Autowired
    private DefaultProperties defaultProperties;

    @Autowired
    private DataFillingManager dataFillingManager;


    @Override
    public Long insertPicture(Long bloggerId, String path, String bewrite, BloggerPictureCategoryEnum category, String title) {
        BloggerPicture picture = new BloggerPicture();
        picture.setBewrite(bewrite);
        picture.setBloggerId(bloggerId);
        picture.setCategory(category.getCode());
        picture.setPath(path);
        picture.setTitle(title);
        int effect = pictureDao.insert(picture);
        if (effect <= 0) return null;

        return picture.getId();
    }

    @Override
    public Long insertPicture(FileVO file, Long bloggerId, String bewrite, BloggerPictureCategoryEnum category,
                              String title) {

        // 默认上传到私有目录
        BloggerPictureCategoryEnum cate = category == null ? BloggerPictureCategoryEnum.PRIVATE : category;

        //保存到磁盘
        String path;
        try {
            path = imageManager.saveImageToDisk(file, bloggerId, cate.getCode());
        } catch (IOException e) {
            e.printStackTrace();
            throw ExceptionUtil.get(CodeMessage.COMMON_UNKNOWN_ERROR, e);
        }
        if (path == null) return null;

        // 如果是图片管理员上传默认图片，需要移动其文件夹
        Long pictureManagerId = websiteProperties.getManagerId();
        if (pictureManagerId.equals(bloggerId) && BloggerPictureCategoryEnum.isDefaultPictureCategory(cate)) {
            // 如果设备上已经有该唯一图片，将原来的图片移到私有文件夹，同时修改数据库
            removeDefaultPictureIfNecessary(bloggerId, cate);
        }

        //插入新纪录
        String ti = StringUtils.isEmpty(title) ? ImageUtils.getImageName(file.getOriginalFilename()) : title;
        return insertPicture(bloggerId, path, bewrite, cate, ti);

    }

    @Override
    public Long insertPicture(byte[] bs, Long bloggerId, String name, String bewrite, BloggerPictureCategoryEnum category, String title) {

        int cate = category.getCode();
        String path;

        //保存到磁盘
        try {
            path = imageManager.saveImageToDisk(bs, name, bloggerId, cate);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if (path == null) return null;

        // 如果是图片管理员上传默认图片，需要移动其文件夹
        Long pictureManagerId = websiteProperties.getManagerId();
        if (pictureManagerId.equals(bloggerId) && BloggerPictureCategoryEnum.isDefaultPictureCategory(cate)) {
            // 如果设备上已经有该唯一图片，将原来的图片移到私有文件夹，同时修改数据库
            removeDefaultPictureIfNecessary(bloggerId, category);
        }

        //插入新纪录
        String ti = StringUtils.isEmpty(title) ? ImageUtils.getImageName(name) : title;
        return insertPicture(bloggerId, path, bewrite, category, ti);

    }

    /*
     * 腾地方
     * 移到默认图片到私有图片文件夹，同时修改数据库记录
     * @param bloggerId 博主id
     */
    private void removeDefaultPictureIfNecessary(Long bloggerId, BloggerPictureCategoryEnum defaultCate) {
        BloggerPicture unique = pictureDao.getBloggerUniquePicture(bloggerId, defaultCate.getCode());

        if (unique != null) {
            try {
                //移动默认图片到私有类别图片所在文件夹
                String newPath = imageManager.moveImage(unique, bloggerId, BloggerPictureCategoryEnum.PRIVATE);

                //更新数据库记录
                unique.setCategory(BloggerPictureCategoryEnum.PRIVATE.getCode());
                unique.setPath(newPath);
                pictureDao.update(unique);

            } catch (IOException e) {
                e.printStackTrace();
                // 移动文件出错，文件移动情况未知，麻烦大了
                // MAYBUG 回滚数据库操作，但磁盘操作无法预料，也无法处理
                throw ExceptionUtil.get(CodeMessage.COMMON_UNKNOWN_ERROR, e);
            }
        }

    }

    @Override
    public boolean deletePicture(Long bloggerId, Long pictureId, boolean deleteOnDisk) {

        BloggerPictureDTO picture = getPicture(pictureId);

        // 对默认图片，图片管理员只能以更新（上传）的方式删除图片，因为这些图片必须时刻存在
        Long pictureManagerId = websiteProperties.getManagerId();
        int cate = picture.getCategory();
        if (bloggerId.equals(pictureManagerId) && BloggerPictureCategoryEnum.isDefaultPictureCategory(cate))
            return false;

        //删除数据库记录
        String path = picture.getPath();
        int effect = pictureDao.delete(pictureId);
        if (effect <= 0) return false;

        if (deleteOnDisk) {
            //删除磁盘文件
            boolean succ = imageManager.deleteImageFromDisk(path);
            // 删除失败时抛出异常，使数据库事务回滚
            if (!succ)
                throw ExceptionUtil.get(CodeMessage.COMMON_UNKNOWN_ERROR, new IOException());
        }

        return true;
    }

    @Override
    public BloggerPictureDTO getPicture(Long pictureId) {
        BloggerPicture picture = pictureDao.getPictureById(pictureId);
        return dataFillingManager.bloggerPicture2DTO(picture);
    }

    @Override
    public BloggerPictureDTO getPicture(Long pictureId, Long bloggerId) {
        BloggerPicture picture = pictureDao.getPictureById(pictureId);
        if (picture == null || !picture.getBloggerId().equals(bloggerId)) return null;
        return dataFillingManager.bloggerPicture2DTO(picture);
    }

    @Override
    public BloggerPictureDTO getDefaultPicture(BloggerPictureCategoryEnum category) {
        BloggerPicture picture = pictureDao.getBloggerUniquePicture(websiteProperties.getManagerId(),
                category.getCode());

        if (picture == null) {
            return null; // 没有默认的图片
        }

        return dataFillingManager.bloggerPicture2DTO(picture);
    }

    @Override
    public ResultModel<PageResult<BloggerPictureDTO>> listBloggerPicture(Long bloggerId, BloggerPictureCategoryEnum category,
                                                                         Integer pageNum, Integer pageSize) {

        pageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
        pageSize = pageSize == null || pageSize < 1 ? defaultProperties.getPictureCount() : pageSize;

        PageHelper.startPage(pageNum, pageSize);
        PageInfo<BloggerPicture> pageInfo = null;
        if (category == null) {
            pageInfo = new PageInfo<>(pictureDao.listPictureByBloggerId(bloggerId));
        } else {
            pageInfo = new PageInfo<>(pictureDao.listPictureByBloggerAndCategory(bloggerId, category.getCode()));
        }

        List<BloggerPicture> result = pageInfo.getList();
        if (CollectionUtils.isEmpty(result)) return null;

        for (BloggerPicture picture : result) {
            String url = stringConstructorManager.constructPictureUrl(picture, DEFAULT_PICTURE);
            picture.setPath(url);
        }

        List<BloggerPictureDTO> dtos = result.stream()
                .map(dataFillingManager::bloggerPicture2DTO)
                .collect(Collectors.toList());

        return ResultModelUtil.pageResult(pageInfo, dtos);
    }

    @Override
    public boolean updatePicture(Long pictureId, BloggerPictureCategoryEnum category, String bewrite, String title) {

        BloggerPicture oldPicture = pictureDao.getPictureById(pictureId);

        // 修改设备上图片路径，如果需要的话
        String newPath = null;
        if (category != null && category.getCode() != oldPicture.getCategory()) { // 修改了类别
            Long bloggerId = oldPicture.getBloggerId();
            try {

                Long pictureManagerId = websiteProperties.getManagerId();
                // 如果为图片管理员在操作
                if (pictureManagerId.equals(bloggerId)) {

                    // 以下两种情况将更新失败，对于默认图片，且图片管理员在操作的情况下，要修改类别或删除图片，只能
                    // 以 普通 -> 默认 的修改方向替换图片，因为这些图片必须时刻存在

                    // 1 目标类别是默认类别，原先类别也为默认类别      默认 -> 默认
                    // 2 目标类别为普通类别，原先类别为默认类别        默认 -> 普通

                    int oldCategory = oldPicture.getCategory();
                    if ((BloggerPictureCategoryEnum.isDefaultPictureCategory(oldCategory) &&
                            BloggerPictureCategoryEnum.isDefaultPictureCategory(category.getCode())) ||
                            (BloggerPictureCategoryEnum.isDefaultPictureCategory(oldCategory) &&
                                    !BloggerPictureCategoryEnum.isDefaultPictureCategory(category.getCode()))) {
                        return false;
                    } else {

                        //腾位置，如果需要的话
                        removeDefaultPictureIfNecessary(bloggerId, category);

                        //移动到目标目录
                        newPath = imageManager.moveImage(oldPicture, bloggerId, category);
                    }

                } else {
                    // 不是图片管理员则只需移动文件即可
                    newPath = imageManager.moveImage(oldPicture, bloggerId, category);
                }

            } catch (IOException e) {
                e.printStackTrace();
                throw ExceptionUtil.get(CodeMessage.COMMON_UNKNOWN_ERROR, e);
            }
        }

        BloggerPicture newPicture = new BloggerPicture();
        newPicture.setBewrite(bewrite);
        newPicture.setBloggerId(oldPicture.getBloggerId());
        newPicture.setCategory(category == null ? oldPicture.getCategory() : category.getCode());
        newPicture.setId(oldPicture.getId());
        newPicture.setTitle(title);
        newPicture.setPath(newPath == null ? oldPicture.getPath() : newPath);

        int effect = pictureDao.update(newPicture);
        if (effect <= 0) return false;

        return true;
    }

    @Override
    public void cleanBlogPicture(Long bloggerId) {
    }

    @Override
    public BloggerPictureDTO getPictureWithUrl(Long pictureId) {

        BloggerPicture picture = pictureDao.getPictureById(pictureId);
        if (picture == null) {
            return null;
        }

        String url = stringConstructorManager.constructPictureUrl(picture, BloggerPictureCategoryEnum.DEFAULT_PICTURE);
        picture.setPath(url);

        return dataFillingManager.bloggerPicture2DTO(picture);
    }

}
