package com.duan.blogos.service.manager;

import com.duan.blogos.service.config.preference.FileProperties;
import com.duan.blogos.service.config.preference.WebsiteProperties;
import com.duan.blogos.service.entity.blogger.BloggerPicture;
import com.duan.blogos.service.enums.BloggerPictureCategoryEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created on 2018/1/2.
 * 字符拼接管理者
 *
 * @author DuanJiaNing
 */
@Component
public class StringConstructorManager {

    @Autowired
    private WebsiteProperties websiteProperties;

    @Autowired
    private FileProperties fileProperties;

    /**
     * 构造图片的访问url
     *
     * @param picture     图片
     * @param defaultCate 图片无法获取时使用的默认图片所属类别
     * @return 获取图片的url
     */
    public String constructPictureUrl(BloggerPicture picture, BloggerPictureCategoryEnum defaultCate) {
        if (picture == null) return null;

        // 参见ImageController：http://localhost:8080/image/prv/523?category=5
        StringBuilder buffer = new StringBuilder(50);
        int cate = picture.getCategory();
        buffer.append("http://")
                .append(websiteProperties.getAddr())
                .append("/image/")
                // 私有图片登录才能获取
                .append(cate == BloggerPictureCategoryEnum.PRIVATE.getCode() ? "prv/" : "")
                .append(picture.getId())
                .append("?category=")
                .append(defaultCate == null ? BloggerPictureCategoryEnum.DEFAULT_PICTURE.getCode() :
                        defaultCate.getCode());

        return buffer.toString();
    }

    /**
     * 拼接图片保存位置所在文件夹路径
     *
     * @param bloggerName  博主名
     * @param categoryName 图片所属类别名
     * @return 文件夹路径
     */
    public String constructImageDirPath(String bloggerName, String categoryName) {
        String rootDirPath = fileProperties.getImageRootPath();
        return rootDirPath + File.separator + bloggerName + File.separator + categoryName;
    }
}
