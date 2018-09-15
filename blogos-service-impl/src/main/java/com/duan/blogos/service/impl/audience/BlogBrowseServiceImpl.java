package com.duan.blogos.service.impl.audience;


import com.duan.blogos.service.config.preference.DefaultProperties;
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
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.audience.BlogBrowseService;
import com.duan.blogos.util.common.CollectionUtils;
import com.duan.blogos.util.common.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private DefaultProperties pageSizeProperties;

    @Value("${preference.db.string-filed-split-character-for-number}")
    private String stringFiledSplitCharacterForNumber;

    @Value("${preference.db.string-filed-split-character-for-string}")
    private String stringFiledSplitCharacterForString;

    @Value("${preference.manager.id}")
    private Integer managerId;

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
        int[] cids = StringUtils.intStringDistinctToArray(blog.getCategoryIds(), stringFiledSplitCharacterForNumber);
        int[] lids = StringUtils.intStringDistinctToArray(blog.getLabelIds(), stringFiledSplitCharacterForNumber);
        List<BlogCategory> categories = cids == null ? null : categoryDao.listCategoryById(cids);
        List<BlogLabel> labels = lids == null ? null : labelDao.listLabelById(lids);

        //填充数据
        BlogMainContentDTO dto = dataFillingManager.blogMainContentToDTO(blog, categories, labels, stringFiledSplitCharacterForString);

        return new ResultModel<>(dto);
    }

    @Override
    public ResultModel<List<BlogCommentDTO>> listBlogComment(int blogId, int offset, int rows) {
        offset = offset < 0 ? 0 : offset;
        rows = rows < 0 ? pageSizeProperties.getComment() : rows;

        List<BlogCommentDTO> result = new ArrayList<>();

        List<BlogComment> comments = commentDao.listCommentByBlogId(blogId, offset, rows,
                BlogCommentStatusEnum.RIGHTFUL.getCode());
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

        return CollectionUtils.isEmpty(result) ? null : new ResultModel<>(result);
    }

    private BloggerPicture getAvatar(Integer id) {
        BloggerPicture avatar;
        if (id != null) {
            avatar = pictureDao.getPictureById(id);
        } else {
            avatar = pictureDao.getBloggerUniquePicture(managerId, DEFAULT_BLOGGER_AVATAR.getCode());
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
