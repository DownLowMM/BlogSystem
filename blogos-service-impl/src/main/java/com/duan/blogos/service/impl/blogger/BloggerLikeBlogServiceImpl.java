package com.duan.blogos.service.impl.blogger;

import com.alibaba.dubbo.config.annotation.Service;
import com.duan.blogos.service.common.BlogSortRule;
import com.duan.blogos.service.config.preference.DefaultProperties;
import com.duan.blogos.service.dao.blog.BlogLikeDao;
import com.duan.blogos.service.dto.blogger.FavoriteBlogListItemDTO;
import com.duan.blogos.service.entity.blog.BlogLike;
import com.duan.blogos.service.manager.BlogDataManager;
import com.duan.blogos.service.restful.PageResult;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerLikeBlogService;
import com.duan.blogos.service.util.ResultModelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

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
    private DefaultProperties defaultProperties;

    @Autowired
    private BlogDataManager blogDataManager;

    @Override
    public boolean getLikeState(Long bloggerId, Long blogId) {
        BlogLike like = likeDao.getLike(bloggerId, blogId);
        return like != null;
    }

    @Override
    public ResultModel<PageResult<FavoriteBlogListItemDTO>> listLikeBlog(Long bloggerId, Integer pageNum, Integer pageSize,
                                                                         BlogSortRule sortRule) {

        pageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
        pageSize = pageSize == null || pageSize < 1 ? defaultProperties.getCollectCount() : pageSize;

        PageHelper.startPage(pageNum, pageSize);
        PageInfo<BlogLike> pageInfo = new PageInfo<>(likeDao.listLikeBlog(bloggerId, sortRule));

        List<BlogLike> likes = pageInfo.getList();
        if (CollectionUtils.isEmpty(likes)) return null;

        //构造结果
        List<FavoriteBlogListItemDTO> result = new ArrayList<>();
        for (BlogLike like : likes) {
            Long blogId = like.getBlogId();
            FavoriteBlogListItemDTO dto = blogDataManager.getFavouriteBlogListItemDTO(bloggerId, blogId,
                    like.getId(), like.getLikeDate(), null);

            result.add(dto);
        }

        return ResultModelUtil.pageResult(pageInfo, result);
    }

    @Override
    public int countByBloggerId(Long bloggerId) {
        return likeDao.countLikeByLikerId(bloggerId);
    }
}
