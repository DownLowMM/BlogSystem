package com.duan.blogos.service.dao.blogger;

import com.duan.blogos.service.dao.BaseDao;
import com.duan.blogos.service.entity.blogger.BloggerLink;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created on 2017/12/29.
 *
 * @author DuanJiaNing
 */
@Repository
public interface BloggerLinkDao extends BaseDao<BloggerLink> {

    /**
     * 根据博主id查询其友情链接
     *
     * @param bloggerId 博主id
     * @return 查询结果
     */
    List<BloggerLink> listBlogLinkByBloggerId(@Param("bloggerId") Long bloggerId);

    /**
     * 检查链接是否存在
     *
     * @param linkId 链接id
     * @return 链接id
     */
    Long getLinkForCheckExist(Long linkId);

    /**
     * 根据id查询链接
     *
     * @param linkId id
     * @return 查询结果
     */
    BloggerLink getLink(Long linkId);

    /**
     * 统计博主的链接数量
     *
     * @param bloggerId 博主id
     * @return 统计结果
     */
    int countLinkByBloggerId(Long bloggerId);
}
