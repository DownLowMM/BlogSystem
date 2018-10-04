package com.duan.blogos.service.service.blogger;


import com.duan.blogos.service.dto.blogger.BloggerLinkDTO;
import com.duan.blogos.service.restful.PageResult;
import com.duan.blogos.service.restful.ResultModel;

/**
 * Created on 2017/12/18.
 * 博主友情链接服务
 *
 * @author DuanJiaNing
 */
public interface BloggerLinkService {

    /**
     * 获取友情链接，自动按优先级从高到底排序
     *
     * @param bloggerId 博主id
     * @return 查询结果
     */
    ResultModel<PageResult<BloggerLinkDTO>> listBloggerLink(Long bloggerId, Integer pageNum, Integer pageSize);

    /**
     * 新增友情链接
     *
     * @param bloggerId 博主id
     * @param iconId    图标id
     * @param title     名字
     * @param url       url
     * @param bewrite   描述
     * @return 新纪录id
     */
    Long insertBloggerLink(Long bloggerId, Long iconId, String title, String url, String bewrite);

    /**
     * 删除链接
     *
     * @param linkId 链接id
     * @return 删除成功为true
     */
    boolean deleteBloggerLink(Long linkId);

    /**
     * 更新链接
     *
     * @param linkId     链接id
     * @param newIconId  新的图片id，不改变传 -1
     * @param newTitle   新的标题，不改变传 null
     * @param newUrl     新的url，不改变传 null
     * @param newBewrite 新的描述，不改变传 null
     * @return 更新失败为false
     */
    boolean updateBloggerLink(Long linkId, Long newIconId, String newTitle,
                              String newUrl, String newBewrite);

    /**
     * 检查链接是否存在
     *
     * @param linkId 链接id
     * @return 存在为true
     */
    boolean getLinkForCheckExist(Long linkId);
}
