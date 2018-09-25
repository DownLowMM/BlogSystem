package com.duan.blogos.service.impl.blogger;

import com.duan.blogos.service.common.BlogSortRule;
import com.duan.blogos.service.config.preference.DbProperties;
import com.duan.blogos.service.config.preference.DefaultProperties;
import com.duan.blogos.service.dao.BlogCategoryRelaDao;
import com.duan.blogos.service.dao.BlogLabelRelaDao;
import com.duan.blogos.service.dao.blog.*;
import com.duan.blogos.service.dao.blogger.BloggerAccountDao;
import com.duan.blogos.service.dao.blogger.BloggerPictureDao;
import com.duan.blogos.service.dao.blogger.BloggerProfileDao;
import com.duan.blogos.service.dto.blog.BlogListItemDTO;
import com.duan.blogos.service.dto.blogger.BloggerDTO;
import com.duan.blogos.service.dto.blogger.FavouriteBlogListItemDTO;
import com.duan.blogos.service.entity.blog.*;
import com.duan.blogos.service.entity.blogger.BloggerAccount;
import com.duan.blogos.service.entity.blogger.BloggerPicture;
import com.duan.blogos.service.entity.blogger.BloggerProfile;
import com.duan.blogos.service.enums.BloggerPictureCategoryEnum;
import com.duan.blogos.service.manager.DataFillingManager;
import com.duan.blogos.service.manager.StringConstructorManager;
import com.duan.blogos.service.manager.comparator.BlogListItemComparatorFactory;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerCollectBlogService;
import com.duan.common.util.CollectionUtils;
import com.duan.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2017/12/19.
 *
 * @author DuanJiaNing
 */
@Service
public class BloggerCollectBlogServiceImpl implements BloggerCollectBlogService {

    @Autowired
    private BlogCollectDao collectDao;

    @Autowired
    private BlogStatisticsDao statisticsDao;

    @Autowired
    private BlogCategoryDao categoryDao;

    @Autowired
    private BlogLabelDao labelDao;

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private BloggerAccountDao accountDao;

    @Autowired
    private BloggerProfileDao profileDao;

    @Autowired
    private BloggerPictureDao pictureDao;

    @Autowired
    private DataFillingManager fillingManager;

    @Autowired
    private DbProperties dbProperties;

    @Autowired
    private DefaultProperties defaultProperties;

    @Autowired
    private StringConstructorManager constructorManager;

    @Autowired
    private BlogCategoryRelaDao categoryRelaDao;

    @Autowired
    private BlogLabelRelaDao labelRelaDao;

    @Override
    public ResultModel<List<FavouriteBlogListItemDTO>> listCollectBlog(Long bloggerId, Long categoryId, int offset, int rows, BlogSortRule sortRule) {

        offset = offset < 0 ? 0 : offset;
        rows = rows < 0 ? defaultProperties.getCollectCount() : rows;

        List<BlogCollect> collects = collectDao.listCollectBlog(bloggerId, categoryId, offset, rows);
        if (CollectionUtils.isEmpty(collects)) return null;

        //排序
        List<BlogStatistics> temp = new ArrayList<>();
        //方便排序后的重组
        Map<Long, BlogCollect> blogCollectMap = new HashMap<>();
        for (BlogCollect collect : collects) {
            Long blogId = collect.getBlogId();
            BlogStatistics statistics = statisticsDao.getStatistics(blogId);
            temp.add(statistics);
            blogCollectMap.put(blogId, collect);
        }
        BlogListItemComparatorFactory factory = new BlogListItemComparatorFactory();
        temp.sort(factory.get(sortRule.getRule(), sortRule.getOrder()));

        //构造结果
        List<FavouriteBlogListItemDTO> result = new ArrayList<>();
        for (BlogStatistics statistics : temp) {
            Long blogId = statistics.getBlogId();

            // BlogListItemDTO
            Blog blog = blogDao.getBlogById(blogId);
            String ch = dbProperties.getStringFiledSplitCharacterForNumber();

            // category
            Long[] cids = StringUtils.longStringDistinctToArray(blog.getCategoryIds(), ch);
            List<BlogCategory> categories = null;
            if (!CollectionUtils.isEmpty(cids)) {
                categories = categoryDao.listCategoryById(cids);
            }

            // label
            Long[] lids = StringUtils.longStringDistinctToArray(blog.getLabelIds(), ch);
            List<BlogLabel> labels = null;
            if (!CollectionUtils.isEmpty(lids)) {
                labels = labelDao.listLabelById(lids);
            }

            BlogListItemDTO listItemDTO = fillingManager.blogListItemToDTO(statistics,
                    CollectionUtils.isEmpty(categories) ? null : categories.toArray(new BlogCategory[0]),
                    CollectionUtils.isEmpty(labels) ? null : labels.toArray(new BlogLabel[0]),
                    blog, null);

            // BloggerDTO
            Long authorId = blog.getBloggerId();
            BloggerAccount account = accountDao.getAccountById(authorId);
            BloggerProfile profile = profileDao.getProfileByBloggerId(authorId);
            BloggerPicture avatar = profile.getAvatarId() == null ? null :
                    pictureDao.getPictureById(profile.getAvatarId());

            // 使使用默认的博主头像
            if (avatar == null) {
                avatar = new BloggerPicture();
                avatar.setCategory(BloggerPictureCategoryEnum.PUBLIC.getCode());
                avatar.setBloggerId(authorId);
                avatar.setId(null);
            }

            String url = constructorManager.constructPictureUrl(avatar, BloggerPictureCategoryEnum.DEFAULT_BLOGGER_AVATAR);
            avatar.setPath(url);

            BloggerDTO bloggerDTO = fillingManager.bloggerAccountToDTO(account, profile, avatar);

            // 结果
            BlogCollect collect = blogCollectMap.get(blogId);
            FavouriteBlogListItemDTO dto = fillingManager.collectBlogListItemToDTO(bloggerId, collect, listItemDTO, bloggerDTO);
            result.add(dto);
        }

        return new ResultModel<>(result);
    }

    @Override
    public boolean updateCollect(Long bloggerId, Long blogId, String newReason, Long newCategory) {
        int effect = collectDao.updateByUnique(bloggerId, blogId, newReason, null);
        return effect > 0;
    }

    @Override
    public boolean getCollectState(Long bloggerId, Long blogId) {
        BlogCollect collect = collectDao.getCollect(bloggerId, blogId);
        return collect != null;
    }

    @Override
    public int countByBloggerId(Long bloggerId) {
        return collectDao.countByCollectorId(bloggerId);
    }
}
