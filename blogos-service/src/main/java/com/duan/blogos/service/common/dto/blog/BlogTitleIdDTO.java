package com.duan.blogos.service.common.dto.blog;

import lombok.Data;

import java.io.Serializable;

/**
 * Created on 2018/4/4.
 *
 * @author DuanJiaNing
 */
@Data
public class BlogTitleIdDTO implements Serializable {

    private String title;

    private Long id;

}
