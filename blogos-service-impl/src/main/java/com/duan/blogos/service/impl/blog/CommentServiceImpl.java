package com.duan.blogos.service.impl.blog;


import com.duan.blogos.service.config.preference.DefaultProperties;
import com.duan.blogos.service.config.preference.WebsiteProperties;
import com.duan.blogos.service.dao.blog.BlogCommentDao;
import com.duan.blogos.service.dao.blogger.BloggerAccountDao;
import com.duan.blogos.service.dao.blogger.BloggerPictureDao;
import com.duan.blogos.service.dao.blogger.BloggerProfileDao;
import com.duan.blogos.service.dto.blog.BlogCommentDTO;
import com.duan.blogos.service.dto.blogger.BloggerDTO;
import com.duan.blogos.service.entity.blog.BlogComment;
import com.duan.blogos.service.entity.blogger.BloggerAccount;
import com.duan.blogos.service.entity.blogger.BloggerPicture;
import com.duan.blogos.service.entity.blogger.BloggerProfile;
import com.duan.blogos.service.enums.BlogCommentStatusEnum;
import com.duan.blogos.service.manager.DataFillingManager;
import com.duan.blogos.service.manager.StringConstructorManager;
import com.duan.blogos.service.restful.PageResult;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blog.CommentService;
import com.duan.blogos.service.util.ResultModelUtil;
import com.duan.common.util.CollectionUtils;
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
public class CommentServiceImpl implements CommentService {

    @Autowired
    private DefaultProperties defaultProperties;

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
    public ResultModel<PageResult<BlogCommentDTO>> listBlogComment(Long blogId, Integer pageSize, Integer pageNum) {
        pageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
        pageSize = pageSize == null || pageSize < 1 ? defaultProperties.getCommentCount() : pageSize;

        List<BlogCommentDTO> result = new ArrayList<>();

        PageHelper.startPage(pageNum, pageSize);
        PageInfo<BlogComment> pageInfo = new PageInfo<>(commentDao.listCommentByBlogId(blogId,
                BlogCommentStatusEnum.RIGHTFUL.getCode()));

        List<BlogComment> comments = pageInfo.getList();
        for (BlogComment comment : comments) {

            //评论者数据
            Long sid = comment.getSpokesmanId();
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

        return ResultModelUtil.pageResult(pageInfo, result);

    }

    private BloggerPicture getAvatar(Long id) {
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
    private BloggerProfile getProfile(Long bloggerId) {
        if (bloggerId == null) return null;
        return profileDao.getProfileByBloggerId(bloggerId);
    }
}
