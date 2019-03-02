package com.duan.blogos.websample.vo;

import com.duan.blogos.service.common.dto.blogger.BloggerStatisticsDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * Created on 2019/3/2.
 *
 * @author DuanJiaNing
 */
@Data
public class BloggerBriefVO implements Serializable {

    // 博主统计信息
    private BloggerStatisticsDTO statistics;

    private String nameBase64;

    private String name;

    private String aboutMe;

    private Long avatarId;

    private Long id;

}
