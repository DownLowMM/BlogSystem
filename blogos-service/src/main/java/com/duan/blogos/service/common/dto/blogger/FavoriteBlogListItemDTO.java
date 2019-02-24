package com.duan.blogos.service.common.dto.blogger;

import com.duan.blogos.service.common.dto.blog.BlogListItemDTO;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created on 2017/12/18.
 * 博主收藏的博文清单
 *
 * @author DuanJiaNing
 */
@Data
public class FavoriteBlogListItemDTO implements Serializable {

    // 记录id
    private Long id;

    //博主id
    private Long bloggerId;

    // 博文内容
    private BlogListItemDTO blog;

    //作者id
    private BloggerDTO author;

    //收藏/喜欢理由
    private String reason;

    //收藏/喜欢时间
    private Timestamp date;

}
