package com.duan.blogos.service.service;


import com.duan.blogos.service.common.BlogSortRule;
import com.duan.blogos.service.enums.BlogStatusEnum;

/**
 * Created on 2017/12/19.
 *
 * @author DuanJiaNing
 */
public interface BlogFilter<T> {

    /**
     * 全限定检索（包括关键字）
     *
     * @param categoryIds 限定在博主的哪些类别之下，不做限定时传null
     * @param labelIds    限定在博主的哪些标签之下，不做限定时传null
     * @param keyWord     关键字,不做限定时传null
     * @param bloggerId   博主id
     * @param sortRule    排序规则，为null则不做约束
     * @param status      博文状态
     * @return 查询结果
     */
    T listFilterAll(Long[] categoryIds, Long[] labelIds, String keyWord, Long bloggerId, Integer pageNum, Integer pageSize,
                    BlogSortRule sortRule, BlogStatusEnum status);

    /**
     * 标签&类别检索（无关键字）
     *
     * @param labelIds    限定在博主的哪些标签之下
     * @param categoryIds 限定在博主的哪些类别之下
     * @param bloggerId   博主id
     * @param sortRule    排序规则，为null则不做约束
     * @param status      博文状态
     * @return 查询结果
     */
    T listFilterByLabelAndCategory(Long[] categoryIds, Long[] labelIds, Long bloggerId, Integer pageNum, Integer pageSize,
                                   BlogSortRule sortRule, BlogStatusEnum status);

}
