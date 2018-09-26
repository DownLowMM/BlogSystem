package com.duan.blogos.service.manager;

import com.duan.blogos.service.dao.BlogCategoryRelaDao;
import com.duan.blogos.service.dao.BlogLabelRelaDao;
import com.duan.blogos.service.dao.blog.BlogCategoryDao;
import com.duan.blogos.service.dao.blog.BlogLabelDao;
import com.duan.blogos.service.dao.blog.BlogStatisticsDao;
import com.duan.blogos.service.dto.blog.BlogListItemDTO;
import com.duan.blogos.service.entity.BlogCategoryRela;
import com.duan.blogos.service.entity.BlogLabelRela;
import com.duan.blogos.service.entity.blog.Blog;
import com.duan.blogos.service.entity.blog.BlogCategory;
import com.duan.blogos.service.entity.blog.BlogLabel;
import com.duan.blogos.service.entity.blog.BlogStatistics;
import com.duan.common.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private DataFillingManager dataFillingManager;

    @Autowired
    private BlogCategoryRelaDao categoryRelaDao;

    @Autowired
    private BlogLabelRelaDao labelRelaDao;

    @Autowired
    private BlogCategoryDao categoryDao;

    @Autowired
    private BlogLabelDao labelDao;

    @Autowired
    private BlogStatisticsDao statisticsDao;

    public BlogListItemDTO getBlogListItemDTO(Blog blog, Long bloggerId, boolean findImg) {
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
                    .map(rela -> categoryDao.getCategory(bloggerId, rela.getCategoryId()))
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
        return dataFillingManager.blogListItemToDTO(statistics, array, a2, blog, img);
    }


}
