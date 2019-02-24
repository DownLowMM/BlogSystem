package com.duan.blogos.service.impl.common;

import com.alibaba.dubbo.config.annotation.Service;
import com.duan.blogos.service.config.preference.DbProperties;
import com.duan.blogos.service.dao.*;
import com.duan.blogos.service.common.dto.blog.BlogBaseStatisticsDTO;
import com.duan.blogos.service.common.dto.blog.BlogStatisticsDTO;
import com.duan.blogos.service.common.dto.blogger.BloggerDTO;
import com.duan.blogos.service.entity.*;
import com.duan.blogos.service.common.restful.ResultModel;
import com.duan.blogos.service.blog.StatisticsService;
import com.duan.blogos.service.blogger.BloggerStatisticsService;
import com.duan.blogos.service.common.util.DataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created on 2017/12/19.
 *
 * @author DuanJiaNing
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private DbProperties dbProperties;

    @Autowired
    private BlogCategoryDao categoryDao;

    @Autowired
    private BlogLabelDao labelDao;

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private BlogStatisticsDao statisticsDao;

    @Autowired
    private BlogLikeDao likeDao;

    @Autowired
    private BlogCollectDao collectDao;

    @Autowired
    private BlogCommentDao commentDao;

    @Autowired
    private BlogCategoryRelaDao categoryRelaDao;

    @Autowired
    private BlogLabelRelaDao labelRelaDao;

    @Autowired
    private BloggerStatisticsService statisticsService;

    @Override
    public ResultModel<BlogStatisticsDTO> getBlogStatistics(Long blogId) {

        Blog blog = blogDao.getBlogById(blogId);
        if (blog == null) return null;


        // 统计信息
        BlogStatistics statistics = statisticsDao.getStatistics(blogId);
        if (statistics == null) return null;

        // 喜欢该篇文章的人
        List<BloggerDTO> likes = null;
        List<BlogLike> likeList = likeDao.listAllLikeByBlogId(blogId);
        if (!CollectionUtils.isEmpty(likeList)) {
            List<Long> ids = likeList.stream()
                    .map(BlogLike::getLikerId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());
            likes = statisticsService.listBloggerDTO(ids);
        }

        // 收藏了该篇文章的人
        List<BloggerDTO> collects = null;
        List<BlogCollect> collectList = collectDao.listAllCollectByBlogId(blogId);
        if (!CollectionUtils.isEmpty(collectList)) {
            List<Long> ids = collectList.stream()
                    .map(BlogCollect::getCollectorId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());
            collects = statisticsService.listBloggerDTO(ids);
        }

        // 评论过该篇文章的人
        List<BloggerDTO> commenter = null;
        List<BlogComment> commentList = commentDao.listAllCommentByBlogId(blogId);
        if (!CollectionUtils.isEmpty(commentList)) {
            List<Long> ls = commentList.stream()
                    .map(BlogComment::getSpokesmanId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());
            commenter = statisticsService.listBloggerDTO(ls);
        }

        // 类别
        List<BlogCategory> categories = null;
        List<BlogCategoryRela> relas = categoryRelaDao.listAllByBlogId(blogId);
        if (!CollectionUtils.isEmpty(relas)) {
            List<Long> cids = relas.stream()
                    .map(BlogCategoryRela::getCategoryId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());
            categories = categoryDao.listCategoryById(cids);
        }

        // 标签
        List<BlogLabel> labels = null;
        List<BlogLabelRela> lrelas = labelRelaDao.listAllByBlogId(blogId);
        if (!CollectionUtils.isEmpty(lrelas)) {
            List<Long> lids = lrelas.stream()
                    .map(BlogLabelRela::getLabelId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());
            labels = labelDao.listLabelById(lids);
        }

        BlogStatisticsDTO dto = DataConverter.PO2DTO.blogStatisticsToDTO(blog, statistics, categories, labels,
                likes, collects, commenter, dbProperties.getStringFiledSplitCharacterForString());

        return ResultModel.success(dto);
    }


    @Override
    public ResultModel<BlogBaseStatisticsDTO> getBlogStatisticsCount(Long blogId) {

        BlogStatistics statistics = statisticsDao.getStatistics(blogId);
        if (statistics == null) return null;

        BlogBaseStatisticsDTO dto = DataConverter.PO2DTO.blogStatisticsCountToDTO(statistics);
        return ResultModel.success(dto);
    }

}
