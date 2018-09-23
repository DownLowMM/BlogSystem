package com.duan.blogos.service.impl.audience;


import com.duan.blogos.service.config.preference.DbProperties;
import com.duan.blogos.service.config.preference.DefaultProperties;
import com.duan.blogos.service.config.preference.WebsiteProperties;
import com.duan.blogos.service.dao.blog.BlogCategoryDao;
import com.duan.blogos.service.dao.blog.BlogCommentDao;
import com.duan.blogos.service.dao.blog.BlogDao;
import com.duan.blogos.service.dao.blog.BlogLabelDao;
import com.duan.blogos.service.dao.blogger.BloggerAccountDao;
import com.duan.blogos.service.dao.blogger.BloggerPictureDao;
import com.duan.blogos.service.dao.blogger.BloggerProfileDao;
import com.duan.blogos.service.dto.blog.BlogCommentDTO;
import com.duan.blogos.service.dto.blog.BlogMainContentDTO;
import com.duan.blogos.service.dto.blogger.BloggerDTO;
import com.duan.blogos.service.entity.blog.Blog;
import com.duan.blogos.service.entity.blog.BlogCategory;
import com.duan.blogos.service.entity.blog.BlogComment;
import com.duan.blogos.service.entity.blog.BlogLabel;
import com.duan.blogos.service.entity.blogger.BloggerAccount;
import com.duan.blogos.service.entity.blogger.BloggerPicture;
import com.duan.blogos.service.entity.blogger.BloggerProfile;
import com.duan.blogos.service.enums.BlogCommentStatusEnum;
import com.duan.blogos.service.manager.DataFillingManager;
import com.duan.blogos.service.manager.StringConstructorManager;
import com.duan.blogos.service.restful.PageResult;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.audience.BlogBrowseService;
import com.duan.common.util.CollectionUtils;
import com.duan.common.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.duan.blogos.service.enums.BloggerPictureCategoryEnum.DEFAULT_BLOGGER_AVATAR;

/**
 * Created on 2017/12/19.
 *
 * @author DuanJiaNing
 */
@Service
public class BlogBrowseServiceImpl implements BlogBrowseService {

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private BlogCategoryDao categoryDao;

    @Autowired
    private BlogLabelDao labelDao;

    @Autowired
    private DefaultProperties defaultProperties;

    @Autowired
    private DbProperties dbProperties;

    @Autowired
    private WebsiteProperties websiteProperties;

    @Autowired
    private DataFillingManager dataFillingManager;

    @Autowired
    private StringConstructorManager constructorManager;

    @Autowired
    private BlogCommentDao commentDao;

    @Autowired
    private BloggerAccountDao accountDao;

    @Autowired
    private BloggerPictureDao pictureDao;

    @Autowired
    private BloggerProfileDao profileDao;

    @Override
    public ResultModel<BlogMainContentDTO> getBlogMainContent(int blogId) {

        //查询数据
        Blog blog = blogDao.getBlogById(blogId);
        if (blog == null) return null;
        String nsp = dbProperties.getStringFiledSplitCharacterForNumber();
        int[] cids = StringUtils.intStringDistinctToArray(blog.getCategoryIds(), nsp);
        int[] lids = StringUtils.intStringDistinctToArray(blog.getLabelIds(), nsp);
        List<BlogCategory> categories = cids == null ? null : categoryDao.listCategoryById(cids);
        List<BlogLabel> labels = lids == null ? null : labelDao.listLabelById(lids);

        //填充数据
        BlogMainContentDTO dto = dataFillingManager.blogMainContentToDTO(blog, categories, labels,
                dbProperties.getStringFiledSplitCharacterForString());

        return new ResultModel<>(dto);
    }

    @Override
    public ResultModel<PageResult<BlogCommentDTO>> listBlogComment(int blogId, Integer pageSize, Integer pageNum) {
        pageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
        pageSize = pageSize == null || pageSize < 0 ? defaultProperties.getCommentCount() : pageSize;

        List<BlogCommentDTO> result = new ArrayList<>();

        PageHelper.startPage(pageNum, pageSize);
        PageInfo<BlogComment> pageInfo = new PageInfo<>(commentDao.listCommentByBlogId(blogId,
                BlogCommentStatusEnum.RIGHTFUL.getCode()));

        List<BlogComment> comments = pageInfo.getList();
        for (BlogComment comment : comments) {

            //评论者数据
            int sid = comment.getSpokesmanId();
            BloggerAccount smAccount = accountDao.getAccountById(sid);
            BloggerProfile smProfile = getProfile(sid);
            BloggerDTO smDTO = dataFillingManager.bloggerAccountToDTO(smAccount, smProfile,
                    getAvatar(smProfile.getAvatarId()));

            BlogCommentDTO dto = dataFillingManager.blogCommentToDTO(comment, smDTO);
            result.add(dto);
        }

        if (CollectionUtils.isEmpty(result)) {
            return null;
        }

        return new ResultModel<>(new PageResult<>(pageInfo.getTotal(), result));
    }

    private BloggerPicture getAvatar(Integer id) {
        BloggerPicture avatar;
        if (id != null) {
            avatar = pictureDao.getPictureById(id);
        } else {
            avatar = pictureDao.getBloggerUniquePicture(websiteProperties.getManagerId(),
                    DEFAULT_BLOGGER_AVATAR.getCode());
        }

        if (avatar != null) {
            avatar.setPath(constructorManager.constructPictureUrl(avatar, DEFAULT_BLOGGER_AVATAR));
        }

        return avatar;
    }


    //获得博主资料
    private BloggerProfile getProfile(Integer bloggerId) {
        if (bloggerId == null) return null;
        return profileDao.getProfileByBloggerId(bloggerId);
    }
}
