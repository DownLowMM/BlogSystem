package com.duan.blogos.service.common.dto.blogger;

import lombok.Data;

import java.io.Serializable;

/**
 * Created on 2018/5/4.
 *
 * @author DuanJiaNing
 */
@Data
public class BloggerBriefDTO implements Serializable {

    // 博主统计信息
    private BloggerStatisticsDTO statistics;

    // 博主 dto
    private BloggerDTO blogger;
}
