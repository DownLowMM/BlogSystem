package com.duan.blogos.service.common.util;

import com.duan.blogos.service.common.dto.BlogCategoryRelaDTO;
import com.duan.blogos.service.common.dto.BlogLabelRelaDTO;
import com.duan.blogos.service.common.dto.LoginResultDTO;
import com.duan.blogos.service.common.dto.blog.*;
import com.duan.blogos.service.common.dto.blogger.*;
import com.duan.blogos.service.entity.*;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created on 2018/10/1.
 *
 * @author DuanJiaNing
 */
public class DataConverter {

    public static class PO2DTO {

        public static BloggerSettingDTO bloggerSetting2DTO(BloggerSetting setting) {
            if (setting == null) {
                return null;
            }

            BloggerSettingDTO dto = new BloggerSettingDTO();

            dto.setId(setting.getId());
            dto.setBloggerId(setting.getBloggerId());
            dto.setMainPageNavPos(setting.getMainPageNavPos());

            return dto;
        }

        public static BlogLabelRelaDTO blogLabelRela2DTO(BlogLabelRela blogLabelRela) {

            BlogLabelRelaDTO dto = new BlogLabelRelaDTO();
            dto.setId(blogLabelRela.getId());
            dto.setBlogId(blogLabelRela.getBlogId());
            dto.setLabelId(blogLabelRela.getLabelId());
            dto.setInsertTime(blogLabelRela.getInsertTime());

            return dto;
        }

        public static BlogCategoryRelaDTO blogCategoryRela2DTO(BlogCategoryRela blogCategoryRela) {

            BlogCategoryRelaDTO dto = new BlogCategoryRelaDTO();
            dto.setId(blogCategoryRela.getId());
            dto.setBlogId(blogCategoryRela.getBlogId());
            dto.setCategoryId(blogCategoryRela.getCategoryId());
            dto.setInsertTime(blogCategoryRela.getInsertTime());

            return dto;
        }

        public static BlogDTO blog2DTO(Blog blog, List<BlogCategoryRela> relas, List<BlogLabelRela> labelRelas) {
            if (blog == null) {
                return null;
            }

            BlogDTO dto = new BlogDTO();

            dto.setId(blog.getId());
            dto.setBloggerId(blog.getBloggerId());
            dto.setCategoryIds(CollectionUtils.isEmpty(relas) ? null :
                    relas.stream()
                            .map(DataConverter.PO2DTO::blogCategoryRela2DTO)
                            .collect(Collectors.toList()));

            dto.setLabelIds(CollectionUtils.isEmpty(labelRelas) ? null :
                    labelRelas.stream()
                            .map(DataConverter.PO2DTO::blogLabelRela2DTO)
                            .collect(Collectors.toList()));

            dto.setState(blog.getState());
            dto.setTitle(blog.getTitle());
            dto.setContent(blog.getContent());
            dto.setContentMd(blog.getContentMd());
            dto.setSummary(blog.getSummary());
            dto.setReleaseDate(blog.getReleaseDate());
            dto.setNearestModifyDate(blog.getNearestModifyDate());
            dto.setKeyWords(blog.getKeyWords());

            return dto;
        }

        public static BloggerAccountDTO bloggerAccount2DTO(BloggerAccount account) {
            if (account == null) {
                return null;
            }

            BloggerAccountDTO dto = new BloggerAccountDTO();
            dto.setId(account.getId());
            dto.setUsername(account.getUsername());
            dto.setRegisterDate(account.getRegisterDate());

            return dto;
        }

        public static BloggerProfileDTO bloggerProfile2DTO(BloggerProfile profile) {
            if (profile == null) {
                return null;
            }

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

        public static BloggerPictureDTO bloggerPicture2DTO(BloggerPicture picture) {
            if (picture == null) {
                return null;
            }

            BloggerPictureDTO pictureDTO = new BloggerPictureDTO();
            pictureDTO.setId(picture.getId());
            pictureDTO.setBloggerId(picture.getBloggerId());
            pictureDTO.setBewrite(picture.getBewrite());
            pictureDTO.setCategory(picture.getCategory());
            pictureDTO.setPath(picture.getPath());
            pictureDTO.setTitle(picture.getTitle());
            pictureDTO.setUploadDate(picture.getUploadDate());

            return pictureDTO;
        }

        public static LoginResultDTO getLoginResultDTO(BloggerAccount po, String token) {
            LoginResultDTO dto = new LoginResultDTO();
            dto.setId(po.getId());
            dto.setRegisterDate(po.getRegisterDate());
            dto.setUsername(po.getUsername());
            dto.setToken(token);

            return dto;
        }

        public static BlogBaseStatisticsDTO blogStatisticsCountToDTO(BlogStatistics statistics) {
            if (statistics == null) {
                return null;
            }

            BlogBaseStatisticsDTO dto = new BlogBaseStatisticsDTO();
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
            dto.setWordCount(statistics.getWordCount());
            return dto;
        }

        public static BlogCommentDTO blogCommentToDTO(BlogComment comment, BloggerDTO spokesman) {
            if (comment == null) {
                return null;
            }

            BlogCommentDTO dto = new BlogCommentDTO();
            dto.setBlogId(comment.getBlogId());
            dto.setContent(comment.getContent());
            dto.setId(comment.getId());
            dto.setReleaseDate(comment.getReleaseDate());
            dto.setState(comment.getState());
            dto.setSpokesman(spokesman);
            return dto;
        }

        public static BloggerDTO bloggerAccountToDTO(BloggerAccount account, BloggerProfile profile, BloggerPicture avatar) {
            if (account == null) {
                return null;
            }

            BloggerDTO dto = new BloggerDTO();
            dto.setId(account.getId());
            dto.setRegisterDate(account.getRegisterDate());
            dto.setUsername(account.getUsername());
            dto.setAvatar(bloggerPicture2DTO(avatar));
            dto.setProfile(bloggerProfile2DTO(profile));

            return dto;
        }

        public static BlogListItemDTO blogListItemToDTO(BlogStatistics statistics,
                                                        BlogCategory[] categories,
                                                        BlogLabel[] labels,
                                                        Blog blog, String blogImg) {
            if (blog == null) {
                return null;
            }

            BlogListItemDTO dto = new BlogListItemDTO();
            dto.setState(blog.getState());
            dto.setNearestModifyDate(blog.getNearestModifyDate());
            dto.setStatistics(DataConverter.PO2DTO.blogStatisticsCountToDTO(statistics));
            dto.setCategories(blogCategory2DTO(categories));
            dto.setLabels(blogLabel2DTO(labels));

            dto.setId(blog.getId());
            dto.setReleaseDate(blog.getReleaseDate());
            dto.setSummary(blog.getSummary());
            dto.setTitle(blog.getTitle());
            dto.setImg(blogImg);
            return dto;

        }

        public static BloggerLinkDTO bloggerLinkToDTO(BloggerLink link, BloggerPicture icon) {
            if (link == null) {
                return null;
            }

            BloggerLinkDTO dto = new BloggerLinkDTO();
            dto.setBewrite(link.getBewrite());
            dto.setBloggerId(link.getBloggerId());
            dto.setIcon(bloggerPicture2DTO(icon));

            dto.setId(link.getId());
            dto.setTitle(link.getTitle());
            dto.setUrl(link.getUrl());
            return dto;
        }

        public static BloggerCategoryDTO blogCategoryToDTO(BlogCategory category, BloggerPicture icon, int count) {
            if (category == null) {
                return null;
            }

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


        public static BlogStatisticsDTO blogStatisticsToDTO(Blog blog, BlogStatistics statistics,
                                                            List<BlogCategory> categories,
                                                            List<BlogLabel> labels,
                                                            List<BloggerDTO> likes,
                                                            List<BloggerDTO> collects,
                                                            List<BloggerDTO> commenter, String splitChar) {
            if (blog == null || statistics == null) {
                return null;
            }

            BlogStatisticsDTO dto = new BlogStatisticsDTO();
            dto.setCategories(blogCategory2DTO(categories));
            dto.setCollects(collects);
            dto.setCommenter(commenter);
            dto.setId(statistics.getId());
            dto.setKeyWords(Util.stringArrayToArray(blog.getKeyWords(), splitChar));
            dto.setLabels(blogLabel2DTO(labels));
            dto.setLikes(likes);
            dto.setNearestModifyDate(blog.getNearestModifyDate());
            dto.setReleaseDate(blog.getReleaseDate());
            dto.setState(blog.getState());
            dto.setStatistics(blogStatistics2BaseDTO(statistics));
            dto.setSummary(blog.getSummary());
            dto.setTitle(blog.getTitle());
            dto.setWordCount(statistics.getWordCount());

            return dto;
        }

        public static BloggerStatisticsDTO bloggerStatisticToDTO(int blogCount, int wordCount,
                                                                 int likeCount, int likedCount,
                                                                 int categoryCount, int labelCount,
                                                                 int collectCount, int collectedCount,
                                                                 int linkCount) {
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

        public static FavoriteBlogListItemDTO favoriteBlogListItemToDTO(Long bloggerId, Long id, Timestamp favoriteDate,
                                                                        String reason, BlogListItemDTO blog, BloggerDTO favoriter) {

            FavoriteBlogListItemDTO dto = new FavoriteBlogListItemDTO();
            dto.setAuthor(favoriter);
            dto.setBlog(blog);
            dto.setBloggerId(bloggerId);
            dto.setDate(favoriteDate);
            dto.setId(id);
            dto.setReason(reason);
            return dto;
        }

        public static BlogLabelDTO blogLabel2DTO(BlogLabel label) {
            if (label == null) {
                return null;
            }

            BlogLabelDTO d = new BlogLabelDTO();
            d.setId(label.getId());
            d.setBloggerId(label.getBloggerId());
            d.setTitle(label.getTitle());
            d.setCreateDate(label.getCreateDate());

            return d;
        }

        public static BlogCategoryDTO[] blogCategory2DTO(BlogCategory[] categories) {
            if (Util.isArrayEmpty(categories)) {
                return null;
            }

            BlogCategoryDTO[] cs = new BlogCategoryDTO[categories.length];
            for (int i = 0; i < categories.length; i++) {
                cs[i] = blogCategory2DTO(categories[i]);
            }

            return cs;
        }

        public static BlogLabelDTO[] blogLabel2DTO(BlogLabel[] labels) {
            if (Util.isArrayEmpty(labels)) {
                return null;
            }

            BlogLabelDTO[] bl = new BlogLabelDTO[labels.length];
            for (int i = 0; i < labels.length; i++) {
                bl[i] = blogLabel2DTO(labels[i]);
            }

            return bl;
        }

        public static BlogBaseStatisticsDTO blogStatistics2BaseDTO(BlogStatistics statistics) {
            if (statistics == null) {
                return null;
            }

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
            dto.setWordCount(statistics.getWordCount());
            dto.setReleaseDate(statistics.getReleaseDate());

            return dto;
        }

        public static BlogLabelDTO[] blogLabel2DTO(List<BlogLabel> labels) {
            if (CollectionUtils.isEmpty(labels)) {
                return null;
            }

            BlogLabelDTO[] dtos = new BlogLabelDTO[labels.size()];
            for (int i = 0; i < labels.size(); i++) {
                dtos[i] = blogLabel2DTO(labels.get(i));
            }

            return dtos;
        }

        public static BlogCategoryDTO[] blogCategory2DTO(List<BlogCategory> categories) {
            if (CollectionUtils.isEmpty(categories)) {
                return null;
            }

            BlogCategoryDTO[] dtos = new BlogCategoryDTO[categories.size()];
            for (int i = 0; i < categories.size(); i++) {
                dtos[i] = blogCategory2DTO(categories.get(i));
            }

            return dtos;
        }

        public static BlogCategoryDTO blogCategory2DTO(BlogCategory category) {
            if (category == null) {
                return null;
            }

            BlogCategoryDTO d = new BlogCategoryDTO();
            d.setId(category.getId());
            d.setBloggerId(category.getBloggerId());
            d.setIconId(category.getIconId());
            d.setTitle(category.getTitle());
            d.setBewrite(category.getBewrite());
            d.setCreateDate(category.getCreateDate());
            return d;
        }

    }

    public static class DTO2DTO {

        public static BloggerBriefDTO bloggerTobrief(BloggerDTO bloggerDTO, BloggerStatisticsDTO statisticsDTO) {
            BloggerBriefDTO dto = new BloggerBriefDTO();
            dto.setBlogger(bloggerDTO);
            dto.setStatistics(statisticsDTO);
            return dto;
        }

    }

}
