package com.duan.blogos.service.impl.blogger;

import com.duan.blogos.service.common.BlogSortRule;
import com.duan.blogos.service.config.preference.DefaultProperties;
import com.duan.blogos.service.dao.blog.BlogDao;
import com.duan.blogos.service.dao.blog.BlogLikeDao;
import com.duan.blogos.service.dao.blog.BlogStatisticsDao;
import com.duan.blogos.service.dao.blogger.BloggerAccountDao;
import com.duan.blogos.service.dao.blogger.BloggerPictureDao;
import com.duan.blogos.service.dao.blogger.BloggerProfileDao;
import com.duan.blogos.service.dto.blog.BlogListItemDTO;
import com.duan.blogos.service.dto.blogger.BloggerDTO;
import com.duan.blogos.service.dto.blogger.FavouriteBlogListItemDTO;
import com.duan.blogos.service.entity.blog.Blog;
import com.duan.blogos.service.entity.blog.BlogLike;
import com.duan.blogos.service.entity.blog.BlogStatistics;
import com.duan.blogos.service.entity.blogger.BloggerAccount;
import com.duan.blogos.service.entity.blogger.BloggerPicture;
import com.duan.blogos.service.entity.blogger.BloggerProfile;
import com.duan.blogos.service.enums.BloggerPictureCategoryEnum;
import com.duan.blogos.service.manager.BlogDataManager;
import com.duan.blogos.service.manager.DataFillingManager;
import com.duan.blogos.service.manager.StringConstructorManager;
import com.duan.blogos.service.manager.comparator.BlogListItemComparatorFactory;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerLikeBlogService;
import com.duan.common.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2018/3/11.
 *
 * @author DuanJiaNing
 */
@Service
public class BloggerLikeBlogServiceImpl implements BloggerLikeBlogService {

    @Autowired
    private BlogLikeDao likeDao;

    @Autowired
    private BlogStatisticsDao statisticsDao;

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
    private DefaultProperties defaultProperties;

    @Autowired
    private StringConstructorManager constructorManager;

    @Autowired
    private BlogDataManager blogDataManager;

    @Override
    public boolean getLikeState(Long bloggerId, Long blogId) {
        BlogLike like = likeDao.getLike(bloggerId, blogId);
        return like != null;
    }

    @Override
    public ResultModel<List<FavouriteBlogListItemDTO>> listLikeBlog(Long bloggerId, int offset, int rows, BlogSortRule sortRule) {

        offset = offset < 0 ? 0 : offset;
        rows = rows < 0 ? defaultProperties.getCollectCount() : rows;

        List<BlogLike> likes = likeDao.listLikeBlog(bloggerId, offset, rows);
        if (CollectionUtils.isEmpty(likes)) return null;

        //排序
        List<BlogStatistics> temp = new ArrayList<>();
        //方便排序后的重组
        Map<Long, BlogLike> blogLikeMap = new HashMap<>();
        for (BlogLike like : likes) {
            Long blogId = like.getBlogId();
            BlogStatistics statistics = statisticsDao.getStatistics(blogId);
            temp.add(statistics);
            blogLikeMap.put(blogId, like);
        }
        BlogListItemComparatorFactory factory = new BlogListItemComparatorFactory();
        temp.sort(factory.get(sortRule.getRule(), sortRule.getOrder()));

        //构造结果
        List<FavouriteBlogListItemDTO> result = new ArrayList<>();
        for (BlogStatistics statistics : temp) {
            Long blogId = statistics.getBlogId();

            // BlogListItemDTO
            Blog blog = blogDao.getBlogById(blogId);
            BlogListItemDTO listItemDTO = blogDataManager.getBlogListItemDTO(blog, false);

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
            BlogLike like = blogLikeMap.get(blogId);
            FavouriteBlogListItemDTO dto = fillingManager.likeBlogListItemToDTO(bloggerId, like, listItemDTO, bloggerDTO);
            result.add(dto);
        }

        return new ResultModel<>(result);
    }

    @Override
    public int countByBloggerId(Long bloggerId) {
        return likeDao.countLikeByLikerId(bloggerId);
    }
}
