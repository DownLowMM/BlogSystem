package com.duan.blogos.service.impl.blogger;

import com.duan.blogos.service.config.preference.DbProperties;
import com.duan.blogos.service.config.preference.DefaultProperties;
import com.duan.blogos.service.config.preference.WebsiteProperties;
import com.duan.blogos.service.dao.blog.BlogCategoryDao;
import com.duan.blogos.service.dao.blog.BlogDao;
import com.duan.blogos.service.dao.blogger.BloggerPictureDao;
import com.duan.blogos.service.dto.blogger.BloggerCategoryDTO;
import com.duan.blogos.service.entity.blog.Blog;
import com.duan.blogos.service.entity.blog.BlogCategory;
import com.duan.blogos.service.entity.blogger.BloggerPicture;
import com.duan.blogos.service.enums.BlogStatusEnum;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ResultUtil;
import com.duan.blogos.service.manager.DataFillingManager;
import com.duan.blogos.service.manager.ImageManager;
import com.duan.blogos.service.manager.StringConstructorManager;
import com.duan.blogos.service.restful.PageResult;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerCategoryService;
import com.duan.common.util.ArrayUtils;
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
    private DataFillingManager fillingManager;

    @Autowired
    private DbProperties dbProperties;

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
    private BlogDao blogDao;

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

        return new ResultModel<>(new PageResult<>(pageInfo.getTotal(), result));
    }

    @Override
    public boolean updateBlogCategory(Long bloggerId, Long categoryId, Long newIconId, String newTitle,
                                      String newBewrite) {

        BlogCategory category = categoryDao.getCategory(bloggerId, categoryId);
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

        BlogCategory category = categoryDao.getCategory(bloggerId, categoryId);
        if (category == null) return false;

        // 图片引用次数--
        Long iconId;
        if ((iconId = category.getIconId()) != null && pictureDao.getUseCount(iconId) > 0) {
            pictureDao.updateUseCountMinus(iconId);
        }

        // 删除数据库类别记录
        int effectDelete = categoryDao.delete(categoryId);
        if (effectDelete <= 0)
            throw ResultUtil.failException(CodeMessage.COMMON_UNKNOWN_ERROR, new SQLException());

        // 修改博文类别
        List<Blog> blogs = blogDao.listAllCategoryByBloggerId(bloggerId);
        String sp = dbProperties.getStringFiledSplitCharacterForNumber();

        // 移除类别即可
        if (newCategoryId == null) {
            blogs.forEach(blog -> {

                Long[] cids = StringUtils.longStringDistinctToArray(blog.getCategoryIds(), sp);
                if (CollectionUtils.longArrayContain(cids, categoryId)) {
                    Long[] ar = ArrayUtils.removeFromArray(cids, categoryId);
                    blog.setCategoryIds(StringUtils.longArrayToString(ar, sp));
                    int effectUpdate = blogDao.update(blog);
                    if (effectUpdate <= 0)
                        throw ResultUtil.failException(CodeMessage.COMMON_UNKNOWN_ERROR, new SQLException());
                }

            });

        } else { // 替换类别
            blogs.forEach(blog -> {

                Long[] cids = StringUtils.longStringDistinctToArray(blog.getCategoryIds(), sp);
                if (CollectionUtils.longArrayContain(cids, categoryId)) {
                    ArrayUtils.replace(cids, categoryId, newCategoryId);
                    blog.setCategoryIds(StringUtils.longArrayToString(cids, sp));
                    int effectUpdate = blogDao.update(blog);
                    if (effectUpdate <= 0)
                        throw ResultUtil.failException(CodeMessage.COMMON_UNKNOWN_ERROR, new SQLException());
                }
            });

        }

        return true;
    }

    @Override
    public BloggerCategoryDTO getCategory(Long bloggerId, Long categoryId) {
        return getBloggerCategoryDTO(bloggerId, categoryDao.getCategory(bloggerId, categoryId));
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

        int count = blogDao.countBlogByCategory(bloggerId, category.getId(), BlogStatusEnum.PUBLIC.getCode());
        return fillingManager.blogCategoryToDTO(category, icon, count);
    }

}
