package com.duan.blogos.service.impl.blog;

import com.alibaba.dubbo.config.annotation.Service;
import com.duan.blogos.service.dao.blog.BlogCollectDao;
import com.duan.blogos.service.dao.blog.BlogComplainDao;
import com.duan.blogos.service.dao.blog.BlogLikeDao;
import com.duan.blogos.service.dao.blog.BlogStatisticsDao;
import com.duan.blogos.service.entity.blog.BlogCollect;
import com.duan.blogos.service.entity.blog.BlogComplain;
import com.duan.blogos.service.entity.blog.BlogLike;
import com.duan.blogos.service.service.blog.OperateService;
import com.duan.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created on 2017/12/26.
 *
 * @author DuanJiaNing
 */
@Service
public class OperateServiceImpl implements OperateService {

    @Autowired
    private BlogStatisticsDao statisticsDao;

    @Autowired
    private BlogCollectDao collectDao;

    @Autowired
    private BlogLikeDao likeDao;

    @Autowired
    private BlogComplainDao complainDao;

    @Override
    public Integer insertShare(Long blogId, Long sharerId) {

        statisticsDao.updateShareCountPlus(blogId);
        Integer count = statisticsDao.getShareCount(blogId);

        return count;
    }

    @Override
    public Long insertCollect(Long blogId, Long collectorId, String reason, Long categoryId) {

        BlogCollect collect = new BlogCollect();
        collect.setBlogId(blogId);
        collect.setCategoryId(categoryId);
        collect.setCollectorId(collectorId);
        collect.setReason(StringUtils.isEmpty(reason) ? null : reason);
        collectDao.insert(collect);

        //博文收藏次数加一
        statisticsDao.updateCollectCountPlus(blogId);

        return collect.getId();
    }

    @Override
    public Integer insertLike(Long blogId, Long likerId) {

        BlogLike like = new BlogLike();
        like.setBlogId(blogId);
        like.setLikerId(likerId);
        likeDao.insert(like);

        //博文喜欢次数加一
        statisticsDao.updateLikeCountPlus(blogId);

        Integer count = statisticsDao.getLikeCount(blogId);
        return count == null ? -1 : count;
    }

    @Override
    public Long insertComplain(Long blogId, Long complainId, String content) {

        BlogComplain complain = new BlogComplain();
        complain.setBlogId(blogId);
        complain.setComplainerId(complainId);
        complain.setContent(content);
        complainDao.insert(complain);

        //博文投诉次数加一
        statisticsDao.updateComplainCountPlus(blogId);

        return complain.getId();
    }

    @Override
    public boolean deleteCollect(Long bloggerId, Long blogId) {
        int effect = collectDao.deleteCollectByBloggerId(bloggerId, blogId);
        if (effect <= 0) return false;

        //博文收藏数减一
        statisticsDao.updateCollectCountMinus(blogId);

        return true;
    }

    @Override
    public boolean deleteLike(Long likerId, Long blogId) {
        int effect = likeDao.deleteLikeByBloggerId(likerId, blogId);
        if (effect <= 0) return false;

        //博文喜欢数减一
        statisticsDao.updateLikeCountMinus(blogId);

        return true;
    }

    @Override
    public void updateBlogViewCountPlus(Long blogId) {
        statisticsDao.updateViewCountPlus(blogId);
    }


}
