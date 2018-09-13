package com.duan.blogos.service.manager;

import com.duan.blogos.service.dto.blog.*;
import com.duan.blogos.service.dto.blogger.BlogListItemDTO;
import com.duan.blogos.service.dto.blogger.*;
import com.duan.blogos.service.entity.blog.*;
import com.duan.blogos.service.entity.blogger.BloggerAccount;
import com.duan.blogos.service.entity.blogger.BloggerLink;
import com.duan.blogos.service.entity.blogger.BloggerPicture;
import com.duan.blogos.service.entity.blogger.BloggerProfile;
import com.duan.blogos.util.common.CollectionUtils;
import com.duan.blogos.util.common.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created on 2017/12/25.
 * entity 数据转换为 dto 数据
 *
 * @author DuanJiaNing
 */
@Component
public class DataFillingManager {

    public BlogStatisticsCountDTO blogStatisticsCountToDTO(BlogStatistics statistics) {

        BlogStatisticsCountDTO dto = new BlogStatisticsCountDTO();
        dto.setAdmireCount(statistics.getAdmireCount());
        dto.setBlogId(statistics.getBlogId());
        dto.setCollectCount(statistics.getCollectCount());
        dto.setCommentCount(statistics.getCommentCount());
        dto.setComplainCount(statistics.getComplainCount());
        dto.setId(statistics.getId());
        dto.setLikeCount(statistics.getLikeCount());
        dto.setReplyCommentCount(statistics.getReplyCommentCount());
        dto.setShareCount(statistics.getShareCount());
        dto.setViewCount(statistics.getViewCount());
        return dto;
    }

    public BlogCommentDTO blogCommentToDTO(BlogComment comment, BloggerDTO spokesman) {
        BlogCommentDTO dto = new BlogCommentDTO();
        dto.setBlogId(comment.getBlogId());
        dto.setContent(comment.getContent());
        dto.setId(comment.getId());
        dto.setReleaseDate(comment.getReleaseDate());
        dto.setState(comment.getState());
        dto.setSpokesman(spokesman);
        return dto;
    }

    public BloggerDTO bloggerAccountToDTO(BloggerAccount account, BloggerProfile profile, BloggerPicture avatar) {
        BloggerDTO dto = new BloggerDTO();
        dto.setId(account.getId());
        dto.setRegisterDate(account.getRegisterDate());
        dto.setUsername(account.getUsername());
        dto.setAvatar(bloggerPicture2DTO(avatar));
        dto.setProfile(bloggerProfile2DTO(profile));

        return dto;
    }

    public com.duan.blogos.service.dto.blog.BlogListItemDTO blogListItemToDTO(BlogStatistics statistics,
                                                                              BlogCategory[] categories,
                                                                              BlogLabel[] labels,
                                                                              Blog blog, String blogImg) {
        com.duan.blogos.service.dto.blog.BlogListItemDTO dto = new com.duan.blogos.service.dto.blog.BlogListItemDTO();
        dto.setCategories(blogCategory2DTO(categories));
        dto.setLabels(blogLabel2DTO(labels));
        dto.setCollectCount(statistics.getCollectCount());
        dto.setCommentCount(statistics.getCommentCount());
        dto.setId(blog.getId());
        dto.setLikeCount(statistics.getLikeCount());
        dto.setReleaseDate(blog.getReleaseDate());
        dto.setSummary(blog.getSummary());
        dto.setTitle(blog.getTitle());
        dto.setViewCount(statistics.getViewCount());
        dto.setImg(blogImg);
        return dto;

    }

    public BloggerLinkDTO bloggerLinkToDTO(BloggerLink link, BloggerPicture icon) {
        BloggerLinkDTO dto = new BloggerLinkDTO();
        dto.setBewrite(link.getBewrite());
        dto.setBloggerId(link.getBloggerId());


        BloggerPictureDTO pictureDTO = new BloggerPictureDTO();
        pictureDTO.setId(icon.getId());
        pictureDTO.setBloggerId(icon.getBloggerId());
        pictureDTO.setBewrite(icon.getBewrite());
        pictureDTO.setCategory(icon.getCategory());
        pictureDTO.setPath(icon.getPath());
        pictureDTO.setTitle(icon.getTitle());
        pictureDTO.setUploadDate(icon.getUploadDate());
        dto.setIcon(pictureDTO);

        dto.setId(link.getId());
        dto.setTitle(link.getTitle());
        dto.setUrl(link.getUrl());
        return dto;
    }

    public BlogMainContentDTO blogMainContentToDTO(Blog blog, List<BlogCategory> categories, List<BlogLabel> labels,
                                                   String splitChar) {

        BlogMainContentDTO dto = new BlogMainContentDTO();
        dto.setCategories(categories == null ? null : blogCategory2DTO(categories));
        dto.setLabels(labels == null ? null : blogLabel2DTO(labels));
        dto.setId(blog.getId());
        dto.setKeyWords(StringUtils.stringArrayToArray(blog.getKeyWords(), splitChar));
        dto.setNearestModifyDate(blog.getNearestModifyDate());
        dto.setReleaseDate(blog.getReleaseDate());
        dto.setStatus(blog.getState());
        dto.setSummary(blog.getSummary());
        dto.setTitle(blog.getTitle());
        dto.setWordCount(blog.getWordCount());
        dto.setContent(blog.getContent());

        return dto;
    }

    public BloggerCategoryDTO blogCategoryToDTO(BlogCategory category, BloggerPicture icon, int count) {
        BloggerCategoryDTO dto = new BloggerCategoryDTO();
        dto.setBloggerId(category.getBloggerId());
        dto.setCreateDate(category.getCreateDate());
        dto.setBewrite(category.getBewrite());
        dto.setIcon(bloggerPicture2DTO(icon));
        dto.setId(category.getId());
        dto.setTitle(category.getTitle());
        dto.setCount(count);
        return dto;
    }

    public BlogListItemDTO bloggerBlogListItemToDTO(Blog blog, BlogStatistics statistics,
                                                    List<BlogCategory> categories) {
        BlogListItemDTO dto = new BlogListItemDTO();
        dto.setCategories(CollectionUtils.isEmpty(categories) ? null : blogCategory2DTO(categories));
        dto.setCollectCount(statistics.getCollectCount());
        dto.setCommentCount(statistics.getCommentCount());
        dto.setComplainCount(statistics.getComplainCount());
        dto.setId(blog.getId());
        dto.setLikeCount(statistics.getLikeCount());
        dto.setNearestModifyDate(blog.getNearestModifyDate());
        dto.setReleaseDate(blog.getReleaseDate());
        dto.setState(blog.getState());
        dto.setSummary(blog.getSummary());
        dto.setTitle(blog.getTitle());
        dto.setViewCount(statistics.getViewCount());
        dto.setWordCount(blog.getWordCount());
        return dto;
    }

    public BlogStatisticsDTO blogStatisticsToDTO(Blog blog, BlogStatistics statistics, BlogCategory[] categories,
                                                 BlogLabel[] labels, BloggerDTO[] likes, BloggerDTO[] collects,
                                                 BloggerDTO[] commenter, String splitChar) {
        BlogStatisticsDTO dto = new BlogStatisticsDTO();
        dto.setCategories(blogCategory2DTO(categories));
        dto.setCollects(collects);
        dto.setCommenter(commenter);
        dto.setId(statistics.getId());
        dto.setKeyWords(StringUtils.stringArrayToArray(blog.getKeyWords(), splitChar));
        dto.setLabels(blogLabel2DTO(labels));
        dto.setLikes(likes);
        dto.setNearestModifyDate(blog.getNearestModifyDate());
        dto.setReleaseDate(blog.getReleaseDate());
        dto.setState(blog.getState());
        dto.setStatistics(blogStatistics2DTO(statistics));
        dto.setSummary(blog.getSummary());
        dto.setTitle(blog.getTitle());
        dto.setWordCount(blog.getWordCount());

        return dto;
    }

    public BloggerStatisticsDTO bloggerStatisticToDTO(int blogCount, int wordCount, int likeCount, int likedCount,
                                                      int categoryCount, int labelCount, int collectCount,
                                                      int collectedCount, int linkCount) {
        BloggerStatisticsDTO dto = new BloggerStatisticsDTO();
        dto.setBlogCount(blogCount);
        dto.setWordCount(wordCount);
        dto.setLikeCount(likeCount);
        dto.setLikedCount(likedCount);
        dto.setCategoryCount(categoryCount);
        dto.setLabelCount(labelCount);
        dto.setCollectCount(collectCount);
        dto.setCollectedCount(collectedCount);
        dto.setLinkCount(linkCount);

        return dto;
    }

    public FavouriteBlogListItemDTO collectBlogListItemToDTO(int bloggerId, BlogCollect collect, com.duan.blogos.service.dto.blog.BlogListItemDTO blog, BloggerDTO author) {
        FavouriteBlogListItemDTO dto = new FavouriteBlogListItemDTO();
        dto.setAuthor(author);
        dto.setBlog(blog);
        dto.setBloggerId(bloggerId);
        dto.setDate(collect.getCollectDate());
        dto.setId(collect.getId());
        dto.setReason(collect.getReason());
        return dto;
    }

    public FavouriteBlogListItemDTO likeBlogListItemToDTO(int bloggerId, BlogLike like, com.duan.blogos.service.dto.blog.BlogListItemDTO blog, BloggerDTO liker) {
        FavouriteBlogListItemDTO dto = new FavouriteBlogListItemDTO();
        dto.setAuthor(liker);
        dto.setBlog(blog);
        dto.setBloggerId(bloggerId);
        dto.setDate(like.getLikeDate());
        dto.setId(like.getId());
        return dto;
    }

    public BloggerBriefDTO bloggerTobrief(BloggerDTO bloggerDTO, BloggerStatisticsDTO statisticsDTO) {
        BloggerBriefDTO dto = new BloggerBriefDTO();
        dto.setBlogger(bloggerDTO);
        dto.setStatistics(statisticsDTO);
        return dto;
    }

// ------------------------------------------------------------------------------------------------

    private BlogLabelDTO blogLabel2DTO(BlogLabel label) {
        BlogLabelDTO d = new BlogLabelDTO();
        d.setId(label.getId());
        d.setBloggerId(label.getBloggerId());
        d.setTitle(label.getTitle());
        d.setCreateDate(label.getCreateDate());

        return d;
    }

    private BlogCategoryDTO blogCategory2DTO(BlogCategory category) {
        BlogCategoryDTO d = new BlogCategoryDTO();
        d.setId(category.getId());
        d.setBloggerId(category.getBloggerId());
        d.setIconId(category.getIconId());
        d.setTitle(category.getTitle());
        d.setBewrite(category.getBewrite());
        d.setCreateDate(category.getCreateDate());
        return d;
    }

    private BlogLabelDTO[] blogLabel2DTO(List<BlogLabel> labels) {
        BlogLabelDTO[] dtos = new BlogLabelDTO[labels.size()];
        for (int i = 0; i < labels.size(); i++) {
            dtos[i] = blogLabel2DTO(labels.get(i));
        }

        return dtos;
    }

    private BlogCategoryDTO[] blogCategory2DTO(List<BlogCategory> categories) {
        BlogCategoryDTO[] dtos = new BlogCategoryDTO[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            dtos[i] = blogCategory2DTO(categories.get(i));
        }

        return dtos;
    }

    private BloggerPictureDTO bloggerPicture2DTO(BloggerPicture avatar) {

        BloggerPictureDTO pictureDTO = new BloggerPictureDTO();
        pictureDTO.setId(avatar.getId());
        pictureDTO.setBloggerId(avatar.getBloggerId());
        pictureDTO.setBewrite(avatar.getBewrite());
        pictureDTO.setCategory(avatar.getCategory());
        pictureDTO.setPath(avatar.getPath());
        pictureDTO.setTitle(avatar.getTitle());
        pictureDTO.setUploadDate(avatar.getUploadDate());

        return pictureDTO;
    }

    private BloggerProfileDTO bloggerProfile2DTO(BloggerProfile profile) {

        BloggerProfileDTO profileDTO = new BloggerProfileDTO();
        profileDTO.setId(profile.getId());
        profileDTO.setBloggerId(profile.getBloggerId());
        profileDTO.setAvatarId(profile.getAvatarId());
        profileDTO.setPhone(profile.getPhone());
        profileDTO.setEmail(profile.getEmail());
        profileDTO.setAboutMe(profile.getAboutMe());
        profileDTO.setIntro(profile.getIntro());

        return profileDTO;
    }

    private BlogCategoryDTO[] blogCategory2DTO(BlogCategory[] categories) {

        BlogCategoryDTO[] cs = new BlogCategoryDTO[categories.length];
        for (int i = 0; i < categories.length; i++) {
            cs[i] = blogCategory2DTO(categories[i]);
        }

        return cs;
    }

    private BlogLabelDTO[] blogLabel2DTO(BlogLabel[] labels) {
        BlogLabelDTO[] bl = new BlogLabelDTO[labels.length];
        for (int i = 0; i < labels.length; i++) {
            bl[i] = blogLabel2DTO(labels[i]);
        }

        return bl;
    }

    private BlogBaseStatisticsDTO blogStatistics2DTO(BlogStatistics statistics) {
        BlogBaseStatisticsDTO dto = new BlogBaseStatisticsDTO();
        dto.setId(statistics.getId());
        dto.setBlogId(statistics.getBlogId());
        dto.setCommentCount(statistics.getCommentCount());
        dto.setViewCount(statistics.getViewCount());
        dto.setReplyCommentCount(statistics.getReplyCommentCount());
        dto.setCollectCount(statistics.getCollectCount());
        dto.setComplainCount(statistics.getComplainCount());
        dto.setShareCount(statistics.getShareCount());
        dto.setAdmireCount(statistics.getAdmireCount());
        dto.setLikeCount(statistics.getLikeCount());
        dto.setReleaseDate(statistics.getReleaseDate());

        return dto;
    }

}
