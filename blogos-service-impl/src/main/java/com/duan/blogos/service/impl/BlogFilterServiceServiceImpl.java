package com.duan.blogos.service.impl;

import com.duan.blogos.service.common.BlogSortRule;
import com.duan.blogos.service.config.preference.DbProperties;
import com.duan.blogos.service.config.preference.DefaultProperties;
import com.duan.blogos.service.dao.BlogCategoryRelaDao;
import com.duan.blogos.service.dao.BlogLabelRelaDao;
import com.duan.blogos.service.dao.blog.BlogDao;
import com.duan.blogos.service.dto.blog.BlogListItemDTO;
import com.duan.blogos.service.entity.BlogCategoryRela;
import com.duan.blogos.service.entity.BlogLabelRela;
import com.duan.blogos.service.entity.blog.Blog;
import com.duan.blogos.service.enums.BlogStatusEnum;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ResultUtil;
import com.duan.blogos.service.manager.BlogDataManager;
import com.duan.blogos.service.manager.BlogLuceneIndexManager;
import com.duan.blogos.service.restful.PageResult;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.BlogFilterService;
import com.duan.common.util.ArrayUtils;
import com.duan.common.util.CollectionUtils;
import com.duan.common.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * Created on 2018/1/15.
 * 博文检索
 *
 * @author DuanJiaNing
 */
@Component
public class BlogFilterServiceServiceImpl implements BlogFilterService {

    @Autowired
    private DefaultProperties defaultProperties;

    @Autowired
    protected DbProperties dbProperties;

    @Autowired
    protected BlogDao blogDao;

    @Autowired
    protected BlogLuceneIndexManager luceneIndexManager;

    @Autowired
    private BlogCategoryRelaDao categoryRelaDao;

    @Autowired
    private BlogLabelRelaDao labelRelaDao;

    @Autowired
    private BlogDataManager blogDataManager;

    @Override
    public ResultModel<PageResult<BlogListItemDTO>> listFilterAll(Long[] categoryIds, Long[] labelIds, String keyWord,
                                                                  Long bloggerId, Integer pageNum, Integer pageSize, BlogSortRule sortRule,
                                                                  BlogStatusEnum status) {

        pageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
        pageSize = pageSize == null || pageSize < 1 ? defaultProperties.getBlogCount() : pageSize;

        if (StringUtils.isEmpty(keyWord)) {
            //标签&类别检索
            return listFilterByLabelAndCategory(categoryIds, labelIds, bloggerId, pageNum, pageSize, sortRule, status);
        } else {
            // 有关键字时需要依赖lucene进行检索
            // UPDATE: 2018/1/10 搜索准确度比较低
            return filterByLucene(keyWord, categoryIds, labelIds, bloggerId, pageNum, pageSize, sortRule, status);
        }

    }

    /**
     * 关键字不为null时需要通过lucene进行全文检索
     *
     * @param keyWord     关键字
     * @param categoryIds 类别id
     * @param labelIds    标签id
     * @param bloggerId   博主id
     * @param sortRule    排序规则
     * @param status      博文状态
     * @return 经过筛选、排序的结果集
     */
    protected ResultModel<PageResult<BlogListItemDTO>> filterByLucene(String keyWord, Long[] categoryIds, Long[] labelIds,
                                                                      Long bloggerId, Integer pageNum, Integer pageSize, BlogSortRule sortRule,
                                                                      BlogStatusEnum status) {

        // ------------------------关键字筛选
        Long[] ids;
        try {
            // 搜索结果无法使用类似于sql limit的方式分页，这里一次性将所有结果查询出，后续考虑使用缓存实现分页
            ids = luceneIndexManager.search(keyWord, 10000);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            throw ResultUtil.failException(CodeMessage.COMMON_UNKNOWN_ERROR, e);
        }
        //关键字为首要条件
        if (CollectionUtils.isEmpty(ids)) return null;

        Set<Long> blogIds = filterByCategoryAndLabels(categoryIds, labelIds, bloggerId);
        blogIds.addAll(Arrays.asList(ids));
        if (CollectionUtils.isEmpty(blogIds)) return null;

        PageResult<BlogListItemDTO> res = constructResult(blogIds, bloggerId, status, sortRule, pageNum, pageSize);
        return new ResultModel<>(res);
    }

    @Override
    public ResultModel<PageResult<BlogListItemDTO>> listFilterByLabelAndCategory(Long[] categoryIds, Long[] labelIds,
                                                                                 Long bloggerId, Integer pageNum, Integer pageSize,
                                                                                 BlogSortRule sortRule, BlogStatusEnum status) {

        Set<Long> blogIds = filterByCategoryAndLabels(categoryIds, labelIds, bloggerId);
        if (CollectionUtils.isEmpty(blogIds)) return null;

        PageResult<BlogListItemDTO> res = constructResult(blogIds, bloggerId, status, sortRule, pageNum, pageSize);
        return new ResultModel<>(res);

    }

    private Set<Long> filterByCategoryAndLabels(Long[] categoryIds, Long[] labelIds, Long bloggerId) {

        Set<Long> blogIds = new HashSet<>();

        if (ArrayUtils.isEmpty(categoryIds) && ArrayUtils.isEmpty(labelIds)) {
            List<Blog> res = blogDao.listAllIdByBloggerId(bloggerId);
            res.forEach(blog -> blogIds.add(blog.getId()));
        } else {

            if (!ArrayUtils.isEmpty(categoryIds)) {
                List<BlogCategoryRela> res = categoryRelaDao.listAllByBloggerIdInCategoryIds(bloggerId, categoryIds);
                res.forEach(rela -> blogIds.add(rela.getBlogId()));
            }

            if (!ArrayUtils.isEmpty(labelIds)) {
                List<BlogLabelRela> res = labelRelaDao.listAllByBloggerIdInLabelIds(bloggerId, labelIds);
                res.forEach(rela -> blogIds.add(rela.getBlogId()));
            }
        }

        return blogIds;
    }


    // 对筛选出的博文进行排序并重组结果集
    private PageResult<BlogListItemDTO> constructResult(Set<Long> blogIds, Long bloggerId, BlogStatusEnum status,
                                                        BlogSortRule sortRule, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        PageInfo<Blog> pageInfo = new PageInfo<>(blogDao.listBlogByBlogIds(new ArrayList<>(blogIds), status.getCode(), sortRule));

        List<BlogListItemDTO> dtos = new ArrayList<>();
        for (Blog blog : pageInfo.getList()) {
            dtos.add(blogDataManager.getBlogListItemDTO(blog, bloggerId, true));
        }

        return new PageResult<>(pageInfo.getTotal(), dtos);

    }

}
