package com.duan.blogos.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.duan.blogos.service.BlogFilterService;
import com.duan.blogos.service.common.CodeMessage;
import com.duan.blogos.service.common.dto.blog.BlogListItemDTO;
import com.duan.blogos.service.common.enums.BlogSortRule;
import com.duan.blogos.service.common.enums.BlogStatusEnum;
import com.duan.blogos.service.common.restful.PageResult;
import com.duan.blogos.service.common.restful.ResultModel;
import com.duan.blogos.service.common.util.ExceptionUtil;
import com.duan.blogos.service.common.util.ResultModelUtil;
import com.duan.blogos.service.common.util.Util;
import com.duan.blogos.service.config.preference.DefaultProperties;
import com.duan.blogos.service.dao.BlogCategoryRelaDao;
import com.duan.blogos.service.dao.BlogDao;
import com.duan.blogos.service.dao.BlogLabelRelaDao;
import com.duan.blogos.service.entity.Blog;
import com.duan.blogos.service.entity.BlogCategoryRela;
import com.duan.blogos.service.entity.BlogLabelRela;
import com.duan.blogos.service.manager.BlogDataManager;
import com.duan.blogos.service.manager.BlogLuceneIndexManager;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * Created on 2018/1/15.
 * 博文检索
 *
 * @author DuanJiaNing
 */
@Service
public class BlogFilterServiceServiceImpl implements BlogFilterService {

    @Autowired
    private DefaultProperties defaultProperties;

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
    public ResultModel<PageResult<BlogListItemDTO>> listFilterAll(
            List<Long> categoryIds, List<Long> labelIds, String keyWord, Long bloggerId,
            Integer pageNum, Integer pageSize,
            BlogSortRule sortRule, BlogStatusEnum status) {

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
    protected ResultModel<PageResult<BlogListItemDTO>> filterByLucene(
            String keyWord, List<Long> categoryIds, List<Long> labelIds, Long bloggerId,
            Integer pageNum, Integer pageSize,
            BlogSortRule sortRule, BlogStatusEnum status) {

        // ------------------------关键字筛选
        Long[] ids;
        try {
            // 搜索结果无法使用类似于sql limit的方式分页，这里一次性将所有结果查询出，后续考虑使用缓存实现分页
            ids = luceneIndexManager.search(keyWord, 10000);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            throw ExceptionUtil.get(CodeMessage.COMMON_UNKNOWN_ERROR, e);
        }
        //关键字为首要条件
        if (Util.isArrayEmpty(ids)) return null;

        Set<Long> blogIds = filterByCategoryAndLabels(categoryIds, labelIds, bloggerId);
        blogIds.addAll(Arrays.asList(ids));
        if (CollectionUtils.isEmpty(blogIds)) return null;

        PageResult<BlogListItemDTO> res = constructResult(blogIds, status, sortRule, pageNum, pageSize);
        return ResultModel.success(res);
    }

    @Override
    public ResultModel<PageResult<BlogListItemDTO>> listFilterByLabelAndCategory(
            List<Long> categoryIds, List<Long> labelIds, Long bloggerId,
            Integer pageNum, Integer pageSize,
            BlogSortRule sortRule, BlogStatusEnum status) {

        Set<Long> blogIds = filterByCategoryAndLabels(categoryIds, labelIds, bloggerId);
        if (CollectionUtils.isEmpty(blogIds)) return null;

        PageResult<BlogListItemDTO> res = constructResult(blogIds, status, sortRule, pageNum, pageSize);
        return ResultModel.success(res);

    }

    private Set<Long> filterByCategoryAndLabels(List<Long> categoryIds, List<Long> labelIds, Long bloggerId) {

        Set<Long> blogIds = new HashSet<>();

        if (CollectionUtils.isEmpty(categoryIds) && CollectionUtils.isEmpty(labelIds)) {
            List<Blog> res = bloggerId != null ? blogDao.listAllIdByBloggerId(bloggerId) : blogDao.listAll();
            res.forEach(blog -> blogIds.add(blog.getId()));
        } else {

            if (!CollectionUtils.isEmpty(categoryIds)) {
                List<BlogCategoryRela> res = bloggerId != null ?
                        categoryRelaDao.listAllByBloggerIdInCategoryIds(bloggerId, categoryIds) :
                        categoryRelaDao.listAllInCategoryIds(categoryIds);
                res.forEach(rela -> blogIds.add(rela.getBlogId()));
            }

            if (!CollectionUtils.isEmpty(labelIds)) {
                List<BlogLabelRela> res = bloggerId != null ?
                        labelRelaDao.listAllByBloggerIdInLabelIds(bloggerId, labelIds) :
                        labelRelaDao.listAllIdInLabelIds(labelIds);
                res.forEach(rela -> blogIds.add(rela.getBlogId()));
            }
        }

        return blogIds;
    }


    // 对筛选出的博文进行排序并重组结果集
    private PageResult<BlogListItemDTO> constructResult(Set<Long> blogIds, BlogStatusEnum status,
                                                        BlogSortRule sortRule, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        PageInfo<Blog> pageInfo = new PageInfo<>(blogDao.listBlogByBlogIds(new ArrayList<>(blogIds), status.getCode(), sortRule));

        List<BlogListItemDTO> dtos = new ArrayList<>();
        for (Blog blog : pageInfo.getList()) {
            BlogListItemDTO dto = blogDataManager.getBlogListItemDTO(blog, true);
            if (dto != null) {
                dto.setTitleBase64(Util.encodeUrlBase64(dto.getTitle()));
                dtos.add(dto);
            }
        }

        return ResultModelUtil.page(pageInfo, dtos);

    }

}
