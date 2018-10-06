package com.duan.blogos.service.dao.blogger;

import com.duan.blogos.service.dao.BaseDao;
import com.duan.blogos.service.entity.blogger.BloggerPicture;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created on 2017/12/25.
 *
 * @author DuanJiaNing
 */
@Repository
public interface BloggerPictureDao extends BaseDao<BloggerPicture> {

    /**
     * 通过id查询图片
     *
     * @param id 图片id
     * @return 查询结果
     */
    BloggerPicture getPictureById(Long id);

    /**
     * 根据图片类别获得博主图片
     *
     * @param bloggerId 博主id
     * @param category  类别
     * @return 查询结果
     */
    BloggerPicture getPictureByCategory(@Param("bloggerId") Long bloggerId, @Param("category") Long category);

    /**
     * 获取博主唯一的图片，如图片管理员管理的默认图片，博主的头像。
     *
     * @param bloggerId 博主id
     * @param category  类别
     * @return 查询结果
     */
    BloggerPicture getBloggerUniquePicture(@Param("bloggerId") Long bloggerId,
                                           @Param("category") Integer category);

    /**
     * 查询博主的所有图片
     *
     * @param bloggerId 博主id
     * @return 查询结果
     */
    List<BloggerPicture> listPictureByBloggerId(@Param("bloggerId") Long bloggerId);

    /**
     * 查询博主的指定类别图片
     *
     * @param bloggerId 博主id
     * @param category  类别id
     * @return 查询结果
     */
    List<BloggerPicture> listPictureByBloggerAndCategory(@Param("bloggerId") Long bloggerId,
                                                         @Param("category") Integer category);

    /**
     * 将图片被引用次数减一
     *
     * @param pictureId 图片id
     * @return 操作影响的行数
     */
    int updateUseCountPlus(Long pictureId);

    /**
     * 将图片被引用次数加一
     *
     * @param pictureId 图片id
     * @return 操作影响的行数
     */
    int updateUseCountMinus(Long pictureId);

    /**
     * 获得图片被引用次数
     *
     * @param pictureId 图片id
     * @return 操作影响的行数
     */
    int getUseCount(Long pictureId);

    /**
     * 根据博主id获得其所有图片
     *
     * @param bloggerId 博主id
     * @return 查询结果
     */
    List<BloggerPicture> getPictureByBloggerId(Long bloggerId);
}
