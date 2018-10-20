package com.duan.blogos.service.impl.blogger;

import com.duan.blogos.service.config.preference.DefaultProperties;
import com.duan.blogos.service.config.preference.WebsiteProperties;
import com.duan.blogos.service.dao.BlogCategoryRelaDao;
import com.duan.blogos.service.dao.blog.BlogCategoryDao;
import com.duan.blogos.service.dao.blogger.BloggerPictureDao;
import com.duan.blogos.service.dto.blogger.BloggerCategoryDTO;
import com.duan.blogos.service.entity.BlogCategoryRela;
import com.duan.blogos.service.entity.blog.BlogCategory;
import com.duan.blogos.service.entity.blogger.BloggerPicture;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.manager.ImageManager;
import com.duan.blogos.service.manager.StringConstructorManager;
import com.duan.blogos.service.restful.PageResult;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerCategoryService;
import com.duan.blogos.service.util.DataConverter;
import com.duan.blogos.service.util.ExceptionUtil;
import com.duan.blogos.service.util.ResultModelUtil;
import com.duan.common.util.CollectionUtils;
import com.duan.common.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.duan.blogos.service.enums.BloggerPictureCategoryEnum.DEFAULT_BLOGGER_BLOG_CATEGORY_ICON;


/**
 * Created on 2017/12/19.
 *
 * @author DuanJiaNing
 */
@Service
public class BloggerCategoryServiceImpl implements BloggerCategoryService {

    @Autowired
    private BlogCategoryDao categoryDao;

    @Autowired
    private StringConstructorManager constructorManager;

    @Autowired
    private WebsiteProperties websiteProperties;

    @Autowired
    private DefaultProperties defaultProperties;


    @Autowired
    private ImageManager imageManager;

    @Autowired
    private BloggerPictureDao pictureDao;

    @Autowired
    private BlogCategoryRelaDao categoryRelaDao;

    @Override
    public ResultModel<PageResult<BloggerCategoryDTO>> listBlogCategory(Long bloggerId, Integer pageNum, Integer pageSize) {

        pageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
        pageSize = pageSize == null || pageSize < 1 ? defaultProperties.getCategoryCount() : pageSize;

        PageHelper.startPage(pageNum, pageSize);
        PageInfo<BlogCategory> pageInfo = new PageInfo<>(categoryDao.listCategoryByBloggerId(bloggerId));

        List<BlogCategory> categories = pageInfo.getList();
        if (CollectionUtils.isEmpty(categories)) return null;

        List<BloggerCategoryDTO> result = new ArrayList<>();
        for (BlogCategory category : categories) {
            result.add(getBloggerCategoryDTO(bloggerId, category));
        }

        return ResultModelUtil.pageResult(pageInfo, result);
    }

    @Override
    public boolean updateBlogCategory(Long bloggerId, Long categoryId, Long newIconId, String newTitle,
                                      String newBewrite) {

        BlogCategory category = categoryDao.getCategory(categoryId);
        Long oldIconId = category.getIconId();
        if (!StringUtils.isEmpty(newTitle)) category.setTitle(newTitle);
        if (!StringUtils.isEmpty(newBewrite)) category.setBewrite(newBewrite);
        if (newIconId != null) category.setIconId(newIconId);
        category.setId(categoryId);
        int effect = categoryDao.update(category);
        if (effect <= 0) return false;

        // 修改图片可见性，引用次数
        imageManager.imageUpdateHandle(bloggerId, newIconId, oldIconId);

        return true;
    }

    @Override
    public Long insertBlogCategory(Long bloggerId, Long iconId, String title, String bewrite) {

        BlogCategory category = new BlogCategory();
        category.setBewrite(bewrite);
        if (iconId != null) category.setIconId(iconId);
        category.setBloggerId(bloggerId);
        category.setTitle(title);
        int effect = categoryDao.insert(category);
        if (effect <= 0) return null;

        // 修改图片可见性，引用次数
        imageManager.imageInsertHandle(bloggerId, iconId);

        return category.getId();
    }

    @Override
    public boolean deleteCategoryAndMoveBlogsTo(Long bloggerId, Long categoryId, Long newCategoryId) {

        BlogCategory category = categoryDao.getCategory(categoryId);
        if (category == null) return false;

        // 图片引用次数--
        Long iconId;
        if ((iconId = category.getIconId()) != null && pictureDao.getUseCount(iconId) > 0) {
            pictureDao.updateUseCountMinus(iconId);
        }

        // 替换类别
        if (newCategoryId != null) {

            List<BlogCategoryRela> res = categoryRelaDao.listAllByBloggerIdAndCategoryId(bloggerId, categoryId);
            if (!CollectionUtils.isEmpty(res)) {
                List<BlogCategoryRela> relas = new ArrayList<>();
                res.forEach(re -> {
                    BlogCategoryRela rela = new BlogCategoryRela();
                    rela.setBlogId(re.getBlogId());
                    rela.setCategoryId(newCategoryId);
                    relas.add(rela);
                });
                categoryRelaDao.insertBatch(relas);
            }

        }

        // 删除数据库类别记录 外键会把 BlogCategoryRelaDao 中的数据删除
        int effectDelete = categoryDao.delete(categoryId);
        if (effectDelete <= 0)
            throw ExceptionUtil.get(CodeMessage.COMMON_UNKNOWN_ERROR, new SQLException());

        return true;
    }

    @Override
    public BloggerCategoryDTO getCategory(Long bloggerId, Long categoryId) {
        return getBloggerCategoryDTO(bloggerId, categoryDao.getCategory(categoryId));
    }

    // 获得单个类别
    private BloggerCategoryDTO getBloggerCategoryDTO(Long bloggerId, BlogCategory category) {

        Long iconId = category.getIconId();

        BloggerPicture icon;
        if (iconId == null) {
            // 默认图片
            Long pictureManagerId = websiteProperties.getManagerId();
            icon = pictureDao.getBloggerUniquePicture(pictureManagerId, DEFAULT_BLOGGER_BLOG_CATEGORY_ICON.getCode());
        } else {
            icon = pictureDao.getPictureById(iconId);
        }

        if (icon != null)
            icon.setPath(constructorManager.constructPictureUrl(icon, DEFAULT_BLOGGER_BLOG_CATEGORY_ICON));

        List<BlogCategoryRela> relas = categoryRelaDao.listAllByBloggerIdAndCategoryId(bloggerId, category.getId());
        int count = CollectionUtils.isEmpty(relas) ? 0 : relas.size();
        return DataConverter.PO2DTO.blogCategoryToDTO(category, icon, count);
    }

}
