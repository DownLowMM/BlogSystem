package com.duan.blogos.service.impl.blogger;

import com.alibaba.dubbo.config.annotation.Service;
import com.duan.blogos.service.common.BlogSortRule;
import com.duan.blogos.service.config.preference.DefaultProperties;
import com.duan.blogos.service.dao.blog.BlogCollectDao;
import com.duan.blogos.service.dto.blogger.FavoriteBlogListItemDTO;
import com.duan.blogos.service.entity.blog.BlogCollect;
import com.duan.blogos.service.manager.BlogDataManager;
import com.duan.blogos.service.restful.PageResult;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerCollectBlogService;
import com.duan.blogos.service.util.ResultModelUtil;
import com.duan.common.util.CollectionUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

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
    private DefaultProperties defaultProperties;

    @Autowired
    private BlogDataManager blogDataManager;

    @Override
    public ResultModel<PageResult<FavoriteBlogListItemDTO>> listCollectBlog(Long bloggerId, Long categoryId,
                                                                            Integer pageNum, Integer pageSize,
                                                                            BlogSortRule sortRule) {

        pageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
        pageSize = pageSize == null || pageSize < 1 ? defaultProperties.getCollectCount() : pageSize;

        PageHelper.startPage(pageNum, pageSize);
        PageInfo<BlogCollect> pageInfo = new PageInfo<>(collectDao.listCollectBlog(bloggerId, categoryId, sortRule));

        List<BlogCollect> collects = pageInfo.getList();
        if (CollectionUtils.isEmpty(collects)) return null;

        //构造结果
        List<FavoriteBlogListItemDTO> result = new ArrayList<>();
        for (BlogCollect collect : collects) {
            Long blogId = collect.getBlogId();
            FavoriteBlogListItemDTO dto = blogDataManager.getFavouriteBlogListItemDTO(bloggerId, blogId,
                    collect.getId(), collect.getCollectDate(), collect.getReason());

            result.add(dto);
        }

        return ResultModelUtil.pageResult(pageInfo, result);
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
