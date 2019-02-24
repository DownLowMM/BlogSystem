package com.duan.blogos.service.impl.blogger;

import com.alibaba.dubbo.config.annotation.Service;
import com.duan.blogos.service.blogger.BloggerAccountService;
import com.duan.blogos.service.common.CodeMessage;
import com.duan.blogos.service.common.dto.blogger.BloggerAccountDTO;
import com.duan.blogos.service.common.util.DataConverter;
import com.duan.blogos.service.common.util.ExceptionUtil;
import com.duan.blogos.service.common.util.Util;
import com.duan.blogos.service.config.preference.WebsiteProperties;
import com.duan.blogos.service.dao.*;
import com.duan.blogos.service.entity.*;
import com.duan.blogos.service.manager.BlogLuceneIndexManager;
import com.duan.blogos.service.manager.ImageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created on 2017/12/19.
 *
 * @author DuanJiaNing
 */
@Service
public class BloggerAccountServiceImpl implements BloggerAccountService {

    @Autowired
    private BloggerAccountDao accountDao;

    @Autowired
    private BloggerPictureDao pictureDao;

    @Autowired
    private BloggerProfileDao profileDao;

    @Autowired
    private BloggerSettingDao settingDao;

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private BlogCollectDao collectDao;

    @Autowired
    private BlogLikeDao likeDao;

    @Autowired
    private BlogStatisticsDao blogStatisticsDao;

    @Autowired
    private ImageManager imageManager;

    @Autowired
    private BlogLuceneIndexManager luceneIndexManager;

    @Autowired
    private WebsiteProperties websiteProperties;

    @Override
    public Long insertAccount(String username, String password) {

        String shaPwd;
        try {
            //将密码通过sha的方式保存
            shaPwd = new BigInteger(Util.toSha(password)).toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw ExceptionUtil.get(CodeMessage.COMMON_UNKNOWN_ERROR, e);
        }

        BloggerAccount account = new BloggerAccount();
        account.setUsername(username);
        account.setPassword(shaPwd);
        int effect = accountDao.insert(account);
        if (effect <= 0) return null;

        Long bloggerId = account.getId();

        // 生成博主设置数据
        BloggerSetting setting = new BloggerSetting();
        setting.setBloggerId(bloggerId);
        setting.setMainPageNavPos(websiteProperties.getPageNavPos());
        settingDao.insert(setting);

        return bloggerId;
    }

    @Override
    public BloggerAccountDTO getAccount(String bloggerName) {
        BloggerAccount account = accountDao.getAccountByName(bloggerName);
        return DataConverter.PO2DTO.bloggerAccount2DTO(account);
    }

    @Override
    public boolean deleteAccount(Long bloggerId) {

        // 图片管理员不允许注销账号
        if (websiteProperties.getManagerId().equals(bloggerId))
            return false;

        //博主图片需要手动删除
        List<BloggerPicture> ps = pictureDao.getPictureByBloggerId(bloggerId);
        if (!CollectionUtils.isEmpty(ps))
            ps.stream().map(BloggerPicture::getPath).forEach(imageManager::deleteImageFromDisk);

        // 删除博文的lucene索引
        List<Blog> blogIds = blogDao.listAllLabelByBloggerId(bloggerId);
        if (!CollectionUtils.isEmpty(blogIds))
            blogIds.stream().mapToLong(Blog::getId).forEach(luceneIndexManager::delete);

        // 将博主喜欢和收藏的博文的喜欢/收藏次数减一
        List<BlogCollect> collects = collectDao.listAllIdByBloggerId(bloggerId);
        if (!CollectionUtils.isEmpty(collects))
            collects.stream().mapToLong(BlogCollect::getBlogId).forEach(blogStatisticsDao::updateCollectCountMinus);

        List<BlogLike> likes = likeDao.listAllIdByBloggerId(bloggerId);
        if (!CollectionUtils.isEmpty(likes))
            likes.stream().mapToLong(BlogLike::getBlogId).forEach(blogStatisticsDao::updateLikeCountMinus);


        // 账户相关数据删除由关系数据库负责处理
        // 删除 blogger_account 数据
        // ->
        int effect = accountDao.delete(bloggerId);
        if (effect <= 0) return false;

        return true;
    }

    @Override
    public boolean updateAccountUserName(Long uid, String newUserName) {

        BloggerAccount account = new BloggerAccount();
        account.setId(uid);
        account.setUsername(newUserName);
        int effect = accountDao.update(account);
        if (effect <= 0) return false;

        return true;
    }

    @Override
    public boolean updateAccountPassword(Long bloggerId, String oldPassword, String newPassword) {

        String oldSha;
        String newSha;

        try {
            oldSha = new BigInteger(Util.toSha(oldPassword)).toString();
            newSha = new BigInteger(Util.toSha(newPassword)).toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw ExceptionUtil.get(CodeMessage.COMMON_UNKNOWN_ERROR, e);
        }

        BloggerAccount account = accountDao.getAccountById(bloggerId);
        String oriSha = account.getPassword();

        // 旧密码正确，同时与新密码相同（并没有修改）
        if (!oriSha.equals(oldSha) || oldSha.equals(newSha)) return false;

        BloggerAccount a = new BloggerAccount();
        a.setId(bloggerId);
        a.setPassword(newSha);
        int effect = accountDao.update(a);
        if (effect <= 0) return false;

        return true;
    }

    @Override
    public BloggerAccountDTO getAccountByPhone(String phone) {
        BloggerProfile profile = profileDao.getProfileByPhone(phone);
        if (profile == null) return null;
        BloggerAccount account = accountDao.getAccountById(profile.getBloggerId());
        return DataConverter.PO2DTO.bloggerAccount2DTO(account);
    }
}
