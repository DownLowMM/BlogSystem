package com.duan.blogos.service.manager;

import com.duan.blogos.service.dao.BlogCategoryRelaDao;
import com.duan.blogos.service.dao.BlogLabelRelaDao;
import com.duan.blogos.service.dao.blog.BlogCategoryDao;
import com.duan.blogos.service.dao.blog.BlogDao;
import com.duan.blogos.service.dao.blog.BlogLabelDao;
import com.duan.blogos.service.dao.blog.BlogStatisticsDao;
import com.duan.blogos.service.dao.blogger.BloggerAccountDao;
import com.duan.blogos.service.dao.blogger.BloggerPictureDao;
import com.duan.blogos.service.dao.blogger.BloggerProfileDao;
import com.duan.blogos.service.dto.blog.BlogListItemDTO;
import com.duan.blogos.service.dto.blogger.BloggerDTO;
import com.duan.blogos.service.dto.blogger.FavoriteBlogListItemDTO;
import com.duan.blogos.service.entity.BlogCategoryRela;
import com.duan.blogos.service.entity.BlogLabelRela;
import com.duan.blogos.service.entity.blog.Blog;
import com.duan.blogos.service.entity.blog.BlogCategory;
import com.duan.blogos.service.entity.blog.BlogLabel;
import com.duan.blogos.service.entity.blog.BlogStatistics;
import com.duan.blogos.service.entity.blogger.BloggerAccount;
import com.duan.blogos.service.entity.blogger.BloggerPicture;
import com.duan.blogos.service.entity.blogger.BloggerProfile;
import com.duan.blogos.service.enums.BloggerPictureCategoryEnum;
import com.duan.blogos.service.util.DataConverter;
import com.duan.common.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2018/9/26.
 *
 * @author DuanJiaNing
 */
@Component
public class BlogDataManager {

    @Autowired
    private BlogCategoryRelaDao categoryRelaDao;

    @Autowired
    private BlogLabelRelaDao labelRelaDao;

    @Autowired
    private BlogCategoryDao categoryDao;

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private BlogLabelDao labelDao;

    @Autowired
    private BloggerAccountDao accountDao;

    @Autowired
    private BloggerProfileDao profileDao;

    @Autowired
    private BloggerPictureDao pictureDao;

    @Autowired
    private StringConstructorManager constructorManager;

    @Autowired
    private BlogStatisticsDao statisticsDao;

    public BlogListItemDTO getBlogListItemDTO(Blog blog, boolean findImg) {
        Long blogId = blog.getId();

        // 找一张图片
        String img = null;
        if (findImg) {
            String content = blog.getContent();
            Pattern pattern = Pattern.compile("<img src=\"(.*)\" .*>");
            Matcher matcher = pattern.matcher(content);
            if (matcher.find())
                img = matcher.group(1);
        }

        BlogCategory[] array = null;
        List<BlogCategoryRela> cts = categoryRelaDao.listAllByBlogId(blogId);
        if (!CollectionUtils.isEmpty(cts)) {
            array = cts.stream()
                    .map(rela -> categoryDao.getCategory(rela.getCategoryId()))
                    .toArray(BlogCategory[]::new);
        }

        BlogLabel[] a2 = null;
        List<BlogLabelRela> lbs = labelRelaDao.listAllByBlogId(blogId);
        if (!CollectionUtils.isEmpty(lbs)) {
            a2 = lbs.stream()
                    .map(rela -> labelDao.getLabel(rela.getLabelId()))
                    .toArray(BlogLabel[]::new);
        }

        BlogStatistics statistics = statisticsDao.getStatistics(blogId);
        return DataConverter.PO2DTO.blogListItemToDTO(statistics, array, a2, blog, img);
    }

    public FavoriteBlogListItemDTO getFavouriteBlogListItemDTO(Long bloggerId, Long blogId, Long id, Timestamp date,
                                                               String reason) {

        // BlogListItemDTO
        Blog blog = blogDao.getBlogById(blogId);
        BlogListItemDTO listItemDTO = getBlogListItemDTO(blog, false);

        // BloggerDTO
        Long authorId = blog.getBloggerId();
        BloggerAccount account = accountDao.getAccountById(authorId);
        BloggerProfile profile = profileDao.getProfileByBloggerId(authorId);
        BloggerPicture avatar = null;
        if (profile != null) {
            avatar = profile.getAvatarId() == null ? null :
                    pictureDao.getPictureById(profile.getAvatarId());
        }

        // 使使用默认的博主头像
        if (avatar == null) {
            avatar = new BloggerPicture();
            avatar.setCategory(BloggerPictureCategoryEnum.PUBLIC.getCode());
            avatar.setBloggerId(authorId);
            avatar.setId(null);
        }

        String url = constructorManager.constructPictureUrl(avatar, BloggerPictureCategoryEnum.DEFAULT_BLOGGER_AVATAR);
        avatar.setPath(url);

        BloggerDTO bloggerDTO = DataConverter.PO2DTO.bloggerAccountToDTO(account, profile, avatar);

        // 结果
        return DataConverter.PO2DTO.favoriteBlogListItemToDTO(bloggerId, id, date, reason,
                listItemDTO, bloggerDTO);
    }

}
