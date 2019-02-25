package com.duan.blogos.websample.vo;

import com.duan.blogos.service.common.dto.blogger.BloggerSettingDTO;
import com.duan.blogos.service.common.dto.blogger.BloggerStatisticsDTO;
import lombok.Data;

/**
 * Created on 2019/2/25.
 *
 * @author DuanJiaNing
 */
@Data
public class PageOwnerBloggerVO extends BloggerVO {

    private String intro;

    private String aboutMe;

    private Long avatarId;

    private BloggerStatisticsDTO Statistics;

    private BloggerSettingDTO setting;

}
