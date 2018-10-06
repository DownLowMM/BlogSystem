package com.duan.blogos.service.impl.blogger;

import com.duan.blogos.service.dao.blog.*;
import com.duan.blogos.service.dao.blogger.BloggerAccountDao;
import com.duan.blogos.service.dao.blogger.BloggerLinkDao;
import com.duan.blogos.service.dao.blogger.BloggerPictureDao;
import com.duan.blogos.service.dao.blogger.BloggerProfileDao;
import com.duan.blogos.service.dto.blogger.BloggerDTO;
import com.duan.blogos.service.dto.blogger.BloggerStatisticsDTO;
import com.duan.blogos.service.entity.blog.BlogStatistics;
import com.duan.blogos.service.entity.blogger.BloggerAccount;
import com.duan.blogos.service.entity.blogger.BloggerPicture;
import com.duan.blogos.service.entity.blogger.BloggerProfile;
import com.duan.blogos.service.enums.BlogStatusEnum;
import com.duan.blogos.service.enums.BloggerPictureCategoryEnum;
import com.duan.blogos.service.manager.StringConstructorManager;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerStatisticsService;
import com.duan.blogos.service.util.DataConverter;
import com.duan.common.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2018/1/17.
 *
 * @author DuanJiaNing
 */
@Service
public class BloggerStatisticsServiceImpl implements BloggerStatisticsService {

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private BlogLikeDao likeDao;

    @Autowired
    private BloggerLinkDao linkDao;

    @Autowired
    private BlogCategoryDao categoryDao;

    @Autowired
    private BlogLabelDao labelDao;

    @Autowired
    private BlogCollectDao collectDao;

    @Autowired
    private BlogStatisticsDao statisticsDao;

    @Autowired
    private StringConstructorManager stringConstructorManager;

    @Autowired
    private BloggerAccountDao accountDao;

    @Autowired
    private BloggerPictureDao pictureDao;

    @Autowired
    private BloggerProfileDao profileDao;

    @Override
    public ResultModel<BloggerStatisticsDTO> getBloggerStatistics(Long bloggerId) {


        List<BlogStatistics> statistics = statisticsDao.listByBloggerId(bloggerId);
        if (CollectionUtils.isEmpty(statistics)) {
            return null;
        }

        int wordCountSum = statistics.stream().mapToInt(BlogStatistics::getWordCount).sum();
        int likeCount = statistics.stream().mapToInt(BlogStatistics::getLikeCount).sum();
        int collectedCount = statistics.stream().mapToInt(BlogStatistics::getCollectCount).sum();

        int blogCount = blogDao.countBlogByBloggerId(bloggerId, BlogStatusEnum.PUBLIC.getCode());
        int likeGiveCount = likeDao.countLikeByLikerId(bloggerId);
        int categoryCount = categoryDao.countByBloggerId(bloggerId);
        int labelCount = labelDao.countByBloggerId(bloggerId);
        int collectCount = collectDao.countByCollectorId(bloggerId);
        int linkCount = linkDao.countLinkByBloggerId(bloggerId);

        BloggerStatisticsDTO dto = DataConverter.PO2DTO.bloggerStatisticToDTO(blogCount, wordCountSum, likeCount,
                likeGiveCount, categoryCount, labelCount, collectCount, collectedCount, linkCount);

        return ResultModel.success(dto);
    }

    // 获得博主dto
    @Override
    public List<BloggerDTO> listBloggerDTO(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) return null;

        List<BloggerDTO> dtos = new ArrayList<>();
        for (Long id : ids) {
            BloggerAccount account = accountDao.getAccountById(id);
            BloggerProfile profile = profileDao.getProfileByBloggerId(id);
            BloggerPicture avatar = null;
            Long avatarId = profile.getAvatarId();
            if (avatarId != null)
                avatar = pictureDao.getPictureById(avatarId);

            if (avatar != null)
                avatar.setPath(stringConstructorManager.constructPictureUrl(avatar, BloggerPictureCategoryEnum.DEFAULT_BLOGGER_AVATAR));

            // 设置默认头像
            if (avatar == null) {
                avatar = new BloggerPicture();
                avatar.setBloggerId(id);
                avatar.setCategory(BloggerPictureCategoryEnum.PUBLIC.getCode());
                avatar.setId(null);
                avatar.setPath(stringConstructorManager.constructPictureUrl(avatar, BloggerPictureCategoryEnum.DEFAULT_BLOGGER_AVATAR));
            }

            BloggerDTO dto = DataConverter.PO2DTO.bloggerAccountToDTO(account, profile, avatar);
            dtos.add(dto);
        }

        return dtos;
    }


}
