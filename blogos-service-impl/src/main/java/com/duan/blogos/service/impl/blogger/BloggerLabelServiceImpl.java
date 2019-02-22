package com.duan.blogos.service.impl.blogger;

import com.alibaba.dubbo.config.annotation.Service;
import com.duan.blogos.service.config.preference.DefaultProperties;
import com.duan.blogos.service.dao.blog.BlogLabelDao;
import com.duan.blogos.service.dto.blog.BlogLabelDTO;
import com.duan.blogos.service.entity.blog.BlogLabel;
import com.duan.blogos.service.restful.PageResult;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerLabelService;
import com.duan.blogos.service.util.DataConverter;
import com.duan.blogos.service.util.ResultModelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2017/12/19.
 *
 * @author DuanJiaNing
 */
@Service
public class BloggerLabelServiceImpl implements BloggerLabelService {

    @Autowired
    private BlogLabelDao labelDao;

    @Autowired
    private DefaultProperties defaultProperties;

    @Override
    public Long insertLabel(Long bloggerId, String title) {

        BlogLabel label = new BlogLabel();
        label.setBloggerId(bloggerId);
        label.setTitle(title);
        int effect = labelDao.insert(label);
        if (effect <= 0) return null;

        return label.getId();
    }

    @Override
    public boolean updateLabel(Long labelId, Long bloggerId, String newTitle) {

        //检查标签存在及标签创建者是否为当前博主
        BlogLabel label = labelDao.getLabel(labelId);
        if (label == null || !label.getBloggerId().equals(bloggerId)) return false;

        BlogLabel la = new BlogLabel();
        la.setTitle(newTitle);
        la.setId(labelId);
        int effect = labelDao.update(la);
        if (effect <= 0) return false;

        return true;
    }

    @Override
    public boolean deleteLabel(Long bloggerId, Long labelId) {

        //检查标签存在及标签创建者是否为当前博主
        BlogLabel label = labelDao.getLabel(labelId);
        if (label == null || !label.getBloggerId().equals(bloggerId)) return false;

        // 删除数据库记录 外键会将 BlogLabelRelaDao 中记录删除
        int effect = labelDao.delete(labelId);
        if (effect <= 0) return false;

        // 将所有拥有该标签的博文修改（j将标签移除）
        /*List<Blog> blogs = blogDao.listAllLabelByBloggerId(bloggerId);
        String ch = dbProperties.getStringFiledSplitCharacterForNumber();
        for (Blog blog : blogs) {
            Long[] lids = StringUtils.longStringDistinctToArray(blog.getLabelIds(), ch);
            if (CollectionUtils.isEmpty(lids)) continue;

            if (CollectionUtils.longArrayContain(lids, labelId)) {
                Long[] ids = ArrayUtils.removeFromArray(lids, labelId);
                blog.setLabelIds(StringUtils.longArrayToString(ids, ch));
                if (blogDao.update(blog) <= 0)
                    throw ResultUtil.get(CodeMessage.COMMON_UNKNOWN_ERROR, new SQLException());
            }
        }*/

        return true;
    }

    @Override
    public ResultModel<PageResult<BlogLabelDTO>> listLabel(Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
        pageSize = pageSize == null || pageSize < 1 ? 10 : pageSize;

        PageHelper.startPage(pageNum, pageSize);
        PageInfo<BlogLabel> pageInfo = new PageInfo<>(labelDao.listLabel());
        List<BlogLabel> result = pageInfo.getList();
        if (CollectionUtils.isEmpty(result)) return null;

        List<BlogLabelDTO> dtos = result.stream().map(DataConverter.PO2DTO::blogLabel2DTO).collect(Collectors.toList());
        return ResultModelUtil.pageResult(pageInfo, dtos);
    }

    @Override
    public BlogLabelDTO getLabel(Long labelId) {
        BlogLabel label = labelDao.getLabel(labelId);
        return DataConverter.PO2DTO.blogLabel2DTO(label);
    }

    @Override
    public ResultModel<PageResult<BlogLabelDTO>> listLabelByBlogger(Long bloggerId, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
        pageSize = pageSize == null || pageSize < 1 ? defaultProperties.getLabelCount() : pageSize;

        PageHelper.startPage(pageNum, pageSize);
        PageInfo<BlogLabel> pageInfo = new PageInfo<>(labelDao.listLabelByBloggerId(bloggerId));

        List<BlogLabel> result = pageInfo.getList();
        if (CollectionUtils.isEmpty(result)) return null;

        List<BlogLabelDTO> dtos = result.stream().map(DataConverter.PO2DTO::blogLabel2DTO).collect(Collectors.toList());

        return ResultModelUtil.pageResult(pageInfo, dtos);

    }
}
