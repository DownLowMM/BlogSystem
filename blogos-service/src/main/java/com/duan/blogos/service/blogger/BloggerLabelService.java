package com.duan.blogos.service.blogger;


import com.duan.blogos.service.common.dto.blog.BlogLabelDTO;
import com.duan.blogos.service.common.restful.PageResult;
import com.duan.blogos.service.common.restful.ResultModel;

/**
 * Created on 2017/12/18.
 * 标签服务
 *
 * @author DuanJiaNing
 */
public interface BloggerLabelService {

    /**
     * 新增标签
     *
     * @param bloggerId 标签创建者（博主）id
     * @param title     标签名
     * @return 新纪录id
     */
    Long insertLabel(Long bloggerId, String title);

    /**
     * 修改标签
     *
     * @param labelId   标签id
     * @param bloggerId 标签所有者
     * @param newTitle  新标签名
     * @return 更新失败为false
     */
    boolean updateLabel(Long labelId, Long bloggerId, String newTitle);

    /**
     * 删除标签，只有标签是当前博主创建时才能删除
     *
     * @param bloggerId 博主id
     * @param labelId   标签id
     * @return 删除成功返回true
     */
    boolean deleteLabel(Long bloggerId, Long labelId);

    /**
     * 获得博主创建的所有标签
     *
     * @return 查询结果
     */
    ResultModel<PageResult<BlogLabelDTO>> listLabel(Integer pageNum, Integer pageSize);

    /**
     * 获得指定标签
     *
     * @param labelId 标签id
     * @return 查询结果
     */
    BlogLabelDTO getLabel(Long labelId);

    /**
     * 获取指定博主创建的所有标签
     *
     * @param bloggerId 博主id
     * @return 查询结果
     */
    ResultModel<PageResult<BlogLabelDTO>> listLabelByBlogger(Long bloggerId, Integer pageNum, Integer pageSize);
}
