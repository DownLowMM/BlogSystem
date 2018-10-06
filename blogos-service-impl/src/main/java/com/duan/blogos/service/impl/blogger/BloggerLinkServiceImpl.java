package com.duan.blogos.service.impl.blogger;

import com.duan.blogos.service.config.preference.DefaultProperties;
import com.duan.blogos.service.config.preference.WebsiteProperties;
import com.duan.blogos.service.dao.blogger.BloggerLinkDao;
import com.duan.blogos.service.dao.blogger.BloggerPictureDao;
import com.duan.blogos.service.dto.blogger.BloggerLinkDTO;
import com.duan.blogos.service.entity.blogger.BloggerLink;
import com.duan.blogos.service.entity.blogger.BloggerPicture;
import com.duan.blogos.service.manager.ImageManager;
import com.duan.blogos.service.manager.StringConstructorManager;
import com.duan.blogos.service.restful.PageResult;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerLinkService;
import com.duan.blogos.service.util.DataConverter;
import com.duan.blogos.service.util.ResultModelUtil;
import com.duan.common.util.CollectionUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.duan.blogos.service.enums.BloggerPictureCategoryEnum.DEFAULT_BLOGGER_LINK_ICON;


/**
 * Created on 2017/12/19.
 *
 * @author DuanJiaNing
 */
@Service
public class BloggerLinkServiceImpl implements BloggerLinkService {

    @Autowired
    private BloggerLinkDao linkDao;

    @Autowired
    private BloggerPictureDao pictureDao;

    @Autowired
    private WebsiteProperties websiteProperties;

    @Autowired
    private DefaultProperties defaultProperties;

    @Autowired
    private StringConstructorManager constructorManager;

    @Autowired
    private ImageManager imageManager;

    @Override
    public ResultModel<PageResult<BloggerLinkDTO>> listBloggerLink(Long bloggerId, Integer pageNum, Integer pageSize) {

        pageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
        pageSize = pageSize == null || pageSize < 1 ? defaultProperties.getLinkCount() : pageSize;

        PageHelper.startPage(pageNum, pageSize);
        PageInfo<BloggerLink> pageInfo = new PageInfo<>(linkDao.listBlogLinkByBloggerId(bloggerId));
        List<BloggerLink> list = pageInfo.getList();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        List<BloggerLinkDTO> result = new ArrayList<>();
        for (BloggerLink link : list) {
            Long iconId = link.getIconId();
            BloggerPicture icon = iconId == null ?
                    pictureDao.getBloggerUniquePicture(websiteProperties.getManagerId(),
                            DEFAULT_BLOGGER_LINK_ICON.getCode()) :
                    pictureDao.getPictureById(iconId);

            // 默认的链接图标可能没有上传
            if (icon != null)
                icon.setPath(constructorManager.constructPictureUrl(icon, DEFAULT_BLOGGER_LINK_ICON));

            BloggerLinkDTO dto = DataConverter.PO2DTO.bloggerLinkToDTO(link, icon);
            result.add(dto);
        }

        return ResultModelUtil.pageResult(pageInfo, result);
    }

    @Override
    public Long insertBloggerLink(Long bloggerId, Long iconId, String title, String url, String bewrite) {

        // 新增链接
        BloggerLink link = new BloggerLink();
        link.setBewrite(bewrite);
        link.setBloggerId(bloggerId);
        link.setIconId(iconId);
        link.setTitle(title);
        link.setUrl(url);
        int effect = linkDao.insert(link);
        if (effect < 0) return null;

        // 修改图片可见性，引用次数
        imageManager.imageInsertHandle(bloggerId, iconId);

        return link.getId();
    }

    @Override
    public boolean updateBloggerLink(Long linkId, Long newIconId, String newTitle, String newUrl, String newBewrite) {

        // 修改链接
        BloggerLink link = linkDao.getLink(linkId);
        Long oldIconId = link.getIconId();
        link.setBewrite(newBewrite);
        link.setIconId(newIconId);
        link.setTitle(newTitle);
        link.setUrl(newUrl);
        link.setId(linkId);
        int effect = linkDao.update(link);
        if (effect < 0) return false;

        // 修改图片可见性，引用次数
        imageManager.imageUpdateHandle(link.getBloggerId(), newIconId, oldIconId);

        return true;
    }

    @Override
    public boolean deleteBloggerLink(Long linkId) {
        BloggerLink link = linkDao.getLink(linkId);
        if (link == null) return false;
        if (link.getIconId() != null) pictureDao.updateUseCountMinus(link.getIconId());

        int effect = linkDao.delete(linkId);
        if (effect < 0) return false;
        return true;
    }

    @Override
    public boolean getLinkForCheckExist(Long linkId) {
        Long id = linkDao.getLinkForCheckExist(linkId);
        if (id == null || !id.equals(linkId)) return false;
        return true;
    }
}
