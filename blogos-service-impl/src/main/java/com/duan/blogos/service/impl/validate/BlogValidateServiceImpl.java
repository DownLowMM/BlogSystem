package com.duan.blogos.service.impl.validate;

import com.duan.blogos.service.dao.blog.BlogDao;
import com.duan.blogos.service.dao.blog.BlogLabelDao;
import com.duan.blogos.service.dao.blog.BlogStatisticsDao;
import com.duan.blogos.service.entity.blog.Blog;
import com.duan.blogos.service.entity.blog.BlogStatistics;
import com.duan.blogos.service.enums.BlogStatusEnum;
import com.duan.blogos.service.service.blogger.BloggerBlogService;
import com.duan.blogos.service.service.validate.BlogValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created on 2018/9/13.
 *
 * @author DuanJiaNing
 */
@Service
public class BlogValidateServiceImpl implements BlogValidateService {

    @Autowired
    private BloggerBlogService bloggerBlogService;

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private BlogLabelDao labelDao;

    @Autowired
    private BlogStatisticsDao statisticsDao;

    @Override
    public boolean checkBlogExist(int blogId) {
        return blogId > 0 && bloggerBlogService.getBlogForCheckExist(blogId);
    }

    @Override
    public boolean checkLabelsExist(int labelId) {
        return labelDao.getLabel(labelId) != null;
    }

    @Override
    public boolean isCreatorOfBlog(int bloggerId, int blogId) {
        Blog blog = blogDao.getBlogById(blogId);
        return blog != null && blog.getBloggerId().equals(bloggerId);
    }

    @Override
    public boolean verifyBlog(String title, String content, String contentMd, String summary, String keyWords) {
        //TODO 博文内容校验
        return true;
    }

    @Override
    public boolean isBlogStatusAllow(int status) {
        List<BlogStatusEnum> list = Arrays.asList(BlogStatusEnum.PUBLIC, BlogStatusEnum.PRIVATE, BlogStatusEnum.DELETED);
        int contain = 0;
        for (BlogStatusEnum s : list) {
            if (s.getCode() == status) contain++;
        }

        return contain > 0;
    }

    @Override
    public boolean isCreatorOfBlogStatistic(int bloggerId, int blogId) {

        if (!isCreatorOfBlog(bloggerId, blogId)) return false;

        BlogStatistics statistics = statisticsDao.getStatistics(blogId);
        if (statistics == null) return false;

        return true;
    }

    @Override
    public boolean checkBlogStatisticExist(int blogId) {
        Integer count = statisticsDao.getViewCount(blogId);
        if (count == null) return false;

        return true;
    }

}
