package com.duan.blogos.websample.vo;

import com.duan.blogos.service.common.dto.blogger.BloggerAccountDTO;
import com.duan.blogos.service.common.dto.blogger.BloggerProfileDTO;
import com.duan.blogos.service.common.dto.blogger.BloggerSettingDTO;
import com.duan.blogos.service.common.dto.blogger.BloggerStatisticsDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * Created on 2019/2/25.
 *
 * @author DuanJiaNing
 */
@Data
public class BloggerVO implements Serializable {

    private BloggerAccountDTO account;

    private String nameBase64;

    private BloggerStatisticsDTO Statistics;

    private BloggerProfileDTO profile;

    private BloggerSettingDTO setting;
}
